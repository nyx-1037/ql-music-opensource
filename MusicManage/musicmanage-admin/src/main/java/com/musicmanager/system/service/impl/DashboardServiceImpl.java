package com.musicmanager.system.service.impl;

import com.musicmanager.system.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 数据可视化服务实现类
 * 
 * @author musicmanager
 * @date 2024-12-27
 */
@Service
public class DashboardServiceImpl implements IDashboardService
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取用户统计数据
     */
    @Override
    public Map<String, Object> getUserStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        // 总用户数
        String totalUsersSql = "SELECT COUNT(*) FROM ql_user";
        Integer totalUsers = jdbcTemplate.queryForObject(totalUsersSql, Integer.class);
        result.put("totalUsers", totalUsers);
        
        // 活跃用户数（有播放记录的用户）
        String activeUsersSql = "SELECT COUNT(DISTINCT user_id) FROM ql_music_play_history";
        Integer activeUsers = jdbcTemplate.queryForObject(activeUsersSql, Integer.class);
        result.put("activeUsers", activeUsers);
        
        // 用户注册趋势（最近30天）
        String userTrendSql = "SELECT DATE(create_time) as date, COUNT(*) as count " +
                             "FROM ql_user " +
                             "WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                             "GROUP BY DATE(create_time) " +
                             "ORDER BY date";
        List<Map<String, Object>> userTrend = jdbcTemplate.queryForList(userTrendSql);
        result.put("userTrend", userTrend);
        
        return result;
    }

    /**
     * 获取音乐统计数据
     */
    @Override
    public Map<String, Object> getMusicStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        // 总音乐数
        String totalMusicSql = "SELECT COUNT(*) FROM ql_music";
        Integer totalMusic = jdbcTemplate.queryForObject(totalMusicSql, Integer.class);
        result.put("totalMusic", totalMusic);
        
        // 公开音乐数
        String publicMusicSql = "SELECT COUNT(*) FROM ql_music WHERE is_public = 1";
        Integer publicMusic = jdbcTemplate.queryForObject(publicMusicSql, Integer.class);
        result.put("publicMusic", publicMusic);
        
        // 音乐分类分布（基于genre字段）
        String categoryDistributionSql = "SELECT genre as name, COUNT(*) as count " +
                                        "FROM ql_music " +
                                        "WHERE genre IS NOT NULL AND genre != '' " +
                                        "GROUP BY genre " +
                                        "ORDER BY count DESC";
        List<Map<String, Object>> categoryDistribution = jdbcTemplate.queryForList(categoryDistributionSql);
        result.put("categoryDistribution", categoryDistribution);
        
        // 音乐上传趋势（最近30天）
        String musicTrendSql = "SELECT DATE(create_time) as date, COUNT(*) as count " +
                              "FROM ql_music " +
                              "WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                              "GROUP BY DATE(create_time) " +
                              "ORDER BY date";
        List<Map<String, Object>> musicTrend = jdbcTemplate.queryForList(musicTrendSql);
        result.put("musicTrend", musicTrend);
        
        return result;
    }

    /**
     * 获取播放历史统计数据
     */
    @Override
    public Map<String, Object> getPlayStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        // 总播放次数
        String totalPlaysSql = "SELECT COUNT(*) FROM ql_music_play_history";
        Integer totalPlays = jdbcTemplate.queryForObject(totalPlaysSql, Integer.class);
        result.put("totalPlays", totalPlays);
        
        // 今日播放次数
        String todayPlaysSql = "SELECT COUNT(*) FROM ql_music_play_history WHERE DATE(play_time) = CURDATE()";
        Integer todayPlays = jdbcTemplate.queryForObject(todayPlaysSql, Integer.class);
        result.put("todayPlays", todayPlays);
        
        // 播放趋势（最近30天）
        String playTrendSql = "SELECT DATE(play_time) as date, COUNT(*) as count " +
                             "FROM ql_music_play_history " +
                             "WHERE play_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                             "GROUP BY DATE(play_time) " +
                             "ORDER BY date";
        List<Map<String, Object>> playTrend = jdbcTemplate.queryForList(playTrendSql);
        result.put("playTrend", playTrend);
        
        // 热门音乐（播放次数最多的前10首）
        String hotMusicSql = "SELECT m.title, m.artist, COUNT(mph.id) as play_count " +
                            "FROM ql_music m " +
                            "LEFT JOIN ql_music_play_history mph ON m.id = mph.music_id " +
                            "GROUP BY m.id, m.title, m.artist " +
                            "ORDER BY play_count DESC " +
                            "LIMIT 10";
        List<Map<String, Object>> hotMusic = jdbcTemplate.queryForList(hotMusicSql);
        result.put("hotMusic", hotMusic);
        
        return result;
    }

    /**
     * 获取收藏统计数据
     */
    @Override
    public Map<String, Object> getFavoriteStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        // 总收藏数
        String totalFavoritesSql = "SELECT COUNT(*) FROM ql_music_favorite";
        Integer totalFavorites = jdbcTemplate.queryForObject(totalFavoritesSql, Integer.class);
        result.put("totalFavorites", totalFavorites);
        
        // 收藏趋势（最近30天）
        String favoriteTrendSql = "SELECT DATE(create_time) as date, COUNT(*) as count " +
                                 "FROM ql_music_favorite " +
                                 "WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                                 "GROUP BY DATE(create_time) " +
                                 "ORDER BY date";
        List<Map<String, Object>> favoriteTrend = jdbcTemplate.queryForList(favoriteTrendSql);
        result.put("favoriteTrend", favoriteTrend);
        
        // 最受欢迎音乐（收藏次数最多的前10首）
        String popularMusicSql = "SELECT m.title, m.artist, COUNT(mf.id) as favorite_count " +
                                "FROM ql_music m " +
                                "LEFT JOIN ql_music_favorite mf ON m.id = mf.music_id " +
                                "GROUP BY m.id, m.title, m.artist " +
                                "ORDER BY favorite_count DESC " +
                                "LIMIT 10";
        List<Map<String, Object>> popularMusic = jdbcTemplate.queryForList(popularMusicSql);
        result.put("popularMusic", popularMusic);
        
        return result;
    }

    /**
     * 获取歌单统计数据
     */
    @Override
    public Map<String, Object> getPlaylistStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        // 总歌单数
        String totalPlaylistsSql = "SELECT COUNT(*) FROM ql_playlist";
        Integer totalPlaylists = jdbcTemplate.queryForObject(totalPlaylistsSql, Integer.class);
        result.put("totalPlaylists", totalPlaylists);
        
        // 公开歌单数
        String publicPlaylistsSql = "SELECT COUNT(*) FROM ql_playlist WHERE is_public = 1";
        Integer publicPlaylists = jdbcTemplate.queryForObject(publicPlaylistsSql, Integer.class);
        result.put("publicPlaylists", publicPlaylists);
        
        // 歌单创建趋势（最近30天）
        String playlistTrendSql = "SELECT DATE(create_time) as date, COUNT(*) as count " +
                                 "FROM ql_playlist " +
                                 "WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                                 "GROUP BY DATE(create_time) " +
                                 "ORDER BY date";
        List<Map<String, Object>> playlistTrend = jdbcTemplate.queryForList(playlistTrendSql);
        result.put("playlistTrend", playlistTrend);
        
        // 热门歌单（包含音乐最多的前10个）
        String hotPlaylistsSql = "SELECT p.name, p.description, COUNT(pm.music_id) as music_count " +
                                "FROM ql_playlist p " +
                                "LEFT JOIN ql_playlist_music pm ON p.id = pm.playlist_id " +
                                "WHERE p.is_public = 1 " +
                                "GROUP BY p.id, p.name, p.description " +
                                "ORDER BY music_count DESC " +
                                "LIMIT 10";
        List<Map<String, Object>> hotPlaylists = jdbcTemplate.queryForList(hotPlaylistsSql);
        result.put("hotPlaylists", hotPlaylists);
        
        return result;
    }

    /**
     * 获取分类统计数据
     */
    @Override
    public Map<String, Object> getCategoryStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        // 总分类数
        String totalCategoriesSql = "SELECT COUNT(*) FROM ql_music_category";
        Integer totalCategories = jdbcTemplate.queryForObject(totalCategoriesSql, Integer.class);
        result.put("totalCategories", totalCategories);
        
        // 分类音乐数量分布
        String categoryMusicCountSql = "SELECT c.name, COUNT(mcr.music_id) as music_count " +
                                      "FROM ql_music_category c " +
                                      "LEFT JOIN ql_music_category_relation mcr ON c.id = mcr.category_id " +
                                      "GROUP BY c.id, c.name " +
                                      "ORDER BY music_count DESC";
        List<Map<String, Object>> categoryMusicCount = jdbcTemplate.queryForList(categoryMusicCountSql);
        result.put("categoryMusicCount", categoryMusicCount);
        
        return result;
    }

    /**
     * 获取关注统计数据
     */
    @Override
    public Map<String, Object> getFollowStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        // 总关注数
        String totalFollowsSql = "SELECT COUNT(*) FROM ql_user_follow";
        Integer totalFollows = jdbcTemplate.queryForObject(totalFollowsSql, Integer.class);
        result.put("totalFollows", totalFollows);
        
        // 关注趋势（最近30天）
        String followTrendSql = "SELECT DATE(created_at) as date, COUNT(*) as count " +
                               "FROM ql_user_follow " +
                               "WHERE created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                               "GROUP BY DATE(created_at) " +
                               "ORDER BY date";
        List<Map<String, Object>> followTrend = jdbcTemplate.queryForList(followTrendSql);
        result.put("followTrend", followTrend);
        
        // 最受关注用户（被关注最多的前10个用户）
        String popularUsersSql = "SELECT u.username, u.nickname, COUNT(uf.id) as follower_count " +
                                "FROM ql_user u " +
                                "LEFT JOIN ql_user_follow uf ON u.id = uf.following_id " +
                                "GROUP BY u.id, u.username, u.nickname " +
                                "ORDER BY follower_count DESC " +
                                "LIMIT 10";
        List<Map<String, Object>> popularUsers = jdbcTemplate.queryForList(popularUsersSql);
        result.put("popularUsers", popularUsers);
        
        return result;
    }

    /**
     * 获取综合统计数据
     */
    @Override
    public Map<String, Object> getOverviewStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        // 基础统计数据
        result.put("totalUsers", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ql_user", Integer.class));
        result.put("totalMusic", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ql_music", Integer.class));
        result.put("totalPlaylists", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ql_playlist", Integer.class));
        result.put("totalPlays", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ql_music_play_history", Integer.class));
        result.put("totalFavorites", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ql_music_favorite", Integer.class));
        result.put("totalFollows", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ql_user_follow", Integer.class));
        
        // 今日数据
        result.put("todayUsers", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ql_user WHERE DATE(create_time) = CURDATE()", Integer.class));
        result.put("todayMusic", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ql_music WHERE DATE(create_time) = CURDATE()", Integer.class));
        result.put("todayPlays", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ql_music_play_history WHERE DATE(play_time) = CURDATE()", Integer.class));
        
        return result;
    }
}