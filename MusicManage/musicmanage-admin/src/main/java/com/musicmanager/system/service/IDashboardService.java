package com.musicmanager.system.service;

import java.util.Map;

/**
 * 数据可视化服务接口
 * 
 * @author musicmanager
 * @date 2024-12-27
 */
public interface IDashboardService 
{
    /**
     * 获取用户统计数据
     * 
     * @return 用户统计数据
     */
    Map<String, Object> getUserStats();

    /**
     * 获取音乐统计数据
     * 
     * @return 音乐统计数据
     */
    Map<String, Object> getMusicStats();

    /**
     * 获取播放历史统计数据
     * 
     * @return 播放历史统计数据
     */
    Map<String, Object> getPlayStats();

    /**
     * 获取收藏统计数据
     * 
     * @return 收藏统计数据
     */
    Map<String, Object> getFavoriteStats();

    /**
     * 获取歌单统计数据
     * 
     * @return 歌单统计数据
     */
    Map<String, Object> getPlaylistStats();

    /**
     * 获取分类统计数据
     * 
     * @return 分类统计数据
     */
    Map<String, Object> getCategoryStats();

    /**
     * 获取关注统计数据
     * 
     * @return 关注统计数据
     */
    Map<String, Object> getFollowStats();

    /**
     * 获取综合统计数据
     * 
     * @return 综合统计数据
     */
    Map<String, Object> getOverviewStats();
}