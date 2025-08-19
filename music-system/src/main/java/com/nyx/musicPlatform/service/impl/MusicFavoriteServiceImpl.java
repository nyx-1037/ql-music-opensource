package com.nyx.musicPlatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.entity.Music;
import com.nyx.musicPlatform.entity.MusicFavorite;
import com.nyx.musicPlatform.entity.Playlist;
import com.nyx.musicPlatform.mapper.MusicFavoriteMapper;
import com.nyx.musicPlatform.mapper.MusicMapper;
import com.nyx.musicPlatform.mapper.PlaylistMapper;
import com.nyx.musicPlatform.service.MusicFavoriteService;
import com.nyx.musicPlatform.vo.MusicVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 音乐收藏服务实现类
 *
 * @author nyx
 * @since 2025-06-01
 */
@Slf4j
@Service
public class MusicFavoriteServiceImpl extends ServiceImpl<MusicFavoriteMapper, MusicFavorite> implements MusicFavoriteService {

    @Autowired
    private MusicMapper musicMapper;
    
    @Autowired
    private PlaylistMapper playlistMapper;

    @Override
    public Result<Void> addFavorite(Long userId, Long musicId) {
        // 检查是否已收藏
        QueryWrapper<MusicFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("music_id", musicId);
        MusicFavorite existing = this.getOne(queryWrapper);
        
        if (existing != null) {
            return Result.error(ResultCode.OPERATION_NOT_ALLOWED, "已经收藏过该音乐");
        }
        
        // 检查音乐是否存在
        Music music = musicMapper.selectById(musicId);
        if (music == null) {
            return Result.error(ResultCode.MUSIC_NOT_FOUND, "音乐不存在");
        }
        
        // 添加收藏
        MusicFavorite favorite = new MusicFavorite();
        favorite.setUserId(userId);
        favorite.setMusicId(musicId);
        favorite.setCreateTime(LocalDateTime.now());
        
        boolean success = this.save(favorite);
        return success ? Result.success() : Result.error(ResultCode.MUSIC_UPDATE_FAILED, "收藏失败");
    }

    @Override
    public Result<Void> removeFavorite(Long userId, Long musicId) {
        QueryWrapper<MusicFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("music_id", musicId);
        
        boolean success = this.remove(queryWrapper);
        return success ? Result.success() : Result.error(ResultCode.MUSIC_UPDATE_FAILED, "取消收藏失败");
    }

    @Override
    public Result<Boolean> isFavorite(Long userId, Long musicId) {
        QueryWrapper<MusicFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("music_id", musicId);
        
        long count = this.count(queryWrapper);
        return Result.success(count > 0);
    }

    @Override
    public Result<List<MusicVO>> getFavoriteList(Long userId) {
        List<Long> musicIds = baseMapper.getFavoriteMusicIdsByUserId(userId);
        
        if (musicIds.isEmpty()) {
            return Result.success(new ArrayList<>());
        }
        
        List<Music> musicList = musicMapper.selectBatchIds(musicIds);
        List<MusicVO> musicVOList = new ArrayList<>();
        
        for (Music music : musicList) {
            MusicVO musicVO = new MusicVO();
            BeanUtils.copyProperties(music, musicVO);
            
            // 设置播放URL和下载URL
            if (music.getFilePath() != null && !music.getFilePath().isEmpty()) {
                musicVO.setPlayUrl(music.getFilePath());
                musicVO.setDownloadUrl(music.getFilePath());
            }
            
            // 设置封面URL
            if (music.getCoverUrl() != null && !music.getCoverUrl().isEmpty()) {
                musicVO.setCoverUrl(music.getCoverUrl());
            } else {
                // 如果没有封面URL，设置默认的封面API路径
                musicVO.setCoverUrl("/api/music/cover/" + music.getId());
            }
            
            musicVOList.add(musicVO);
        }
        
        return Result.success(musicVOList);
    }

    @Override
    public Result<Void> addPlaylistFavorite(Long userId, Long playlistId) {
        // 检查歌单是否存在
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            return Result.failed(ResultCode.PLAYLIST_NOT_FOUND);
        }
        
        // 检查是否已经收藏
        QueryWrapper<MusicFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("playlist_id", playlistId);
        MusicFavorite existingFavorite = this.getOne(queryWrapper);
        if (existingFavorite != null) {
            return Result.failed(ResultCode.PLAYLIST_ALREADY_FAVORITED);
        }
        
        // 添加收藏记录
        MusicFavorite favorite = new MusicFavorite();
        favorite.setUserId(userId);
        favorite.setPlaylistId(playlistId);
        favorite.setCreateTime(LocalDateTime.now());
        
        boolean result = this.save(favorite);
        if (result) {
            return Result.success();
        } else {
            return Result.failed(ResultCode.PLAYLIST_FAVORITE_FAILED);
        }
    }

    @Override
    public Result<Void> removePlaylistFavorite(Long userId, Long playlistId) {
        // 检查是否已收藏
        QueryWrapper<MusicFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("playlist_id", playlistId);
        MusicFavorite existingFavorite = this.getOne(queryWrapper);
        if (existingFavorite == null) {
            return Result.failed(ResultCode.PLAYLIST_NOT_FAVORITED);
        }
        
        // 删除收藏记录
        boolean success = this.remove(queryWrapper);
        if (success) {
            return Result.success();
        } else {
            return Result.failed(ResultCode.PLAYLIST_UNFAVORITE_FAILED);
        }
    }

    @Override
    public Result<Boolean> isPlaylistFavorite(Long userId, Long playlistId) {
        QueryWrapper<MusicFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("playlist_id", playlistId);
        MusicFavorite favorite = this.getOne(queryWrapper);
        return Result.success(favorite != null);
    }
    
    @Override
    public Result<List<Playlist>> getFavoritePlaylistList(Long userId) {
        try {
            // 查询用户收藏的歌单ID列表
            QueryWrapper<MusicFavorite> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .isNotNull("playlist_id")
                       .orderByDesc("create_time");
            List<MusicFavorite> favorites = this.list(queryWrapper);
            
            if (favorites.isEmpty()) {
                return Result.success(new ArrayList<>());
            }
            
            // 提取歌单ID列表
            List<Long> playlistIds = favorites.stream()
                    .map(MusicFavorite::getPlaylistId)
                    .collect(Collectors.toList());
            
            // 查询歌单详情
            List<Playlist> playlists = playlistMapper.selectBatchIds(playlistIds);
            
            return Result.success(playlists);
        } catch (Exception e) {
            log.error("获取收藏歌单列表失败，用户ID: {}", userId, e);
            return Result.failed("获取收藏歌单列表失败");
        }
    }

}