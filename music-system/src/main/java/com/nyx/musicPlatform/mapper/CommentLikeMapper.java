package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nyx.musicPlatform.entity.CommentLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评论点赞Mapper接口
 */
@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {
    
    /**
     * 检查用户是否已点赞评论
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 是否已点赞
     */
    Boolean existsByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);
    
    /**
     * 删除用户对评论的点赞
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 删除的记录数
     */
    int deleteByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);
    
    /**
     * 获取评论的点赞数
     * @param commentId 评论ID
     * @return 点赞数
     */
    Long countByCommentId(@Param("commentId") Long commentId);
}