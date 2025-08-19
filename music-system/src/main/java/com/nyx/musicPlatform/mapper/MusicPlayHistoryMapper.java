package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nyx.musicPlatform.entity.MusicPlayHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 音乐播放历史 Mapper 接口
 *
 * @author nyx
 * @since 2025-06-01
 */
@Mapper
public interface MusicPlayHistoryMapper extends BaseMapper<MusicPlayHistory> {

    /**
     * 根据用户ID获取播放历史列表
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 播放历史列表
     */
    List<MusicPlayHistory> getPlayHistoryByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 获取用户最近播放的音乐ID列表
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 音乐ID列表
     */
    List<Long> getRecentPlayMusicIds(@Param("userId") Long userId, @Param("limit") Integer limit);

}