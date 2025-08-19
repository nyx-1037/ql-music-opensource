package com.nyx.musicPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nyx.musicPlatform.entity.UserFollow;
import com.nyx.musicPlatform.vo.UserDetailVO;

import java.util.List;

/**
 * 用户关注关系服务接口
 * 
 * @author nyx
 * @since 2025-06
 */
public interface UserFollowService extends IService<UserFollow> {

    /**
     * 关注用户
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否成功
     */
    boolean followUser(Long followerId, Long followingId);

    /**
     * 取消关注用户
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否成功
     */
    boolean unfollowUser(Long followerId, Long followingId);

    /**
     * 检查用户是否关注了另一个用户
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否关注
     */
    boolean isFollowing(Long followerId, Long followingId);

    /**
     * 获取用户的关注列表
     * 
     * @param userId 用户ID
     * @return 关注的用户列表
     */
    List<UserDetailVO> getFollowingList(Long userId);

    /**
     * 获取用户的粉丝列表
     * 
     * @param userId 用户ID
     * @return 粉丝用户列表
     */
    List<UserDetailVO> getFollowersList(Long userId);

    /**
     * 获取用户的关注数量
     * 
     * @param userId 用户ID
     * @return 关注数量
     */
    Long getFollowingCount(Long userId);

    /**
     * 获取用户的粉丝数量
     * 
     * @param userId 用户ID
     * @return 粉丝数量
     */
    Long getFollowerCount(Long userId);
}