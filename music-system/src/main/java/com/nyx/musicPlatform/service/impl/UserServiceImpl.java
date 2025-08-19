package com.nyx.musicPlatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.entity.User;
import com.nyx.musicPlatform.mapper.MusicMapper;
import com.nyx.musicPlatform.mapper.UserMapper;
import com.nyx.musicPlatform.service.UserService;
import com.nyx.musicPlatform.service.UserFollowService;
import com.nyx.musicPlatform.utils.RedisUtils;
import com.nyx.musicPlatform.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 * 
 * @author nyx
 * @since 2025-06
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private com.nyx.musicPlatform.service.MusicFavoriteService musicFavoriteService;

    @Autowired
    private com.nyx.musicPlatform.service.PlaylistService playlistService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String USER_CACHE_PREFIX = "user:info:";
    private static final long USER_CACHE_TTL = 1800; // 30分钟

    @Override
    public boolean register(String username, String password, String email, String nickname) {
        // 参数验证
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password) || !StringUtils.hasText(email)) {
            throw new RuntimeException("用户名、密码和邮箱不能为空");
        }

        // 检查用户名是否已存在
        if (existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (existsByEmail(email)) {
            throw new RuntimeException("邮箱已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword(password));
        user.setEmail(email);
        user.setNickname(StringUtils.hasText(nickname) ? nickname : username);
        user.setStatus(1); // 默认启用

        return save(user);
    }

    @Override
    public User login(String username, String password) {
        // 参数验证
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        // 查询用户
        User user = findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }

        // 验证密码
        if (!verifyPassword(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 缓存用户信息
        redisUtils.set(USER_CACHE_PREFIX + user.getId(), user, USER_CACHE_TTL);

        return user;
    }

    @Override
    public User findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return userMapper.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        return userMapper.findByEmail(email);
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        
        // 先从缓存中获取
        String cacheKey = USER_CACHE_PREFIX + userId;
        User cachedUser = (User) redisUtils.get(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }
        
        // 从数据库获取
        User user = getById(userId);
        if (user != null) {
            // 缓存用户信息
            redisUtils.set(cacheKey, user, USER_CACHE_TTL);
        }
        
        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return userMapper.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return userMapper.existsByEmail(email);
    }

    @Override
    public boolean updateUserInfo(Long userId, String nickname, String email, String avatarUrl) {
        if (userId == null) {
            return false;
        }

        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查邮箱是否被其他用户使用
        if (StringUtils.hasText(email) && !email.equals(user.getEmail())) {
            User existUser = findByEmail(email);
            if (existUser != null && !existUser.getId().equals(userId)) {
                throw new RuntimeException("邮箱已被其他用户使用");
            }
        }

        // 更新用户信息
        if (StringUtils.hasText(nickname)) {
            user.setNickname(nickname);
        }
        if (StringUtils.hasText(email)) {
            user.setEmail(email);
        }
        if (StringUtils.hasText(avatarUrl)) {
            user.setAvatarUrl(avatarUrl);
        }

        boolean result = updateById(user);
        if (result) {
            // 更新缓存
            redisUtils.set(USER_CACHE_PREFIX + userId, user, USER_CACHE_TTL);
        }
        return result;
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null || !StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            return false;
        }

        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!verifyPassword(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        // 更新密码
        user.setPassword(encodePassword(newPassword));
        boolean result = updateById(user);
        if (result) {
            // 清除缓存，强制重新登录
            redisUtils.del(USER_CACHE_PREFIX + userId);
        }
        return result;
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        if (userId == null || status == null) {
            return false;
        }

        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setStatus(status);
        boolean result = updateById(user);
        if (result) {
            // 更新缓存
            redisUtils.set(USER_CACHE_PREFIX + userId, user, USER_CACHE_TTL);
        }
        return result;
    }

    @Override
    public Integer getUserStatus(Long userId) {
        if (userId == null) {
            return null;
        }
        return userMapper.getUserStatus(userId);
    }

    @Override
    public boolean isUserEnabled(Long userId) {
        Integer status = getUserStatus(userId);
        return status != null && status == 1;
    }

    @Override
    public IPage<User> getUserList(Page<User> page) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<User> searchUsers(Page<User> page, String keyword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                    .like("username", keyword)
                    .or()
                    .like("nickname", keyword)
                    .or()
                    .like("email", keyword)
            );
        }
        queryWrapper.orderByDesc("create_time");
        return page(page, queryWrapper);
    }

    @Override
    public Object getUserStatistics(Long userId) {
        if (userId == null) {
            return null;
        }

        User user = getById(userId);
        if (user == null) {
            return null;
        }

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("user", user);
        statistics.put("musicCount", musicMapper.countByUserId(userId));
        
        return statistics;
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (userId == null) {
            return false;
        }

        boolean result = removeById(userId);
        if (result) {
            // 清除缓存
            redisUtils.del(USER_CACHE_PREFIX + userId);
        }
        return result;
    }

    @Override
    public boolean deleteUsers(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }

        boolean result = removeByIds(userIds);
        if (result) {
            // 清除缓存
            for (Long userId : userIds) {
                redisUtils.del(USER_CACHE_PREFIX + userId);
            }
        }
        return result;
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        if (userId == null || !StringUtils.hasText(newPassword)) {
            return false;
        }

        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setPassword(encodePassword(newPassword));
        boolean result = updateById(user);
        if (result) {
            // 清除缓存，强制重新登录
            redisUtils.del(USER_CACHE_PREFIX + userId);
        }
        return result;
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        if (!StringUtils.hasText(rawPassword) || !StringUtils.hasText(encodedPassword)) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encodePassword(String password) {
        if (!StringUtils.hasText(password)) {
            return null;
        }
        return passwordEncoder.encode(password);
    }

    @Override
    public UserDetailVO getUserDetail(Long userId) {
        if (userId == null) {
            return null;
        }

        // 获取用户基本信息
        User user = getById(userId);
        if (user == null) {
            return null;
        }

        // 构建用户详细信息VO
        UserDetailVO userDetailVO = new UserDetailVO();
        userDetailVO.setId(user.getId());
        userDetailVO.setUsername(user.getUsername());
        userDetailVO.setNickname(user.getNickname());
        userDetailVO.setAvatar(user.getAvatarUrl()); // 映射avatarUrl到avatar
        userDetailVO.setEmail(user.getEmail());
        userDetailVO.setStatus(user.getStatus());
        userDetailVO.setCreateTime(user.getCreateTime());
        userDetailVO.setUpdateTime(user.getUpdateTime());
        
        // 设置默认值
        userDetailVO.setBio(null); // 暂时没有bio字段
        userDetailVO.setIsVerified(false); // 暂时没有认证功能
        
        // 获取关注和粉丝统计信息
        userDetailVO.setFollowingCount(userFollowService.getFollowingCount(userId));
        userDetailVO.setFollowerCount(userFollowService.getFollowerCount(userId));
        
        // 获取歌单数量
        QueryWrapper<com.nyx.musicPlatform.entity.Playlist> playlistWrapper = new QueryWrapper<>();
        playlistWrapper.eq("user_id", userId);
        playlistWrapper.eq("is_deleted", 0);
        long playlistCount = playlistService.count(playlistWrapper);
        userDetailVO.setPlaylistCount(playlistCount);
        
        // 获取收藏数量
        QueryWrapper<com.nyx.musicPlatform.entity.MusicFavorite> favoriteWrapper = new QueryWrapper<>();
        favoriteWrapper.eq("user_id", userId);
        long favoriteCount = musicFavoriteService.count(favoriteWrapper);
        userDetailVO.setFavoriteCount(favoriteCount);
        
        return userDetailVO;
    }

    @Override
    public boolean followUser(Long followerId, Long followingId) {
        return userFollowService.followUser(followerId, followingId);
    }

    @Override
    public boolean unfollowUser(Long followerId, Long followingId) {
        return userFollowService.unfollowUser(followerId, followingId);
    }

    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        return userFollowService.isFollowing(followerId, followingId);
    }

    @Override
    public List<UserDetailVO> getFollowingList(Long userId) {
        return userFollowService.getFollowingList(userId);
    }

    @Override
    public List<UserDetailVO> getFollowersList(Long userId) {
        return userFollowService.getFollowersList(userId);
    }
}