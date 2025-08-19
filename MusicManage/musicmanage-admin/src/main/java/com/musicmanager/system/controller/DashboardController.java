package com.musicmanager.system.controller;

import com.musicmanager.common.annotation.Log;
import com.musicmanager.common.core.controller.BaseController;
import com.musicmanager.common.core.domain.AjaxResult;
import com.musicmanager.common.enums.BusinessType;
import com.musicmanager.system.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 数据可视化控制器
 * 
 * @author musicmanager
 * @date 2024-12-27
 */
@RestController
@RequestMapping("/system/dashboard")
public class DashboardController extends BaseController
{
    @Autowired
    private IDashboardService dashboardService;

    /**
     * 获取用户统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:list')")
    @GetMapping("/userStats")
    public AjaxResult getUserStats()
    {
        Map<String, Object> stats = dashboardService.getUserStats();
        return AjaxResult.success(stats);
    }

    /**
     * 获取音乐统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:list')")
    @GetMapping("/musicStats")
    public AjaxResult getMusicStats()
    {
        Map<String, Object> stats = dashboardService.getMusicStats();
        return AjaxResult.success(stats);
    }

    /**
     * 获取播放历史统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:list')")
    @GetMapping("/playStats")
    public AjaxResult getPlayStats()
    {
        Map<String, Object> stats = dashboardService.getPlayStats();
        return AjaxResult.success(stats);
    }

    /**
     * 获取收藏统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:list')")
    @GetMapping("/favoriteStats")
    public AjaxResult getFavoriteStats()
    {
        Map<String, Object> stats = dashboardService.getFavoriteStats();
        return AjaxResult.success(stats);
    }

    /**
     * 获取歌单统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:list')")
    @GetMapping("/playlistStats")
    public AjaxResult getPlaylistStats()
    {
        Map<String, Object> stats = dashboardService.getPlaylistStats();
        return AjaxResult.success(stats);
    }

    /**
     * 获取分类统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:list')")
    @GetMapping("/categoryStats")
    public AjaxResult getCategoryStats()
    {
        Map<String, Object> stats = dashboardService.getCategoryStats();
        return AjaxResult.success(stats);
    }

    /**
     * 获取关注统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:list')")
    @GetMapping("/followStats")
    public AjaxResult getFollowStats()
    {
        Map<String, Object> stats = dashboardService.getFollowStats();
        return AjaxResult.success(stats);
    }

    /**
     * 获取综合统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:list')")
    @GetMapping("/overviewStats")
    public AjaxResult getOverviewStats()
    {
        Map<String, Object> stats = dashboardService.getOverviewStats();
        return AjaxResult.success(stats);
    }

    /**
     * 获取概览数据（前端兼容接口）
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:list')")
    @GetMapping("/overview")
    public AjaxResult getOverview()
    {
        Map<String, Object> stats = dashboardService.getOverviewStats();
        return AjaxResult.success(stats);
    }
}