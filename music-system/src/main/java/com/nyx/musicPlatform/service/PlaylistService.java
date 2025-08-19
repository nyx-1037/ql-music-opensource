package com.nyx.musicPlatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nyx.musicPlatform.dto.PlaylistDTO;
import com.nyx.musicPlatform.entity.Playlist;
import com.nyx.musicPlatform.vo.MusicVO;
import com.nyx.musicPlatform.vo.PlaylistVO;

import java.util.List;

/**
 * 歌单服务接口
 *
 * @author nyx
 * @since 2025-06
 */
public interface PlaylistService extends IService<Playlist> {

    /**
     * 创建歌单
     *
     * @param playlistDTO 歌单信息
     * @param userId 用户ID
     * @return 创建的歌单
     */
    PlaylistVO createPlaylist(PlaylistDTO playlistDTO, Long userId);

    /**
     * 更新歌单信息
     *
     * @param id 歌单ID
     * @param playlistDTO 歌单信息
     * @param userId 用户ID
     * @return 更新后的歌单
     */
    PlaylistVO updatePlaylist(Long id, PlaylistDTO playlistDTO, Long userId);

    /**
     * 删除歌单
     *
     * @param id 歌单ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deletePlaylist(Long id, Long userId);

    /**
     * 分页查询歌单
     *
     * @param page 分页参数
     * @param userId 用户ID（可选）
     * @param isPublic 是否公开（可选）
     * @param keyword 关键词（可选）
     * @param currentUserId 当前用户ID
     * @return 歌单分页结果
     */
    IPage<PlaylistVO> getPlaylistPage(Page<PlaylistVO> page, Long userId, Boolean isPublic, String keyword, Long currentUserId);

    /**
     * 根据ID查询歌单详情
     *
     * @param id 歌单ID
     * @param currentUserId 当前用户ID
     * @return 歌单详情
     */
    PlaylistVO getPlaylistById(Long id, Long currentUserId);

    /**
     * 查询用户的歌单列表
     *
     * @param userId 用户ID
     * @param currentUserId 当前用户ID
     * @return 歌单列表
     */
    List<PlaylistVO> getUserPlaylists(Long userId, Long currentUserId);

    /**
     * 添加音乐到歌单
     *
     * @param playlistId 歌单ID
     * @param musicId 音乐ID
     * @param userId 用户ID
     * @return 是否添加成功
     */
    boolean addMusicToPlaylist(Long playlistId, Long musicId, Long userId);

    /**
     * 从歌单中移除音乐
     *
     * @param playlistId 歌单ID
     * @param musicId 音乐ID
     * @param userId 用户ID
     * @return 是否移除成功
     */
    boolean removeMusicFromPlaylist(Long playlistId, Long musicId, Long userId);

    /**
     * 获取歌单中的音乐列表
     *
     * @param playlistId 歌单ID
     * @param currentUserId 当前用户ID
     * @return 音乐列表
     */
    List<MusicVO> getPlaylistMusics(Long playlistId, Long currentUserId);

    /**
     * 增加歌单播放次数
     *
     * @param playlistId 歌单ID
     */
    void incrementPlayCount(Long playlistId);

    /**
     * 更新歌单音乐排序
     *
     * @param playlistId 歌单ID
     * @param musicId 音乐ID
     * @param sortOrder 新的排序号
     * @param userId 用户ID
     * @return 是否更新成功
     */
    boolean updateMusicOrder(Long playlistId, Long musicId, Integer sortOrder, Long userId);
}