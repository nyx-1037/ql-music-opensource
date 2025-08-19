package com.nyx.musicPlatform.controller;

import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.entity.User;
import com.nyx.musicPlatform.service.UserService;
import com.nyx.musicPlatform.common.utils.JwtUtils;
import com.nyx.musicPlatform.utils.RedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 
 * @author nyx
 * @since 2025-06
 */
@Tag(name = "用户认证", description = "用户认证相关接口")
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisUtils redisUtils;

    // Token在Redis中的前缀
    private static final String TOKEN_PREFIX = "token:";
    // Token过期时间（秒）- 与JWT过期时间保持一致
    private static final long TOKEN_EXPIRE_TIME = 86400; // 24小时

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录接口")
    @PostMapping("/user/login")
    public Result login(@RequestBody Map<String, Object> loginRequest) {
        
        try {
            // 从请求体中获取参数
            String username = (String) loginRequest.get("username");
            String password = (String) loginRequest.get("password");
            
            // 参数验证
            if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
                return Result.error(ResultCode.PARAM_ERROR, "用户名和密码不能为空");
            }

            User user = userService.login(username, password);
            if (user != null) {
                // 生成JWT token
                String token = jwtUtils.generateAccessToken(user.getId(), user.getUsername(), "USER");
                
                // 将token存储到Redis中，支持主动失效
                String tokenKey = TOKEN_PREFIX + user.getId();
                redisUtils.set(tokenKey, token, TOKEN_EXPIRE_TIME);
                
                Map<String, Object> loginResult = new HashMap<>();
                loginResult.put("token", token);
                loginResult.put("user", user);
                return Result.success(loginResult, "登录成功");
            } else {
                return Result.error(ResultCode.USER_LOGIN_FAILED, "用户名或密码错误");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.USER_LOGIN_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "用户注册接口")
    @PostMapping("/user/register")
    public Result register(@RequestBody Map<String, Object> registerRequest) {
        
        try {
            // 从请求体中获取参数
            String username = (String) registerRequest.get("username");
            String password = (String) registerRequest.get("password");
            String email = (String) registerRequest.get("email");
            String nickname = (String) registerRequest.get("nickname");
            
            // 参数验证
            if (!StringUtils.hasText(username) || !StringUtils.hasText(password) || !StringUtils.hasText(email)) {
                return Result.error(ResultCode.PARAM_ERROR, "用户名、密码和邮箱不能为空");
            }

            // 用户名长度验证
            if (username.length() < 3 || username.length() > 20) {
                return Result.error(ResultCode.PARAM_ERROR, "用户名长度必须在3-20个字符之间");
            }

            // 密码长度验证
            if (password.length() < 6 || password.length() > 20) {
                return Result.error(ResultCode.PARAM_ERROR, "密码长度必须在6-20个字符之间");
            }

            // 邮箱格式验证
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                return Result.error(ResultCode.PARAM_ERROR, "邮箱格式不正确");
            }

            boolean result = userService.register(username, password, email, nickname);
            if (result) {
                return Result.success("注册成功");
            } else {
                return Result.error(ResultCode.USER_REGISTER_FAILED, "注册失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.USER_REGISTER_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出", description = "用户登出接口")
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (StringUtils.hasText(token)) {
                // 从token中获取用户ID
                Long userId = jwtUtils.getUserIdFromToken(token);
                if (userId != null) {
                    // 从Redis中删除token
                    String tokenKey = TOKEN_PREFIX + userId;
                    redisUtils.del(tokenKey);
                }
                return Result.success("登出成功");
            }
            return Result.success("登出成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 检查认证状态
     */
    @Operation(summary = "检查认证状态", description = "检查用户认证状态接口")
    @GetMapping("/user/check-auth")
    public Result checkAuth(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token不存在");
            }

            // 验证token有效性
            if (!jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效或已过期");
            }

            // 从token中获取用户ID
            Long userId = jwtUtils.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token格式错误");
            }

            // 获取用户信息
            User user = userService.getUserById(userId);
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_FOUND, "用户不存在");
            }

            // 构造前端期望的数据结构
            Map<String, Object> authData = new HashMap<>();
            authData.put("user", user);
            authData.put("isAuthenticated", true);
            
            return Result.success(authData, "认证有效");
        } catch (Exception e) {
            return Result.error(ResultCode.TOKEN_INVALID, "Token验证失败");
        }
    }
}