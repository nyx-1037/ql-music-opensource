package com.nyx.musicPlatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nyx.musicPlatform.common.exception.BusinessException;
import com.nyx.musicPlatform.entity.CommentLike;
import com.nyx.musicPlatform.entity.MusicComment;
import com.nyx.musicPlatform.mapper.CommentLikeMapper;
import com.nyx.musicPlatform.mapper.MusicCommentMapper;
import java.util.Date;
import com.nyx.musicPlatform.service.MusicCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 音乐评论服务实现类
 * 
 * @author nyx
 * @since 2025-06
 */
@Slf4j
@Service
@Transactional
public class MusicCommentServiceImpl extends ServiceImpl<MusicCommentMapper, MusicComment> implements MusicCommentService {

    @Autowired
    private MusicCommentMapper musicCommentMapper;

    @Autowired
    private CommentLikeMapper commentLikeMapper;

    @Override
    public MusicComment addComment(Long userId, Long musicId, String content, Long parentId) {
        // 验证参数
        if (userId == null || musicId == null || !StringUtils.hasText(content)) {
            throw new BusinessException("参数不能为空");
        }

        if (content.length() > 500) {
            throw new BusinessException("评论内容不能超过500字符");
        }

        try {
            // 创建评论对象
            MusicComment comment = new MusicComment();
            comment.setUserId(userId);
            comment.setMusicId(musicId);
            comment.setContent(content.trim());
            comment.setParentId(parentId);
            comment.setLikeCount(0);
            comment.setStatus(1);
            comment.setCreateTime(LocalDateTime.now());
            comment.setUpdateTime(LocalDateTime.now());

            // 保存到数据库
            save(comment);

            log.info("用户 {} 对音乐 {} 添加评论成功", userId, musicId);
            return comment;
        } catch (Exception e) {
            log.error("添加评论失败", e);
            throw new BusinessException("添加评论失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<MusicComment> getCommentsByMusicId(Page<MusicComment> page, Long musicId, String sortType) {
        if (musicId == null) {
            throw new BusinessException("音乐ID不能为空");
        }
        
        // 根据排序类型选择不同的查询方法
        if ("hot".equals(sortType)) {
            return musicCommentMapper.findByMusicIdOrderByLikes(page, musicId);
        } else {
            // 默认按时间排序（最新）
            return musicCommentMapper.findByMusicId(page, musicId);
        }
    }

    /**
     * 获取音乐评论（包含用户点赞状态）
     * @param page 分页对象
     * @param musicId 音乐ID
     * @param userId 用户ID（可为空）
     * @param sortType 排序类型（latest: 最新, hot: 最热）
     * @return 评论分页数据
     */
    @Override
    public IPage<MusicComment> getCommentsByMusicIdWithUserLike(Page<MusicComment> page, Long musicId, Long userId, String sortType) {
        if (musicId == null) {
            throw new BusinessException("音乐ID不能为空");
        }

        try {
            IPage<MusicComment> result;
            
            // 根据排序类型选择不同的查询方法
            if ("hot".equals(sortType)) {
                result = musicCommentMapper.findByMusicIdOrderByLikes(page, musicId);
            } else {
                // 默认按时间排序（最新）
                result = musicCommentMapper.findByMusicId(page, musicId);
            }
            
            // 如果用户已登录，设置点赞状态
            if (userId != null && result.getRecords() != null) {
                for (MusicComment comment : result.getRecords()) {
                    boolean isLiked = commentLikeMapper.existsByUserIdAndCommentId(userId, comment.getId());
                    comment.setIsLiked(isLiked);
                }
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取音乐评论失败: musicId={}, userId={}, sortType={}", musicId, userId, sortType, e);
            throw new BusinessException("获取评论失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<MusicComment> getCommentsByUserId(Page<MusicComment> page, Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        return musicCommentMapper.findByUserId(page, userId);
    }

    @Override
    public List<MusicComment> getRepliesByParentId(Long parentId) {
        if (parentId == null) {
            throw new BusinessException("父评论ID不能为空");
        }
        return musicCommentMapper.findRepliesByParentId(parentId);
    }

    @Override
    public boolean deleteComment(Long commentId, Long userId) {
        if (commentId == null || userId == null) {
            throw new BusinessException("参数不能为空");
        }

        try {
            // 查询评论是否存在且属于该用户
            MusicComment comment = getById(commentId);
            if (comment == null) {
                throw new BusinessException("评论不存在");
            }

            if (!comment.getUserId().equals(userId)) {
                throw new BusinessException("无权限删除该评论");
            }

            // 软删除评论
            int result = musicCommentMapper.softDeleteById(commentId);
            
            log.info("用户 {} 删除评论 {} 成功", userId, commentId);
            return result > 0;
        } catch (Exception e) {
            log.error("删除评论失败", e);
            throw new BusinessException("删除评论失败: " + e.getMessage());
        }
    }

    @Override
    public boolean likeComment(Long commentId, Long userId) {
        try {
            // 检查是否已经点赞
            if (commentLikeMapper.existsByUserIdAndCommentId(userId, commentId)) {
                return false; // 已经点赞过了
            }
            
            // 创建点赞记录
            CommentLike commentLike = new CommentLike();
            commentLike.setUserId(userId);
            commentLike.setCommentId(commentId);
            commentLike.setCreateTime(LocalDateTime.now());
            
            int result = commentLikeMapper.insert(commentLike);
            if (result > 0) {
                // 增加评论点赞数
            musicCommentMapper.incrementLikeCount(commentId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("点赞评论失败: commentId={}, userId={}", commentId, userId, e);
            return false;
        }
    }

    @Override
    public boolean unlikeComment(Long commentId, Long userId) {
        try {
            // 检查是否已经点赞
            if (!commentLikeMapper.existsByUserIdAndCommentId(userId, commentId)) {
                return false; // 没有点赞过
            }
            
            // 删除点赞记录
            int result = commentLikeMapper.deleteByUserIdAndCommentId(userId, commentId);
            if (result > 0) {
                // 减少评论点赞数
            musicCommentMapper.decrementLikeCount(commentId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("取消点赞评论失败: commentId={}, userId={}", commentId, userId, e);
            return false;
        }
    }

    @Override
    public long getCommentCountByMusicId(Long musicId) {
        if (musicId == null) {
            return 0;
        }
        return musicCommentMapper.countByMusicId(musicId);
    }

    @Override
    public long getCommentCountByUserId(Long userId) {
        if (userId == null) {
            return 0;
        }
        return musicCommentMapper.countByUserId(userId);
    }

    @Override
    public List<MusicComment> getLatestComments(int limit) {
        if (limit <= 0) {
            limit = 10;
        }
        return musicCommentMapper.findLatestComments(limit);
    }

    @Override
    public List<MusicComment> getHotCommentsByMusicId(Long musicId, int limit) {
        if (musicId == null) {
            throw new BusinessException("音乐ID不能为空");
        }
        if (limit <= 0) {
            limit = 10;
        }
        return musicCommentMapper.findHotCommentsByMusicId(musicId, limit);
    }

    @Override
    public long getTotalCommentCount() {
        return musicCommentMapper.countAll();
    }

    @Override
    public boolean hasUserCommented(Long musicId, Long userId) {
        if (musicId == null || userId == null) {
            return false;
        }
        return musicCommentMapper.existsByMusicIdAndUserId(musicId, userId);
    }

    @Override
    public boolean updateComment(Long commentId, String content, Long userId) {
        if (commentId == null || !StringUtils.hasText(content) || userId == null) {
            throw new BusinessException("参数不能为空");
        }

        if (content.length() > 500) {
            throw new BusinessException("评论内容不能超过500字符");
        }

        try {
            // 查询评论是否存在且属于该用户
            MusicComment comment = getById(commentId);
            if (comment == null) {
                throw new BusinessException("评论不存在");
            }

            if (!comment.getUserId().equals(userId)) {
                throw new BusinessException("无权限修改该评论");
            }

            // 更新评论内容
            comment.setContent(content.trim());
            comment.setUpdateTime(LocalDateTime.now());
            boolean result = updateById(comment);
            
            log.info("用户 {} 更新评论 {} 成功", userId, commentId);
            return result;
        } catch (Exception e) {
            log.error("更新评论失败", e);
            throw new BusinessException("更新评论失败: " + e.getMessage());
        }
    }

    @Override
    public MusicComment getCommentById(Long commentId) {
        if (commentId == null) {
            throw new BusinessException("评论ID不能为空");
        }
        return getById(commentId);
    }

    @Override
    public boolean deleteCommentsBatch(List<Long> commentIds, Long userId) {
        if (commentIds == null || commentIds.isEmpty() || userId == null) {
            throw new BusinessException("参数不能为空");
        }

        try {
            int successCount = 0;
            for (Long commentId : commentIds) {
                if (deleteComment(commentId, userId)) {
                    successCount++;
                }
            }
            
            log.info("用户 {} 批量删除评论，成功删除 {} 条", userId, successCount);
            return successCount > 0;
        } catch (Exception e) {
            log.error("批量删除评论失败", e);
            throw new BusinessException("批量删除评论失败: " + e.getMessage());
        }
    }
}