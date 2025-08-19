package com.nyx.musicPlatform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.utils.JwtUtils;
import com.nyx.musicPlatform.dto.PlaylistDTO;
import com.nyx.musicPlatform.entity.Playlist;
import com.nyx.musicPlatform.service.PlaylistService;
import com.nyx.musicPlatform.service.MusicFavoriteService;
import com.nyx.musicPlatform.vo.MusicVO;
import com.nyx.musicPlatform.vo.PlaylistVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 歌单控制器
 *
 * @author nyx
 * @since 2025-06
 */
@Tag(name = "歌单管理", description = "歌单管理相关接口")
@Slf4j
@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private MusicFavoriteService musicFavoriteService;

    /**
     * 创建歌单
     */
    @Operation(summary = "创建歌单", description = "创建新的歌单")
    @PostMapping
    public Result<PlaylistVO> createPlaylist(
            @Valid @RequestBody PlaylistDTO playlistDTO,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            PlaylistVO playlist = playlistService.createPlaylist(playlistDTO, userId);
            return Result.success(playlist);
        } catch (Exception e) {
            log.error("创建歌单失败", e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 更新歌单
     */
    @Operation(summary = "更新歌单", description = "更新指定歌单信息")
    @PutMapping("/{id}")
    public Result<PlaylistVO> updatePlaylist(
            @Parameter(description = "歌单ID") @PathVariable Long id,
            @Valid @RequestBody PlaylistDTO playlistDTO,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            PlaylistVO playlist = playlistService.updatePlaylist(id, playlistDTO, userId);
            return Result.success(playlist);
        } catch (Exception e) {
            log.error("更新歌单失败，ID: {}", id, e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 删除歌单
     */
    @Operation(summary = "删除歌单", description = "删除指定歌单")
    @DeleteMapping("/{id}")
    public Result<Boolean> deletePlaylist(
            @Parameter(description = "歌单ID") @PathVariable Long id,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            boolean deleted = playlistService.deletePlaylist(id, userId);
            return Result.success(deleted);
        } catch (Exception e) {
            log.error("删除歌单失败，ID: {}", id, e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 分页查询歌单
     */
    @Operation(summary = "分页查询歌单", description = "分页查询歌单列表")
    @GetMapping
    public Result<IPage<PlaylistVO>> getPlaylistPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "是否公开") @RequestParam(required = false) Boolean isPublic,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            HttpServletRequest request) {
        try {
            Long currentUserId = getCurrentUserIdOptional(request);
            Page<PlaylistVO> page = new Page<>(current, size);
            IPage<PlaylistVO> result = playlistService.getPlaylistPage(page, userId, isPublic, keyword, currentUserId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("分页查询歌单失败", e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 根据ID查询歌单详情
     */
    @Operation(summary = "查询歌单详情", description = "根据ID查询歌单详情")
    @GetMapping("/{id}")
    public Result<PlaylistVO> getPlaylistById(
            @Parameter(description = "歌单ID") @PathVariable Long id,
            HttpServletRequest request) {
        try {
            Long currentUserId = getCurrentUserIdOptional(request);
            PlaylistVO playlist = playlistService.getPlaylistById(id, currentUserId);
            return Result.success(playlist);
        } catch (Exception e) {
            log.error("查询歌单详情失败，ID: {}", id, e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 查询用户歌单列表
     */
    @Operation(summary = "查询用户歌单列表", description = "查询指定用户的歌单列表")
    @GetMapping("/user/{userId}")
    public Result<List<PlaylistVO>> getUserPlaylists(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            HttpServletRequest request) {
        log.info("=== 开始获取用户播放列表 ===");
        log.info("请求用户ID: {}", userId);
        
        try {
            Long currentUserId = getCurrentUserIdOptional(request);
            log.info("当前用户ID: {}", currentUserId);
            
            List<PlaylistVO> playlists = playlistService.getUserPlaylists(userId, currentUserId);
            log.info("获取到播放列表数量: {}", playlists != null ? playlists.size() : 0);
            log.info("播放列表详情: {}", playlists);
            
            Result<List<PlaylistVO>> result = Result.success(playlists);
            log.info("返回结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("查询用户歌单列表失败，用户ID: {}", userId, e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 查询当前用户歌单列表
     */
    @Operation(summary = "查询当前用户歌单列表", description = "查询当前登录用户的歌单列表")
    @GetMapping("/my")
    public Result<List<PlaylistVO>> getMyPlaylists(HttpServletRequest request) {
        try {
            Long currentUserId = getCurrentUserId(request);
            List<PlaylistVO> playlists = playlistService.getUserPlaylists(currentUserId, currentUserId);
            return Result.success(playlists);
        } catch (Exception e) {
            log.error("查询当前用户歌单列表失败", e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 添加音乐到歌单
     */
    @Operation(summary = "添加音乐到歌单", description = "将音乐添加到指定歌单")
    @PostMapping("/{playlistId}/music/{musicId}")
    public Result<Boolean> addMusicToPlaylist(
            @Parameter(description = "歌单ID") @PathVariable Long playlistId,
            @Parameter(description = "音乐ID") @PathVariable Long musicId,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            boolean added = playlistService.addMusicToPlaylist(playlistId, musicId, userId);
            return Result.success(added);
        } catch (Exception e) {
            log.error("添加音乐到歌单失败，歌单ID: {}, 音乐ID: {}", playlistId, musicId, e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 从歌单移除音乐
     */
    @Operation(summary = "从歌单移除音乐", description = "从指定歌单中移除音乐")
    @DeleteMapping("/{playlistId}/music/{musicId}")
    public Result<Boolean> removeMusicFromPlaylist(
            @Parameter(description = "歌单ID") @PathVariable Long playlistId,
            @Parameter(description = "音乐ID") @PathVariable Long musicId,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            boolean removed = playlistService.removeMusicFromPlaylist(playlistId, musicId, userId);
            return Result.success(removed);
        } catch (Exception e) {
            log.error("从歌单中移除音乐失败，歌单ID: {}, 音乐ID: {}", playlistId, musicId, e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 查询歌单音乐列表
     */
    @Operation(summary = "查询歌单音乐列表", description = "查询指定歌单中的音乐列表")
    @GetMapping("/{playlistId}/music")
    public Result<List<MusicVO>> getPlaylistMusic(
            @Parameter(description = "歌单ID") @PathVariable Long playlistId,
            HttpServletRequest request) {
        try {
            Long currentUserId = getCurrentUserIdOptional(request);
            
            // 如果是收藏的音乐歌单（ID为-1），返回用户收藏的音乐列表
            if (playlistId == -1) {
                if (currentUserId == null) {
                    throw new RuntimeException("请先登录");
                }
                Result<List<MusicVO>> favoriteMusicResult = musicFavoriteService.getFavoriteList(currentUserId);
                if (favoriteMusicResult.isSuccess()) {
                    return Result.success(favoriteMusicResult.getData());
                } else {
                    return Result.failed(favoriteMusicResult.getMessage());
                }
            }
            
            List<MusicVO> musics = playlistService.getPlaylistMusics(playlistId, currentUserId);
            return Result.success(musics);
        } catch (Exception e) {
            log.error("查询歌单音乐列表失败，歌单ID: {}", playlistId, e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 增加歌单播放次数
     */
    @Operation(summary = "增加歌单播放次数", description = "增加指定歌单的播放次数")
    @PostMapping("/{playlistId}/play")
    public Result<Void> incrementPlayCount(
            @Parameter(description = "歌单ID") @PathVariable Long playlistId) {
        try {
            playlistService.incrementPlayCount(playlistId);
            return Result.success();
        } catch (Exception e) {
            log.error("增加歌单播放次数失败，歌单ID: {}", playlistId, e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 更新歌单音乐排序
     */
    @Operation(summary = "更新歌单音乐排序", description = "更新歌单中音乐的排序")
    @PutMapping("/{playlistId}/music/{musicId}/order")
    public Result<Boolean> updateMusicOrder(
            @Parameter(description = "歌单ID") @PathVariable Long playlistId,
            @Parameter(description = "音乐ID") @PathVariable Long musicId,
            @Parameter(description = "排序号") @RequestParam Integer sortOrder,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            boolean updated = playlistService.updateMusicOrder(playlistId, musicId, sortOrder, userId);
            return Result.success(updated);
        } catch (Exception e) {
            log.error("更新歌单音乐排序失败，歌单ID: {}, 音乐ID: {}", playlistId, musicId, e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 获取当前用户ID（必须登录）
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("请先登录");
        }
        
        token = token.substring(7);
        try {
            return jwtUtils.getUserIdFromToken(token);
        } catch (Exception e) {
            throw new RuntimeException("登录已过期，请重新登录");
        }
    }

    /**
     * 获取当前用户ID（可选登录）
     */
    private Long getCurrentUserIdOptional(HttpServletRequest request) {
        try {
            return getCurrentUserId(request);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 收藏歌单
     */
    @Operation(summary = "收藏歌单", description = "收藏指定歌单")
    @PostMapping("/{playlistId}/favorite")
    public Result<Void> favoritePlaylist(
            @Parameter(description = "歌单ID") @PathVariable Long playlistId,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            return musicFavoriteService.addPlaylistFavorite(userId, playlistId);
        } catch (Exception e) {
            log.error("收藏歌单失败，歌单ID: {}", playlistId, e);
            return Result.failed(e.getMessage());
        }
    }
    
    /**
     * 取消收藏歌单
     */
    @Operation(summary = "取消收藏歌单", description = "取消收藏指定歌单")
    @DeleteMapping("/{playlistId}/favorite")
    public Result<Void> unfavoritePlaylist(
            @Parameter(description = "歌单ID") @PathVariable Long playlistId,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            return musicFavoriteService.removePlaylistFavorite(userId, playlistId);
        } catch (Exception e) {
            log.error("取消收藏歌单失败，歌单ID: {}", playlistId, e);
            return Result.failed(e.getMessage());
        }
    }
    
    /**
     * 检查歌单收藏状态
     */
    @Operation(summary = "检查歌单收藏状态", description = "检查是否已收藏指定歌单")
    @GetMapping("/{playlistId}/favorite/status")
    public Result<Boolean> checkPlaylistFavoriteStatus(
            @Parameter(description = "歌单ID") @PathVariable Long playlistId,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            return musicFavoriteService.isPlaylistFavorite(userId, playlistId);
        } catch (Exception e) {
            log.error("检查歌单收藏状态失败，歌单ID: {}", playlistId, e);
            return Result.failed(e.getMessage());
        }
    }
    
    /**
     * 获取收藏歌单列表
     */
    @Operation(summary = "获取收藏歌单列表", description = "获取当前用户收藏的歌单列表")
    @GetMapping("/favorites")
    public Result<List<Playlist>> getFavoritePlaylistList(HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            return musicFavoriteService.getFavoritePlaylistList(userId);
        } catch (Exception e) {
            log.error("获取收藏歌单列表失败", e);
            return Result.failed(e.getMessage());
        }
    }
}