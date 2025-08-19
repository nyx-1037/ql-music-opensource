package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nyx.musicPlatform.entity.MusicComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 音乐评论Mapper接口
 * 
 * @author nyx
 * @since 2025-06
 */
@Mapper
public interface MusicCommentMapper extends BaseMapper<MusicComment> {

    /**
     * 根据音乐ID分页查询评论列表
     * @param page 分页对象
     * @param musicId 音乐ID
     * @return 评论分页列表
     */
    @Select("SELECT c.*, u.username, u.nickname, u.avatar_url as avatar " +
            "FROM ql_music_comment c " +
            "LEFT JOIN ql_user u ON c.user_id = u.id " +
            "WHERE c.music_id = #{musicId} AND c.status = 1 " +
            "ORDER BY c.create_time DESC")
    IPage<MusicComment> findByMusicId(Page<MusicComment> page, @Param("musicId") Long musicId);

    /**
     * 根据音乐ID分页查询评论列表（按点赞数排序）
     * @param page 分页对象
     * @param musicId 音乐ID
     * @return 评论分页列表
     */
    @Select("SELECT c.*, u.username, u.nickname, u.avatar_url as avatar " +
            "FROM ql_music_comment c " +
            "LEFT JOIN ql_user u ON c.user_id = u.id " +
            "WHERE c.music_id = #{musicId} AND c.status = 1 " +
            "ORDER BY c.like_count DESC, c.create_time DESC")
    IPage<MusicComment> findByMusicIdOrderByLikes(Page<MusicComment> page, @Param("musicId") Long musicId);

    /**
     * 根据用户ID分页查询评论列表
     * @param page 分页对象
     * @param userId 用户ID
     * @return 评论分页列表
     */
    @Select("SELECT * FROM ql_music_comment WHERE user_id = #{userId} AND status = 1 ORDER BY create_time DESC")
    IPage<MusicComment> findByUserId(Page<MusicComment> page, @Param("userId") Long userId);

    /**
     * 根据父评论ID查询回复列表
     * @param parentId 父评论ID
     * @return 回复列表
     */
    @Select("SELECT * FROM ql_music_comment WHERE parent_id = #{parentId} AND status = 1 ORDER BY create_time ASC")
    List<MusicComment> findRepliesByParentId(@Param("parentId") Long parentId);

    /**
     * 获取音乐的评论总数
     * @param musicId 音乐ID
     * @return 评论总数
     */
    @Select("SELECT COUNT(*) FROM ql_music_comment WHERE music_id = #{musicId} AND status = 1")
    long countByMusicId(@Param("musicId") Long musicId);

    /**
     * 获取用户的评论总数
     * @param userId 用户ID
     * @return 评论总数
     */
    @Select("SELECT COUNT(*) FROM ql_music_comment WHERE user_id = #{userId} AND status = 1")
    long countByUserId(@Param("userId") Long userId);

    /**
     * 增加评论点赞数
     * @param commentId 评论ID
     * @return 更新行数
     */
    @Update("UPDATE ql_music_comment SET like_count = like_count + 1 WHERE id = #{commentId} AND status = 1")
    int incrementLikeCount(@Param("commentId") Long commentId);

    /**
     * 减少评论点赞数
     * @param commentId 评论ID
     * @return 更新行数
     */
    @Update("UPDATE ql_music_comment SET like_count = like_count - 1 WHERE id = #{commentId} AND status = 1 AND like_count > 0")
    int decrementLikeCount(@Param("commentId") Long commentId);

    /**
     * 获取最新评论列表
     * @param limit 限制数量
     * @return 最新评论列表
     */
    @Select("SELECT * FROM ql_music_comment WHERE status = 1 ORDER BY create_time DESC LIMIT #{limit}")
    List<MusicComment> findLatestComments(@Param("limit") int limit);

    /**
     * 获取热门评论列表（按点赞数排序）
     * @param musicId 音乐ID
     * @param limit 限制数量
     * @return 热门评论列表
     */
    @Select("SELECT * FROM ql_music_comment WHERE music_id = #{musicId} AND status = 1 ORDER BY like_count DESC, create_time DESC LIMIT #{limit}")
    List<MusicComment> findHotCommentsByMusicId(@Param("musicId") Long musicId, @Param("limit") int limit);

    /**
     * 获取系统评论总数
     * @return 评论总数
     */
    @Select("SELECT COUNT(*) FROM ql_music_comment WHERE status = 1")
    long countAll();

    /**
     * 根据音乐ID和用户ID查询用户是否已评论
     * @param musicId 音乐ID
     * @param userId 用户ID
     * @return 是否已评论
     */
    @Select("SELECT COUNT(*) > 0 FROM ql_music_comment WHERE music_id = #{musicId} AND user_id = #{userId} AND status = 1")
    boolean existsByMusicIdAndUserId(@Param("musicId") Long musicId, @Param("userId") Long userId);

    /**
     * 软删除评论
     * @param commentId 评论ID
     * @return 更新行数
     */
    @Update("UPDATE ql_music_comment SET status = 0 WHERE id = #{commentId}")
    int softDeleteById(@Param("commentId") Long commentId);
}