package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nyx.musicPlatform.entity.PlaylistMusic;
import com.nyx.musicPlatform.vo.MusicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 歌单音乐关联Mapper接口
 *
 * @author nyx
 * @since 2025-06
 */
@Mapper
public interface PlaylistMusicMapper extends BaseMapper<PlaylistMusic> {

    /**
 * 查询歌单中的音乐列表
 *
 * @param playlistId 歌单ID
 * @return 音乐列表
 */
    List<MusicVO> selectMusicsByPlaylistId(@Param("playlistId") Long playlistId);

    /**
 * 检查音乐是否已在歌单中
 *
 * @param playlistId 歌单ID
 * @param musicId 音乐ID
 * @return 是否存在
 */
    boolean existsByPlaylistIdAndMusicId(@Param("playlistId") Long playlistId, @Param("musicId") Long musicId);

    /**
 * 获取歌单中音乐的最大排序号
 *
 * @param playlistId 歌单ID
 * @return 最大排序号
 */
    Integer getMaxSortOrder(@Param("playlistId") Long playlistId);

    /**
 * 删除歌单中的音乐
 *
 * @param playlistId 歌单ID
 * @param musicId 音乐ID
 * @return 删除的记录数
 */
    int deleteByPlaylistIdAndMusicId(@Param("playlistId") Long playlistId, @Param("musicId") Long musicId);

    /**
 * 更新音乐在歌单中的排序
 *
 * @param playlistId 歌单ID
 * @param musicId 音乐ID
 * @param sortOrder 新的排序号
 * @return 更新的记录数
 */
    int updateSortOrder(@Param("playlistId") Long playlistId, @Param("musicId") Long musicId, @Param("sortOrder") Integer sortOrder);
}