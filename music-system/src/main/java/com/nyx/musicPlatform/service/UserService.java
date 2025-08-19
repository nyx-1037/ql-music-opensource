package com.nyx.musicPlatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nyx.musicPlatform.entity.User;
import com.nyx.musicPlatform.vo.UserDetailVO;

/**
 * 用户服务接口
 * 
 * @author nyx
 * @since 2025-06
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @param nickname 昵称
     * @return 注册结果
     */
    boolean register(String username, String password, String email, String nickname);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录用户信息
     */
    User login(String username, String password);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(String email);

    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 存在返回true，不存在返回false
     */
    boolean existsByEmail(String email);

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param nickname 昵称
     * @param email 邮箱
     * @param avatarUrl 头像URL
     * @return 更新结果
     */
    boolean updateUserInfo(Long userId, String nickname, String email, String avatarUrl);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 启用/禁用用户
     * @param userId 用户ID
     * @param status 状态（0-禁用，1-启用）
     * @return 操作结果
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 获取用户状态
     * @param userId 用户ID
     * @return 用户状态
     */
    Integer getUserStatus(Long userId);

    /**
     * 检查用户是否启用
     * @param userId 用户ID
     * @return 启用返回true，禁用返回false
     */
    boolean isUserEnabled(Long userId);

    /**
     * 分页查询用户列表
     * @param page 分页对象
     * @return 用户分页列表
     */
    IPage<User> getUserList(Page<User> page);

    /**
     * 根据关键词搜索用户
     * @param page 分页对象
     * @param keyword 搜索关键词（用户名或昵称）
     * @return 用户分页列表
     */
    IPage<User> searchUsers(Page<User> page, String keyword);

    /**
     * 获取用户统计信息
     * @param userId 用户ID
     * @return 用户统计信息（包含音乐数量等）
     */
    Object getUserStatistics(Long userId);

    /**
     * 删除用户（逻辑删除）
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteUser(Long userId);

    /**
     * 批量删除用户（逻辑删除）
     * @param userIds 用户ID列表
     * @return 删除结果
     */
    boolean deleteUsers(java.util.List<Long> userIds);

    /**
     * 重置密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 重置结果
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密密码
     * @return 验证结果
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);

    /**
     * 加密密码
     * @param password 原始密码
     * @return 加密后的密码
     */
    String encodePassword(String password);

    /**
     * 获取用户详细信息
     * @param userId 用户ID
     * @return 用户详细信息VO
     */
    UserDetailVO getUserDetail(Long userId);

    /**
     * 关注用户
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 关注结果
     */
    boolean followUser(Long followerId, Long followingId);

    /**
     * 取消关注用户
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 取消关注结果
     */
    boolean unfollowUser(Long followerId, Long followingId);

    /**
     * 检查用户是否关注了另一个用户
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否关注
     */
    boolean isFollowing(Long followerId, Long followingId);

    /**
     * 获取用户的关注列表
     * @param userId 用户ID
     * @return 关注的用户列表
     */
    java.util.List<UserDetailVO> getFollowingList(Long userId);

    /**
     * 获取用户的粉丝列表
     * @param userId 用户ID
     * @return 粉丝用户列表
     */
    java.util.List<UserDetailVO> getFollowersList(Long userId);
}