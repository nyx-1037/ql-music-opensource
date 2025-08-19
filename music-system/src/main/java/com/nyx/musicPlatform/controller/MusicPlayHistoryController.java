package com.nyx.musicPlatform.controller;

import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.service.MusicPlayHistoryService;
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
 * 音乐播放历史控制器
 *
 * @author nyx
 * @since 2025-06
 */
@RestController
@RequestMapping("/api/history")
@Tag(name = "播放历史管理", description = "播放历史管理相关接口")
public class MusicPlayHistoryController {

    @Autowired
    private MusicPlayHistoryService musicPlayHistoryService;
    
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/record")
    @Operation(summary = "记录播放历史", description = "记录播放历史接口")
    public Result<Void> recordPlayHistory(
            @Parameter(description = "音乐ID") @RequestParam Long musicId,
            @Parameter(description = "播放时长(秒)") @RequestParam(required = false) Integer playDuration,
            @Parameter(description = "播放位置(秒)") @RequestParam(required = false) Integer playPosition,
            HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long userId = jwtUtils.getUserIdFromToken(token);
        return musicPlayHistoryService.recordPlayHistory(userId, musicId, playDuration, playPosition);
    }

    @GetMapping("/list")
    @Operation(summary = "获取播放历史", description = "获取播放历史接口")
    public Result<List<MusicVO>> getPlayHistory(
            @Parameter(description = "限制数量") @RequestParam(required = false, defaultValue = "50") Integer limit,
            HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long userId = jwtUtils.getUserIdFromToken(token);
        return musicPlayHistoryService.getPlayHistory(userId, limit);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "清空播放历史", description = "清空播放历史接口")
    public Result<Void> clearPlayHistory(HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long userId = jwtUtils.getUserIdFromToken(token);
        return musicPlayHistoryService.clearPlayHistory(userId);
    }

    @GetMapping("/recent")
    @Operation(summary = "获取最近播放", description = "获取最近播放接口")
    public Result<List<MusicVO>> getRecentPlayMusic(
            @Parameter(description = "限制数量") @RequestParam(required = false, defaultValue = "10") Integer limit,
            HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long userId = jwtUtils.getUserIdFromToken(token);
        return musicPlayHistoryService.getRecentPlayMusic(userId, limit);
    }

}