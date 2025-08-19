package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nyx.musicPlatform.entity.UserFollow;
import com.nyx.musicPlatform.vo.UserDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户关注关系Mapper接口
 * 
 * @author nyx
 * @since 2025-06
 */
@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {

    /**
     * 检查用户是否关注了另一个用户
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否关注
     */
    @Select("SELECT COUNT(*) > 0 FROM ql_user_follow WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    boolean isFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    /**
     * 获取用户的关注列表
     * 
     * @param userId 用户ID
     * @return 关注的用户列表
     */
    @Select("SELECT u.id, u.username, u.nickname, u.avatar_url as avatar, u.bio " +
            "FROM ql_user u INNER JOIN ql_user_follow uf ON u.id = uf.following_id " +
            "WHERE uf.follower_id = #{userId} ORDER BY uf.created_at DESC")
    List<UserDetailVO> getFollowingList(@Param("userId") Long userId);

    /**
     * 获取用户的粉丝列表
     * 
     * @param userId 用户ID
     * @return 粉丝用户列表
     */
    @Select("SELECT u.id, u.username, u.nickname, u.avatar_url as avatar, u.bio " +
            "FROM ql_user u INNER JOIN ql_user_follow uf ON u.id = uf.follower_id " +
            "WHERE uf.following_id = #{userId} ORDER BY uf.created_at DESC")
    List<UserDetailVO> getFollowersList(@Param("userId") Long userId);

    /**
     * 获取用户的关注数量
     * 
     * @param userId 用户ID
     * @return 关注数量
     */
    @Select("SELECT COUNT(*) FROM ql_user_follow WHERE follower_id = #{userId}")
    Long getFollowingCount(@Param("userId") Long userId);

    /**
     * 获取用户的粉丝数量
     * 
     * @param userId 用户ID
     * @return 粉丝数量
     */
    @Select("SELECT COUNT(*) FROM ql_user_follow WHERE following_id = #{userId}")
    Long getFollowerCount(@Param("userId") Long userId);
}