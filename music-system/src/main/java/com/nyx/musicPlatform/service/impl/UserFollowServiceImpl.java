package com.nyx.musicPlatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nyx.musicPlatform.entity.UserFollow;
import com.nyx.musicPlatform.mapper.UserFollowMapper;
import com.nyx.musicPlatform.service.UserFollowService;
import com.nyx.musicPlatform.vo.UserDetailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户关注关系服务实现类
 * 
 * @author nyx
 * @since 2025-06
 */
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {

    private static final Logger log = LoggerFactory.getLogger(UserFollowServiceImpl.class);

    @Override
    @Transactional
    public boolean followUser(Long followerId, Long followingId) {
        try {
            // 参数验证
            if (followerId == null || followingId == null) {
                throw new IllegalArgumentException("关注者ID和被关注者ID不能为空");
            }
            
            // 不能关注自己
            if (followerId.equals(followingId)) {
                throw new IllegalArgumentException("不能关注自己");
            }
            
            // 检查是否已经关注
            if (isFollowing(followerId, followingId)) {
                throw new IllegalArgumentException("已经关注了该用户");
            }
            
            // 创建关注关系
            UserFollow userFollow = new UserFollow()
                    .setFollowerId(followerId)
                    .setFollowingId(followingId)
                    .setCreatedAt(LocalDateTime.now());
            
            return save(userFollow);
        } catch (Exception e) {
            log.error("关注用户失败: followerId={}, followingId={}", followerId, followingId, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean unfollowUser(Long followerId, Long followingId) {
        try {
            // 参数验证
            if (followerId == null || followingId == null) {
                throw new IllegalArgumentException("关注者ID和被关注者ID不能为空");
            }
            
            // 检查是否已经关注
            if (!isFollowing(followerId, followingId)) {
                throw new IllegalArgumentException("尚未关注该用户");
            }
            
            // 删除关注关系
            QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("follower_id", followerId)
                       .eq("following_id", followingId);
            
            return remove(queryWrapper);
        } catch (Exception e) {
            log.error("取消关注用户失败: followerId={}, followingId={}", followerId, followingId, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        if (followerId == null || followingId == null) {
            return false;
        }
        return baseMapper.isFollowing(followerId, followingId);
    }

    @Override
    public List<UserDetailVO> getFollowingList(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return baseMapper.getFollowingList(userId);
    }

    @Override
    public List<UserDetailVO> getFollowersList(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return baseMapper.getFollowersList(userId);
    }

    @Override
    public Long getFollowingCount(Long userId) {
        if (userId == null) {
            return 0L;
        }
        return baseMapper.getFollowingCount(userId);
    }

    @Override
    public Long getFollowerCount(Long userId) {
        if (userId == null) {
            return 0L;
        }
        return baseMapper.getFollowerCount(userId);
    }
}