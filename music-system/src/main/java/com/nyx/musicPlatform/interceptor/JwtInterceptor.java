package com.nyx.musicPlatform.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.common.utils.JwtUtils;
import com.nyx.musicPlatform.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT认证拦截器
 * 
 * @author nyx
 * @since 2025-06
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisUtils redisUtils;

    // Token在Redis中的前缀
    private static final String TOKEN_PREFIX = "token:";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String requestURI = request.getRequestURI();
        
        // 记录请求信息
        log.info("JWT拦截器 - 请求路径: {}, 请求方法: {}", requestURI, request.getMethod());
        
        // 跨域预检请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 白名单路径，无需验证Token
        if (isWhiteList(requestURI)) {
            log.info("白名单路径，直接放行: {}", requestURI);
            return true;
        }
        
        // 获取Token
        String token = jwtUtils.getTokenFromRequest(request);
        
        // Token为空
        if (!StringUtils.hasText(token)) {
            log.warn("Token缺失 - 请求路径: {}", requestURI);
            writeErrorResponse(response, ResultCode.TOKEN_MISSING, "Token缺失，请先登录");
            return false;
        }
        
        // 验证Token
        try {
            if (!jwtUtils.validateToken(token)) {
                log.warn("Token无效 - 请求路径: {}, Token: {}", requestURI, token);
                writeErrorResponse(response, ResultCode.TOKEN_INVALID, "Token无效，请重新登录");
                return false;
            }
            
            // 检查Token是否过期
            if (jwtUtils.isTokenExpired(token)) {
                log.warn("Token已过期 - 请求路径: {}, Token: {}", requestURI, token);
                writeErrorResponse(response, ResultCode.TOKEN_EXPIRED, "Token已过期，请重新登录");
                return false;
            }
            
            // 获取用户信息
            String username = jwtUtils.getUsernameFromToken(token);
            Long userId = jwtUtils.getUserIdFromToken(token);
            
            // 检查Token是否在Redis中存在（支持主动失效）
            if (userId != null) {
                String tokenKey = TOKEN_PREFIX + userId;
                Object redisTokenObj = redisUtils.get(tokenKey);
                if (redisTokenObj == null) {
                    log.warn("Token已失效（Redis中不存在） - 请求路径: {}, 用户ID: {}", requestURI, userId);
                    writeErrorResponse(response, ResultCode.TOKEN_INVALID, "Token已失效，请重新登录");
                    return false;
                }
                String redisToken = (String) redisTokenObj;
                if (!token.equals(redisToken)) {
                    log.warn("Token不匹配 - 请求路径: {}, 用户ID: {}", requestURI, userId);
                    writeErrorResponse(response, ResultCode.TOKEN_INVALID, "Token已失效，请重新登录");
                    return false;
                }
            }
            
            // 设置用户信息到请求属性中
            
            if (StringUtils.hasText(username) && userId != null) {
                request.setAttribute("username", username);
                request.setAttribute("userId", userId);
                log.info("Token验证成功 - 用户: {}, ID: {}, 请求路径: {}", username, userId, requestURI);
                return true;
            } else {
                log.warn("Token中用户信息无效 - 请求路径: {}, Token: {}", requestURI, token);
                writeErrorResponse(response, ResultCode.TOKEN_INVALID, "Token中用户信息无效");
                return false;
            }
            
        } catch (Exception e) {
            log.error("Token验证异常 - 请求路径: " + requestURI + ", Token: " + token + ", 异常: " + e.getMessage());
            writeErrorResponse(response, ResultCode.TOKEN_INVALID, "Token验证失败");
            return false;
        }
    }
    
    /**
     * 判断是否为白名单路径
     * 
     * @param requestURI 请求路径
     * @return 是否为白名单路径
     */
    private boolean isWhiteList(String requestURI) {
        // 定义白名单路径
        String[] whiteList = {
            // 用户相关
            "/api/user/register",
            "/api/user/login",
            "/api/user/refresh-token",
            "/api/auth/login",
            "/api/auth/register",
            "/api/login",
            "/api/register",
            "/api/logout",
            
            // 音乐相关（公开访问）
            "/api/music/list",
            "/api/music/hot",
            "/api/music/latest",
            "/api/music/search",
            "/api/music/search/title",
            "/api/music/search/artist",
            "/api/music/search/album",
            "/api/music/recommend",
            "/api/music/*",
            "/api/music/stream/**",
            "/api/music/cover/**",
            
            // Banner相关（公开访问）
            "/api/banner/active",
            
            // SpringDoc OpenAPI相关
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/doc.html",
            
            // 静态资源
            "/static/**",
            "/public/**",
            "/assets/**",
            "/favicon.ico",
            
            // 健康检查
            "/actuator/**",
            "/health",
            "/info",
            
            // 错误页面
            "/error"
        };
        
        for (String pattern : whiteList) {
            if (isPathMatch(pattern, requestURI)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 路径匹配（支持通配符）
     * 
     * @param pattern 模式
     * @param path 路径
     * @return 是否匹配
     */
    private boolean isPathMatch(String pattern, String path) {
        if (pattern.equals(path)) {
            return true;
        }
        
        // 支持 /** 通配符
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(prefix);
        }
        
        // 支持 /* 通配符
        if (pattern.endsWith("/*")) {
            String prefix = pattern.substring(0, pattern.length() - 2);
            return path.startsWith(prefix) && (path.length() == prefix.length() || path.charAt(prefix.length()) == '/');
        }
        
        return false;
    }
    
    /**
     * 写入错误响应
     * 
     * @param response HTTP响应
     * @param resultCode 结果码
     * @param message 错误消息
     * @throws IOException IO异常
     */
    private void writeErrorResponse(HttpServletResponse response, ResultCode resultCode, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // 设置CORS头
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        Result result = Result.error(resultCode, message);
        String jsonResponse = objectMapper.writeValueAsString(result);
        
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResponse);
            writer.flush();
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理请求属性
        request.removeAttribute("username");
        request.removeAttribute("userId");
        
        if (ex != null) {
            log.error("请求处理异常 - 请求路径: " + request.getRequestURI() + ", 异常: " + ex.getMessage());
        }
    }
}