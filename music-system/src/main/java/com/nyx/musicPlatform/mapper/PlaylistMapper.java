package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nyx.musicPlatform.entity.Playlist;
import com.nyx.musicPlatform.vo.PlaylistVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 歌单Mapper接口
 *
 * @author nyx
 * @since 2025-06
 */
@Mapper
public interface PlaylistMapper extends BaseMapper<Playlist> {

    /**
 * 分页查询歌单详情
 *
 * @param page 分页参数
 * @param userId 用户ID（可选，用于查询指定用户的歌单）
 * @param isPublic 是否公开（可选）
 * @param keyword 关键词（可选，用于搜索歌单名称）
 * @return 歌单详情分页结果
 */
    IPage<PlaylistVO> selectPlaylistPage(
            Page<PlaylistVO> page,
            @Param("userId") Long userId,
            @Param("isPublic") Boolean isPublic,
            @Param("keyword") String keyword,
            @Param("currentUserId") Long currentUserId
    );

    /**
 * 根据ID查询歌单详情
 *
 * @param id 歌单ID
 * @param currentUserId 当前用户ID
 * @return 歌单详情
 */
    PlaylistVO selectPlaylistById(@Param("id") Long id, @Param("currentUserId") Long currentUserId);

    /**
 * 查询用户的歌单列表
 *
 * @param userId 用户ID
 * @param currentUserId 当前用户ID
 * @return 歌单列表
 */
    List<PlaylistVO> selectUserPlaylists(@Param("userId") Long userId, @Param("currentUserId") Long currentUserId);

    /**
 * 更新歌单的音乐数量
 *
 * @param playlistId 歌单ID
 */
    void updateMusicCount(@Param("playlistId") Long playlistId);

    /**
 * 增加歌单播放次数
 *
 * @param playlistId 歌单ID
 */
    void incrementPlayCount(@Param("playlistId") Long playlistId);
}