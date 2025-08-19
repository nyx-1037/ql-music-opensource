package com.nyx.musicPlatform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.entity.MusicComment;
import com.nyx.musicPlatform.service.MusicCommentService;
import com.nyx.musicPlatform.common.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 音乐评论控制器
 * 
 * @author nyx
 * @since 2025-06
 */
@Tag(name = "音乐评论管理", description = "音乐评论管理相关接口")
@RestController
@RequestMapping("/api/music/comment")
@Slf4j
public class MusicCommentController {

    @Autowired
    private MusicCommentService musicCommentService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 添加评论
     */
    @Operation(summary = "添加评论", description = "为音乐添加评论")
    @PostMapping("/add")
    public Result addComment(
            @Parameter(description = "音乐ID") @RequestParam Long musicId,
            @Parameter(description = "评论内容") @RequestParam String content,
            @Parameter(description = "父评论ID（回复时使用）") @RequestParam(required = false) Long parentId,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            // 参数验证
            if (musicId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐ID不能为空");
            }

            if (!StringUtils.hasText(content)) {
                return Result.error(ResultCode.PARAM_ERROR, "评论内容不能为空");
            }

            if (content.trim().length() > 500) {
                return Result.error(ResultCode.PARAM_ERROR, "评论内容不能超过500字符");
            }

            MusicComment comment = musicCommentService.addComment(userId, musicId, content.trim(), parentId);
            return Result.success(comment, "评论添加成功");
        } catch (RuntimeException e) {
            return Result.error(ResultCode.OPERATION_NOT_ALLOWED, e.getMessage());
        } catch (Exception e) {
            log.error("添加评论失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取音乐评论列表
     */
    @Operation(summary = "获取音乐评论列表", description = "分页获取指定音乐的评论列表")
    @GetMapping("/list")
    public Result getCommentsByMusicId(
            @Parameter(description = "音乐ID") @RequestParam Long musicId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "排序方式：time-最新，hot-最热") @RequestParam(defaultValue = "time") String sortType,
            HttpServletRequest request) {
        
        try {
            if (musicId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐ID不能为空");
            }

            // 获取当前用户ID（可能为空）
            Long userId = null;
            try {
                String token = jwtUtils.getTokenFromRequest(request);
                if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                    userId = jwtUtils.getUserIdFromToken(token);
                }
            } catch (Exception e) {
                // 忽略token解析错误，允许未登录用户查看评论
                log.debug("解析用户token失败，将以未登录状态获取评论", e);
            }

            Page<MusicComment> pageObj = new Page<>(page, limit);
            IPage<MusicComment> commentPage;
            
            if (userId != null) {
                // 用户已登录，获取包含点赞状态的评论
                commentPage = musicCommentService.getCommentsByMusicIdWithUserLike(pageObj, musicId, userId, sortType);
            } else {
                // 用户未登录，获取普通评论
                commentPage = musicCommentService.getCommentsByMusicId(pageObj, musicId, sortType);
            }
            
            return Result.success(commentPage, "查询成功");
        } catch (Exception e) {
            log.error("获取评论列表失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取用户评论列表
     */
    @Operation(summary = "获取用户评论列表", description = "分页获取指定用户的评论列表")
    @GetMapping("/user")
    public Result getCommentsByUserId(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            Page<MusicComment> pageObj = new Page<>(page, limit);
            IPage<MusicComment> commentPage = musicCommentService.getCommentsByUserId(pageObj, userId);
            
            return Result.success(commentPage, "查询成功");
        } catch (Exception e) {
            log.error("获取用户评论列表失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取评论回复列表
     */
    @Operation(summary = "获取评论回复列表", description = "获取指定评论的回复列表")
    @GetMapping("/replies")
    public Result getRepliesByParentId(
            @Parameter(description = "父评论ID") @RequestParam Long parentId) {
        
        try {
            if (parentId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "父评论ID不能为空");
            }

            List<MusicComment> replies = musicCommentService.getRepliesByParentId(parentId);
            return Result.success(replies, "查询成功");
        } catch (Exception e) {
            log.error("获取回复列表失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 删除评论
     */
    @Operation(summary = "删除评论", description = "删除指定评论")
    @DeleteMapping("/delete/{commentId}")
    public Result deleteComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            if (commentId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "评论ID不能为空");
            }

            boolean result = musicCommentService.deleteComment(commentId, userId);
            if (result) {
                return Result.success(null, "评论删除成功");
            } else {
                return Result.error(ResultCode.OPERATION_NOT_ALLOWED, "评论删除失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.OPERATION_NOT_ALLOWED, e.getMessage());
        } catch (Exception e) {
            log.error("删除评论失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 点赞评论
     */
    @Operation(summary = "点赞评论", description = "为评论点赞")
    @PostMapping("/like/{commentId}")
    public Result likeComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            if (commentId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "评论ID不能为空");
            }

            boolean result = musicCommentService.likeComment(commentId, userId);
            if (result) {
                return Result.success(null, "点赞成功");
            } else {
                return Result.error(ResultCode.OPERATION_NOT_ALLOWED, "点赞失败或已点赞");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.OPERATION_NOT_ALLOWED, e.getMessage());
        } catch (Exception e) {
            log.error("点赞评论失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 取消点赞评论
     */
    @Operation(summary = "取消点赞评论", description = "取消对评论的点赞")
    @PostMapping("/unlike/{commentId}")
    public Result unlikeComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            if (commentId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "评论ID不能为空");
            }

            boolean result = musicCommentService.unlikeComment(commentId, userId);
            if (result) {
                return Result.success(null, "取消点赞成功");
            } else {
                return Result.error(ResultCode.OPERATION_NOT_ALLOWED, "取消点赞失败或未点赞");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.OPERATION_NOT_ALLOWED, e.getMessage());
        } catch (Exception e) {
            log.error("取消点赞评论失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 更新评论内容
     */
    @Operation(summary = "更新评论内容", description = "更新指定评论的内容")
    @PutMapping("/update/{commentId}")
    public Result updateComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            @Parameter(description = "新的评论内容") @RequestParam String content,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            if (commentId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "评论ID不能为空");
            }

            if (!StringUtils.hasText(content)) {
                return Result.error(ResultCode.PARAM_ERROR, "评论内容不能为空");
            }

            if (content.trim().length() > 500) {
                return Result.error(ResultCode.PARAM_ERROR, "评论内容不能超过500字符");
            }

            boolean result = musicCommentService.updateComment(commentId, content.trim(), userId);
            if (result) {
                return Result.success(null, "评论更新成功");
            } else {
                return Result.error(ResultCode.OPERATION_NOT_ALLOWED, "评论更新失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.OPERATION_NOT_ALLOWED, e.getMessage());
        } catch (Exception e) {
            log.error("更新评论失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取评论详情
     */
    @Operation(summary = "获取评论详情", description = "获取指定评论的详细信息")
    @GetMapping("/detail/{commentId}")
    public Result getCommentDetail(
            @Parameter(description = "评论ID") @PathVariable Long commentId) {
        
        try {
            if (commentId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "评论ID不能为空");
            }

            MusicComment comment = musicCommentService.getCommentById(commentId);
            if (comment != null) {
                return Result.success(comment, "查询成功");
            } else {
                return Result.error(ResultCode.NOT_FOUND, "评论不存在");
            }
        } catch (Exception e) {
            log.error("获取评论详情失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取音乐评论统计信息
     */
    @Operation(summary = "获取音乐评论统计信息", description = "获取指定音乐的评论统计信息")
    @GetMapping("/stats/{musicId}")
    public Result getCommentStats(
            @Parameter(description = "音乐ID") @PathVariable Long musicId) {
        
        try {
            if (musicId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐ID不能为空");
            }

            long commentCount = musicCommentService.getCommentCountByMusicId(musicId);
            List<MusicComment> hotComments = musicCommentService.getHotCommentsByMusicId(musicId, 5);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("commentCount", commentCount);
            stats.put("hotComments", hotComments);
            
            return Result.success(stats, "查询成功");
        } catch (Exception e) {
            log.error("获取评论统计信息失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取最新评论列表
     */
    @Operation(summary = "获取最新评论列表", description = "获取系统最新的评论列表")
    @GetMapping("/latest")
    public Result getLatestComments(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<MusicComment> comments = musicCommentService.getLatestComments(limit);
            return Result.success(comments, "查询成功");
        } catch (Exception e) {
            log.error("获取最新评论列表失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }
}