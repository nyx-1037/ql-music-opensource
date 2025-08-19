package com.nyx.musicPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.entity.Music;
import com.nyx.musicPlatform.entity.MusicFavorite;
import com.nyx.musicPlatform.entity.Playlist;
import com.nyx.musicPlatform.vo.MusicVO;

import java.util.List;

/**
 * 音乐收藏服务接口
 *
 * @author nyx
 * @since 2025-06-01
 */
public interface MusicFavoriteService extends IService<MusicFavorite> {

    /**
     * 添加收藏
     *
     * @param userId 用户ID
     * @param musicId 音乐ID
     * @return 操作结果
     */
    Result<Void> addFavorite(Long userId, Long musicId);

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param musicId 音乐ID
     * @return 操作结果
     */
    Result<Void> removeFavorite(Long userId, Long musicId);

    /**
     * 检查是否已收藏
     *
     * @param userId 用户ID
     * @param musicId 音乐ID
     * @return 是否已收藏
     */
    Result<Boolean> isFavorite(Long userId, Long musicId);

    /**
     * 获取用户收藏的音乐列表
     *
     * @param userId 用户ID
     * @return 音乐列表
     */
    Result<List<MusicVO>> getFavoriteList(Long userId);

    /**
     * 获取用户收藏歌单列表
     *
     * @param userId 用户ID
     * @return 歌单列表
     */
    Result<List<Playlist>> getFavoritePlaylistList(Long userId);

    /**
     * 添加歌单收藏
     *
     * @param userId 用户ID
     * @param playlistId 歌单ID
     * @return 操作结果
     */
    Result<Void> addPlaylistFavorite(Long userId, Long playlistId);

    /**
     * 取消歌单收藏
     *
     * @param userId 用户ID
     * @param playlistId 歌单ID
     * @return 操作结果
     */
    Result<Void> removePlaylistFavorite(Long userId, Long playlistId);

    /**
     * 检查是否已收藏歌单
     *
     * @param userId 用户ID
     * @param playlistId 歌单ID
     * @return 是否已收藏
     */
    Result<Boolean> isPlaylistFavorite(Long userId, Long playlistId);

}