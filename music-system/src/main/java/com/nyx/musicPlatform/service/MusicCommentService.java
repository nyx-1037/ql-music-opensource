package com.nyx.musicPlatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nyx.musicPlatform.entity.MusicComment;

import java.util.List;

/**
 * 音乐评论服务接口
 * 
 * @author nyx
 * @since 2025-06
 */
public interface MusicCommentService extends IService<MusicComment> {

    /**
     * 添加评论
     * @param userId 用户ID
     * @param musicId 音乐ID
     * @param content 评论内容
     * @param parentId 父评论ID（可为空）
     * @return 评论对象
     */
    MusicComment addComment(Long userId, Long musicId, String content, Long parentId);

    /**
     * 根据音乐ID分页查询评论列表
     * @param page 分页对象
     * @param musicId 音乐ID
     * @param sortType 排序类型（latest: 最新, hot: 最热）
     * @return 评论分页列表
     */
    IPage<MusicComment> getCommentsByMusicId(Page<MusicComment> page, Long musicId, String sortType);

    /**
     * 根据音乐ID分页查询评论列表（包含用户点赞状态）
     * @param page 分页对象
     * @param musicId 音乐ID
     * @param userId 用户ID
     * @param sortType 排序类型（latest: 最新, hot: 最热）
     * @return 评论分页列表
     */
    IPage<MusicComment> getCommentsByMusicIdWithUserLike(Page<MusicComment> page, Long musicId, Long userId, String sortType);

    /**
     * 根据用户ID分页查询评论列表
     * @param page 分页对象
     * @param userId 用户ID
     * @return 评论分页列表
     */
    IPage<MusicComment> getCommentsByUserId(Page<MusicComment> page, Long userId);

    /**
     * 根据父评论ID查询回复列表
     * @param parentId 父评论ID
     * @return 回复列表
     */
    List<MusicComment> getRepliesByParentId(Long parentId);

    /**
     * 删除评论（软删除）
     * @param commentId 评论ID
     * @param userId 用户ID（权限验证）
     * @return 删除结果
     */
    boolean deleteComment(Long commentId, Long userId);

    /**
     * 点赞评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean likeComment(Long commentId, Long userId);

    /**
     * 取消点赞评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean unlikeComment(Long commentId, Long userId);

    /**
     * 获取音乐的评论总数
     * @param musicId 音乐ID
     * @return 评论总数
     */
    long getCommentCountByMusicId(Long musicId);

    /**
     * 获取用户的评论总数
     * @param userId 用户ID
     * @return 评论总数
     */
    long getCommentCountByUserId(Long userId);

    /**
     * 获取最新评论列表
     * @param limit 限制数量
     * @return 最新评论列表
     */
    List<MusicComment> getLatestComments(int limit);

    /**
     * 获取热门评论列表
     * @param musicId 音乐ID
     * @param limit 限制数量
     * @return 热门评论列表
     */
    List<MusicComment> getHotCommentsByMusicId(Long musicId, int limit);

    /**
     * 获取系统评论总数
     * @return 评论总数
     */
    long getTotalCommentCount();

    /**
     * 检查用户是否已对该音乐评论
     * @param musicId 音乐ID
     * @param userId 用户ID
     * @return 是否已评论
     */
    boolean hasUserCommented(Long musicId, Long userId);

    /**
     * 更新评论内容
     * @param commentId 评论ID
     * @param content 新的评论内容
     * @param userId 用户ID（权限验证）
     * @return 更新结果
     */
    boolean updateComment(Long commentId, String content, Long userId);

    /**
     * 获取评论详情
     * @param commentId 评论ID
     * @return 评论对象
     */
    MusicComment getCommentById(Long commentId);

    /**
     * 批量删除评论
     * @param commentIds 评论ID列表
     * @param userId 用户ID（权限验证）
     * @return 删除结果
     */
    boolean deleteCommentsBatch(List<Long> commentIds, Long userId);
}