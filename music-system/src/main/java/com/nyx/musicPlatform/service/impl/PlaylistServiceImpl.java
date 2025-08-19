package com.nyx.musicPlatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nyx.musicPlatform.dto.PlaylistDTO;
import com.nyx.musicPlatform.entity.Music;
import com.nyx.musicPlatform.entity.MusicFavorite;
import com.nyx.musicPlatform.entity.Playlist;
import com.nyx.musicPlatform.entity.PlaylistMusic;
import com.nyx.musicPlatform.mapper.PlaylistMapper;
import com.nyx.musicPlatform.mapper.PlaylistMusicMapper;
import com.nyx.musicPlatform.service.MusicFavoriteService;
import com.nyx.musicPlatform.service.MusicService;
import com.nyx.musicPlatform.service.PlaylistService;
import com.nyx.musicPlatform.vo.MusicVO;
import com.nyx.musicPlatform.vo.PlaylistVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 歌单服务实现类
 *
 * @author nyx
 * @since 2025-06
 */
@Slf4j
@Service
public class PlaylistServiceImpl extends ServiceImpl<PlaylistMapper, Playlist> implements PlaylistService {

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private PlaylistMusicMapper playlistMusicMapper;

    @Autowired
    private MusicService musicService;
    
    @Autowired
    private MusicFavoriteService musicFavoriteService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlaylistVO createPlaylist(PlaylistDTO playlistDTO, Long userId) {
        log.info("创建歌单，用户ID: {}, 歌单名称: {}", userId, playlistDTO.getName());
        
        // 创建歌单实体
        Playlist playlist = new Playlist();
        BeanUtils.copyProperties(playlistDTO, playlist);
        playlist.setUserId(userId);
        playlist.setPlayCount(0L);
        playlist.setMusicCount(0);
        playlist.setStatus(1);
        playlist.setCreateTime(LocalDateTime.now());
        playlist.setUpdateTime(LocalDateTime.now());
        playlist.setIsDeleted(0);
        
        // 保存歌单
        boolean saved = save(playlist);
        if (!saved) {
            throw new RuntimeException("创建歌单失败");
        }
        
        log.info("歌单创建成功，ID: {}", playlist.getId());
        
        // 返回歌单详情
        return getPlaylistById(playlist.getId(), userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlaylistVO updatePlaylist(Long id, PlaylistDTO playlistDTO, Long userId) {
        log.info("更新歌单，ID: {}, 用户ID: {}", id, userId);
        
        // 检查歌单是否存在且属于当前用户
        Playlist playlist = getById(id);
        if (playlist == null || playlist.getIsDeleted() == 1) {
            throw new RuntimeException("歌单不存在");
        }
        
        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此歌单");
        }
        
        // 更新歌单信息
        BeanUtils.copyProperties(playlistDTO, playlist);
        playlist.setUpdateTime(LocalDateTime.now());
        
        boolean updated = updateById(playlist);
        if (!updated) {
            throw new RuntimeException("更新歌单失败");
        }
        
        log.info("歌单更新成功，ID: {}", id);
        
        // 返回更新后的歌单详情
        return getPlaylistById(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePlaylist(Long id, Long userId) {
        log.info("删除歌单，ID: {}, 用户ID: {}", id, userId);
        
        // 检查歌单是否存在且属于当前用户
        Playlist playlist = getById(id);
        if (playlist == null || playlist.getIsDeleted() == 1) {
            throw new RuntimeException("歌单不存在");
        }
        
        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此歌单");
        }
        
        // 逻辑删除歌单
        playlist.setIsDeleted(1);
        playlist.setUpdateTime(LocalDateTime.now());
        
        boolean deleted = updateById(playlist);
        if (deleted) {
            // 删除歌单中的所有音乐关联
            LambdaQueryWrapper<PlaylistMusic> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PlaylistMusic::getPlaylistId, id);
            playlistMusicMapper.delete(wrapper);
            
            log.info("歌单删除成功，ID: {}", id);
        }
        
        return deleted;
    }

    @Override
    public IPage<PlaylistVO> getPlaylistPage(Page<PlaylistVO> page, Long userId, Boolean isPublic, String keyword, Long currentUserId) {
        log.info("分页查询歌单，用户ID: {}, 是否公开: {}, 关键词: {}, 当前用户ID: {}", userId, isPublic, keyword, currentUserId);
        return playlistMapper.selectPlaylistPage(page, userId, isPublic, keyword, currentUserId);
    }

    @Override
    public PlaylistVO getPlaylistById(Long id, Long currentUserId) {
        log.info("查询歌单详情，ID: {}, 当前用户ID: {}", id, currentUserId);
        
        PlaylistVO playlistVO = playlistMapper.selectPlaylistById(id, currentUserId);
        if (playlistVO == null) {
            throw new RuntimeException("歌单不存在或无权限访问");
        }
        
        // 获取歌单中的音乐列表
        List<MusicVO> musics = getPlaylistMusics(id, currentUserId);
        playlistVO.setMusics(musics);
        
        return playlistVO;
    }

    @Override
    public List<PlaylistVO> getUserPlaylists(Long userId, Long currentUserId) {
        log.info("=== 开始查询用户歌单列表 ===");
        log.info("查询用户歌单列表，用户ID: {}, 当前用户ID: {}", userId, currentUserId);
        
        try {
            // 获取用户创建的歌单
            log.info("调用playlistMapper.selectUserPlaylists，参数: userId={}, currentUserId={}", userId, currentUserId);
            List<PlaylistVO> userPlaylists = playlistMapper.selectUserPlaylists(userId, currentUserId);
            log.info("从数据库获取到的歌单数量: {}", userPlaylists != null ? userPlaylists.size() : 0);
            log.info("用户创建的歌单列表: {}", userPlaylists);
            
            // 如果是查询当前用户自己的歌单，添加"收藏的音乐"虚拟歌单
            if (userId.equals(currentUserId)) {
                log.info("当前用户查询自己的歌单，添加收藏的音乐虚拟歌单");
                PlaylistVO favoritePlaylist = createFavoritePlaylist(userId);
                log.info("创建的收藏歌单: {}", favoritePlaylist);
                userPlaylists.add(0, favoritePlaylist); // 添加到列表开头
                log.info("添加收藏歌单后的总数量: {}", userPlaylists.size());
            }
            
            log.info("最终返回的歌单列表: {}", userPlaylists);
            log.info("=== 用户歌单列表查询完成 ===");
            return userPlaylists;
        } catch (Exception e) {
            log.error("查询用户歌单列表失败，用户ID: {}, 当前用户ID: {}", userId, currentUserId, e);
            throw e;
        }
    }
    
    /**
     * 创建"收藏的音乐"虚拟歌单
     */
    private PlaylistVO createFavoritePlaylist(Long userId) {
        PlaylistVO favoritePlaylist = new PlaylistVO();
        favoritePlaylist.setId(-1L); // 使用特殊ID标识这是虚拟歌单
        favoritePlaylist.setName("收藏的音乐");
        favoritePlaylist.setDescription("我收藏的所有音乐");
        favoritePlaylist.setCoverUrl("/img/favorite-playlist.jpg");
        favoritePlaylist.setUserId(userId);
        favoritePlaylist.setIsPublic(false);
        favoritePlaylist.setPlayCount(0L);
        favoritePlaylist.setIsOwner(true);
        favoritePlaylist.setCreateTime(LocalDateTime.now());
        favoritePlaylist.setUpdateTime(LocalDateTime.now());
        
        // 获取收藏音乐数量
        QueryWrapper<MusicFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).isNotNull("music_id");
        long favoriteCount = musicFavoriteService.count(queryWrapper);
        favoritePlaylist.setMusicCount((int) favoriteCount);
        
        return favoritePlaylist;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMusicToPlaylist(Long playlistId, Long musicId, Long userId) {
        log.info("添加音乐到歌单，歌单ID: {}, 音乐ID: {}, 用户ID: {}", playlistId, musicId, userId);
        
        // 检查歌单是否存在且属于当前用户
        Playlist playlist = getById(playlistId);
        if (playlist == null || playlist.getIsDeleted() == 1) {
            throw new RuntimeException("歌单不存在");
        }
        
        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此歌单");
        }
        
        // 检查音乐是否存在
        Music music = musicService.getById(musicId);
        if (music == null || music.getDeleted() == 1 || music.getStatus() != 1) {
            throw new RuntimeException("音乐不存在或不可用");
        }
        
        // 检查音乐是否已在歌单中
        boolean exists = playlistMusicMapper.existsByPlaylistIdAndMusicId(playlistId, musicId);
        if (exists) {
            throw new RuntimeException("音乐已在歌单中");
        }
        
        // 获取最大排序号
        Integer maxSortOrder = playlistMusicMapper.getMaxSortOrder(playlistId);
        
        // 添加音乐到歌单
        PlaylistMusic playlistMusic = new PlaylistMusic();
        playlistMusic.setPlaylistId(playlistId);
        playlistMusic.setMusicId(musicId);
        playlistMusic.setSortOrder(maxSortOrder + 1);
        playlistMusic.setCreateTime(LocalDateTime.now());
        
        boolean added = playlistMusicMapper.insert(playlistMusic) > 0;
        if (added) {
            // 更新歌单音乐数量
            playlistMapper.updateMusicCount(playlistId);
            log.info("音乐添加到歌单成功，歌单ID: {}, 音乐ID: {}", playlistId, musicId);
        }
        
        return added;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeMusicFromPlaylist(Long playlistId, Long musicId, Long userId) {
        log.info("从歌单中移除音乐，歌单ID: {}, 音乐ID: {}, 用户ID: {}", playlistId, musicId, userId);
        
        // 检查歌单是否存在且属于当前用户
        Playlist playlist = getById(playlistId);
        if (playlist == null || playlist.getIsDeleted() == 1) {
            throw new RuntimeException("歌单不存在");
        }
        
        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此歌单");
        }
        
        // 从歌单中移除音乐
        int removed = playlistMusicMapper.deleteByPlaylistIdAndMusicId(playlistId, musicId);
        if (removed > 0) {
            // 更新歌单音乐数量
            playlistMapper.updateMusicCount(playlistId);
            log.info("音乐从歌单中移除成功，歌单ID: {}, 音乐ID: {}", playlistId, musicId);
        }
        
        return removed > 0;
    }

    @Override
    public List<MusicVO> getPlaylistMusics(Long playlistId, Long currentUserId) {
        log.info("查询歌单音乐列表，歌单ID: {}, 当前用户ID: {}", playlistId, currentUserId);
        
        // 检查歌单访问权限
        PlaylistVO playlist = playlistMapper.selectPlaylistById(playlistId, currentUserId);
        if (playlist == null) {
            throw new RuntimeException("歌单不存在或无权限访问");
        }
        
        return playlistMusicMapper.selectMusicsByPlaylistId(playlistId);
    }

    @Override
    public void incrementPlayCount(Long playlistId) {
        log.info("增加歌单播放次数，歌单ID: {}", playlistId);
        playlistMapper.incrementPlayCount(playlistId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMusicOrder(Long playlistId, Long musicId, Integer sortOrder, Long userId) {
        log.info("更新歌单音乐排序，歌单ID: {}, 音乐ID: {}, 排序号: {}, 用户ID: {}", playlistId, musicId, sortOrder, userId);
        
        // 检查歌单是否存在且属于当前用户
        Playlist playlist = getById(playlistId);
        if (playlist == null || playlist.getIsDeleted() == 1) {
            throw new RuntimeException("歌单不存在");
        }
        
        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此歌单");
        }
        
        // 更新音乐排序
        int updated = playlistMusicMapper.updateSortOrder(playlistId, musicId, sortOrder);
        if (updated > 0) {
            log.info("歌单音乐排序更新成功，歌单ID: {}, 音乐ID: {}", playlistId, musicId);
        }
        
        return updated > 0;
    }
}