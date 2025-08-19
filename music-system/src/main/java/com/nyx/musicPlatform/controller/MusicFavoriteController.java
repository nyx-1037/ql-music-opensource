package com.nyx.musicPlatform.controller;

import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.service.MusicFavoriteService;
import com.nyx.musicPlatform.common.utils.JwtUtils;
import com.nyx.musicPlatform.vo.MusicVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 音乐收藏控制器
 *
 * @author nyx
 * @since 2025-06-01
 */
@RestController
@RequestMapping("/api/music/favorite")
@Tag(name = "音乐收藏管理", description = "音乐收藏管理相关接口")
public class MusicFavoriteController {

    @Autowired
    private MusicFavoriteService musicFavoriteService;
    
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/add/{musicId}")
    @Operation(summary = "添加收藏", description = "添加收藏接口")
    public Result<Void> addFavorite(
            @Parameter(description = "音乐ID") @PathVariable Long musicId,
            HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long userId = jwtUtils.getUserIdFromToken(token);
        return musicFavoriteService.addFavorite(userId, musicId);
    }

    @DeleteMapping("/remove/{musicId}")
    @Operation(summary = "取消收藏", description = "取消收藏接口")
    public Result<Void> removeFavorite(
            @Parameter(description = "音乐ID") @PathVariable Long musicId,
            HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long userId = jwtUtils.getUserIdFromToken(token);
        return musicFavoriteService.removeFavorite(userId, musicId);
    }

    @GetMapping("/check/{musicId}")
    @Operation(summary = "检查是否已收藏", description = "检查是否已收藏接口")
    public Result<Boolean> isFavorite(
            @Parameter(description = "音乐ID") @PathVariable Long musicId,
            HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long userId = jwtUtils.getUserIdFromToken(token);
        return musicFavoriteService.isFavorite(userId, musicId);
    }

    @GetMapping("/list")
    @Operation(summary = "获取收藏列表", description = "获取收藏列表接口")
    public Result<List<MusicVO>> getFavoriteList(HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long userId = jwtUtils.getUserIdFromToken(token);
        return musicFavoriteService.getFavoriteList(userId);
    }

}