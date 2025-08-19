package com.nyx.musicPlatform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.dto.UserUpdateDTO;
import com.nyx.musicPlatform.entity.User;
import com.nyx.musicPlatform.service.OssFileService;
import com.nyx.musicPlatform.service.UserService;
import com.nyx.musicPlatform.common.utils.JwtUtils;
import com.nyx.musicPlatform.vo.UserDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author nyx
 * @since 2025-06
 */
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OssFileService ossFileService;



    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前用户信息接口")
    @GetMapping("/info")
    public Result getCurrentUser(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token)) {
                return Result.error(ResultCode.TOKEN_MISSING, "Token缺失");
            }

            if (!jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            UserDetailVO userDetail = userService.getUserDetail(userId);
            if (userDetail != null) {
                return Result.success(userDetail, "获取成功");
            } else {
                return Result.error(ResultCode.USER_NOT_FOUND, "用户不存在");
            }
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 根据用户ID获取用户信息
     */
    @Operation(summary = "根据用户ID获取用户信息", description = "根据用户ID获取用户信息接口")
    @GetMapping("/info/{userId}")
    public Result getUserById(@Parameter(description = "用户ID") @PathVariable Long userId) {
        try {
            if (userId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "用户ID不能为空");
            }

            UserDetailVO userDetail = userService.getUserDetail(userId);
            if (userDetail != null) {
                // 隐私保护，不返回邮箱
                userDetail.setEmail(null);
                return Result.success(userDetail, "获取成功");
            } else {
                return Result.error(ResultCode.USER_NOT_FOUND, "用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息", description = "更新用户信息接口")
    @PutMapping("/update")
    public Result<User> updateUser(@RequestBody UserUpdateDTO request, HttpServletRequest httpRequest) {
        try {
            // 从请求头获取token
            String token = httpRequest.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return Result.error(ResultCode.UNAUTHORIZED, "未提供有效的认证令牌");
            }
            
            token = token.substring(7); // 移除 "Bearer " 前缀
            
            // 验证token并获取用户ID
            Long userId = jwtUtils.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error(ResultCode.UNAUTHORIZED, "无效的认证令牌");
            }
            
            // 获取当前用户
            User currentUser = userService.getById(userId);
            if (currentUser == null) {
                return Result.error(ResultCode.USER_NOT_FOUND, "用户不存在");
            }
            
            // 更新用户信息
            if (request.getNickname() != null) {
                currentUser.setNickname(request.getNickname());
            }
            if (request.getEmail() != null) {
                currentUser.setEmail(request.getEmail());
            }
            if (request.getAvatarUrl() != null) {
                currentUser.setAvatarUrl(request.getAvatarUrl());
            }
            
            currentUser.setUpdateTime(LocalDateTime.now());
            
            // 保存更新
            boolean updated = userService.updateById(currentUser);
            if (!updated) {
                return Result.error(ResultCode.SYSTEM_ERROR, "更新用户信息失败");
            }
            
            return Result.success(currentUser);
            
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "更新用户信息失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/upload-avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest httpRequest) {
        try {
            // 从请求头获取token
            String token = httpRequest.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return Result.error(ResultCode.UNAUTHORIZED, "未提供有效的认证令牌");
            }
            
            token = token.substring(7); // 移除 "Bearer " 前缀
            
            // 验证token并获取用户ID
            Long userId = jwtUtils.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error(ResultCode.UNAUTHORIZED, "无效的认证令牌");
            }
            
            // 验证文件
            if (file == null || file.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "请选择要上传的头像文件");
            }
            
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error(ResultCode.PARAM_ERROR, "只支持上传图片文件");
            }
            
            // 验证文件大小（限制为5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                return Result.error(ResultCode.PARAM_ERROR, "头像文件大小不能超过5MB");
            }
            
            // 上传头像到OSS
            String avatarUrl = ossFileService.uploadAvatarFile(file);
            if (avatarUrl == null) {
                return Result.error(ResultCode.SYSTEM_ERROR, "头像上传失败");
            }
            
            // 更新用户头像URL
            User currentUser = userService.getById(userId);
            if (currentUser == null) {
                return Result.error(ResultCode.USER_NOT_FOUND, "用户不存在");
            }
            
            currentUser.setAvatarUrl(avatarUrl);
            currentUser.setUpdateTime(LocalDateTime.now());
            
            boolean updated = userService.updateById(currentUser);
            if (!updated) {
                return Result.error(ResultCode.SYSTEM_ERROR, "更新用户头像失败");
            }
            
            return Result.success(avatarUrl);
            
        } catch (Exception e) {
            log.error("上传头像失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "上传头像失败: " + e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码", description = "修改密码接口")
    @PutMapping("/password")
    public Result changePassword(
            @Parameter(description = "旧密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 参数验证
            if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
                return Result.error(ResultCode.PARAM_ERROR, "旧密码和新密码不能为空");
            }

            // 新密码长度验证
            if (newPassword.length() < 6 || newPassword.length() > 20) {
                return Result.error(ResultCode.PARAM_ERROR, "新密码长度必须在6-20个字符之间");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            boolean result = userService.changePassword(userId, oldPassword, newPassword);
            if (result) {
                return Result.success("密码修改成功");
            } else {
                return Result.error(ResultCode.USER_PASSWORD_CHANGE_FAILED, "密码修改失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.USER_PASSWORD_CHANGE_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 分页查询用户列表（管理员功能）
     */
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表接口")
    @GetMapping("/list")
    public Result getUserList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 这里可以添加管理员权限验证
            
            Page<User> page = new Page<>(current, size);
            IPage<User> userPage;
            
            if (StringUtils.hasText(keyword)) {
                userPage = userService.searchUsers(page, keyword);
            } else {
                userPage = userService.getUserList(page);
            }

            // 清除密码信息
            userPage.getRecords().forEach(user -> user.setPassword(null));
            
            return Result.success(userPage, "查询成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 启用/禁用用户（管理员功能）
     */
    @Operation(summary = "启用/禁用用户", description = "启用/禁用用户接口")
    @PutMapping("/status/{userId}")
    public Result updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "状态：1-启用，0-禁用") @RequestParam Integer status,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 这里可以添加管理员权限验证
            
            if (userId == null || status == null) {
                return Result.error(ResultCode.PARAM_ERROR, "参数不能为空");
            }

            if (status != 0 && status != 1) {
                return Result.error(ResultCode.PARAM_ERROR, "状态值只能是0或1");
            }

            boolean result = userService.updateUserStatus(userId, status);
            if (result) {
                String message = status == 1 ? "用户启用成功" : "用户禁用成功";
                return Result.success(message);
            } else {
                return Result.error(ResultCode.USER_UPDATE_FAILED, "操作失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.USER_UPDATE_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 删除用户（管理员功能）
     */
    @Operation(summary = "删除用户", description = "删除用户接口")
    @DeleteMapping("/{userId}")
    public Result deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 这里可以添加管理员权限验证
            
            if (userId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "用户ID不能为空");
            }

            boolean result = userService.deleteUser(userId);
            if (result) {
                return Result.success("删除成功");
            } else {
                return Result.error(ResultCode.USER_DELETE_FAILED, "删除失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.USER_DELETE_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 批量删除用户（管理员功能）
     */
    @Operation(summary = "批量删除用户", description = "批量删除用户接口")
    @DeleteMapping("/batch")
    public Result deleteUsersBatch(
            @Parameter(description = "用户ID列表") @RequestBody List<Long> userIds,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 这里可以添加管理员权限验证
            
            if (userIds == null || userIds.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "用户ID列表不能为空");
            }

            boolean result = userService.deleteUsers(userIds);
            if (result) {
                return Result.success("批量删除成功");
            } else {
                return Result.error(ResultCode.USER_DELETE_FAILED, "批量删除失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.USER_DELETE_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 重置用户密码（管理员功能）
     */
    @Operation(summary = "重置用户密码", description = "重置用户密码接口")
    @PutMapping("/reset-password/{userId}")
    public Result resetPassword(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "新密码") @RequestParam String newPassword,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 这里可以添加管理员权限验证
            
            if (userId == null || !StringUtils.hasText(newPassword)) {
                return Result.error(ResultCode.PARAM_ERROR, "用户ID和新密码不能为空");
            }

            // 新密码长度验证
            if (newPassword.length() < 6 || newPassword.length() > 20) {
                return Result.error(ResultCode.PARAM_ERROR, "新密码长度必须在6-20个字符之间");
            }

            boolean result = userService.resetPassword(userId, newPassword);
            if (result) {
                return Result.success("密码重置成功");
            } else {
                return Result.error(ResultCode.USER_PASSWORD_RESET_FAILED, "密码重置失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.USER_PASSWORD_RESET_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取用户统计信息（管理员功能）
     */
    @Operation(summary = "获取用户统计信息", description = "获取用户统计信息接口")
    @GetMapping("/statistics")
    public Result getUserStatistics(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 这里可以添加管理员权限验证
            
            // 获取当前用户ID或使用管理员权限获取所有统计信息
            Long userId = jwtUtils.getUserIdFromToken(token);
            Object statistics = userService.getUserStatistics(userId);
            return Result.success(statistics, "获取成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 刷新Token
     */
    @Operation(summary = "刷新Token", description = "刷新Token接口")
    @PostMapping("/refresh-token")
    public Result refreshToken(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token)) {
                return Result.error(ResultCode.TOKEN_MISSING, "Token缺失");
            }

            // 验证Token是否有效（即使过期也要能解析）
            String username = jwtUtils.getUsernameFromToken(token);
            if (!StringUtils.hasText(username)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 生成新的Token
            User user = userService.findByUsername(username);
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_FOUND, "用户不存在");
            }

            String newAccessToken = jwtUtils.generateAccessToken(user.getId(), user.getUsername(), "USER");
            String newRefreshToken = jwtUtils.generateRefreshToken(user.getId(), user.getUsername());

            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("accessToken", newAccessToken);
            tokenData.put("refreshToken", newRefreshToken);
            tokenData.put("tokenType", "Bearer");
            tokenData.put("expiresIn", String.valueOf(jwtUtils.getTokenRemainingTime(newAccessToken)));

            return Result.success(tokenData, "Token刷新成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 关注用户
     */
    @Operation(summary = "关注用户", description = "关注用户接口")
    @PostMapping("/follow/{userId}")
    public Result followUser(
            @Parameter(description = "被关注用户ID") @PathVariable Long userId,
            HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long currentUserId = jwtUtils.getUserIdFromToken(token);
            if (currentUserId == null) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            if (userId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "用户ID不能为空");
            }

            boolean result = userService.followUser(currentUserId, userId);
            if (result) {
                return Result.success("关注成功");
            } else {
                return Result.error(ResultCode.SYSTEM_ERROR, "关注失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.PARAM_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("关注用户失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 取消关注用户
     */
    @Operation(summary = "取消关注用户", description = "取消关注用户接口")
    @DeleteMapping("/follow/{userId}")
    public Result unfollowUser(
            @Parameter(description = "被取消关注用户ID") @PathVariable Long userId,
            HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long currentUserId = jwtUtils.getUserIdFromToken(token);
            if (currentUserId == null) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            if (userId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "用户ID不能为空");
            }

            boolean result = userService.unfollowUser(currentUserId, userId);
            if (result) {
                return Result.success("取消关注成功");
            } else {
                return Result.error(ResultCode.SYSTEM_ERROR, "取消关注失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.PARAM_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("取消关注用户失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 检查关注状态
     */
    @Operation(summary = "检查关注状态", description = "检查是否关注了指定用户")
    @GetMapping("/follow/status/{userId}")
    public Result checkFollowStatus(
            @Parameter(description = "被检查用户ID") @PathVariable Long userId,
            HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long currentUserId = jwtUtils.getUserIdFromToken(token);
            if (currentUserId == null) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            if (userId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "用户ID不能为空");
            }

            boolean isFollowing = userService.isFollowing(currentUserId, userId);
            Map<String, Object> result = new HashMap<>();
            result.put("isFollowing", isFollowing);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            log.error("检查关注状态失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取关注列表
     */
    @Operation(summary = "获取关注列表", description = "获取用户的关注列表")
    @GetMapping("/following/{userId}")
    public Result getFollowingList(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        try {
            if (userId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "用户ID不能为空");
            }

            List<UserDetailVO> followingList = userService.getFollowingList(userId);
            return Result.success(followingList, "获取成功");
        } catch (Exception e) {
            log.error("获取关注列表失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取粉丝列表
     */
    @Operation(summary = "获取粉丝列表", description = "获取用户的粉丝列表")
    @GetMapping("/followers/{userId}")
    public Result getFollowersList(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        try {
            if (userId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "用户ID不能为空");
            }

            List<UserDetailVO> followersList = userService.getFollowersList(userId);
            return Result.success(followersList, "获取成功");
        } catch (Exception e) {
            log.error("获取粉丝列表失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }
}