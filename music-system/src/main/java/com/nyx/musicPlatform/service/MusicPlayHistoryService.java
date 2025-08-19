package com.nyx.musicPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.entity.MusicPlayHistory;
import com.nyx.musicPlatform.vo.MusicVO;

import java.util.List;

/**
 * 音乐播放历史服务接口
 *
 * @author nyx
 * @since 2025-06-01
 */
public interface MusicPlayHistoryService extends IService<MusicPlayHistory> {

    /**
     * 记录播放历史
     *
     * @param userId 用户ID
     * @param musicId 音乐ID
     * @param playDuration 播放时长(秒)
     * @param playPosition 播放位置(秒)
     * @return 操作结果
     */
    Result<Void> recordPlayHistory(Long userId, Long musicId, Integer playDuration, Integer playPosition);

    /**
     * 获取用户播放历史
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 播放历史列表
     */
    Result<List<MusicVO>> getPlayHistory(Long userId, Integer limit);

    /**
     * 清空用户播放历史
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Void> clearPlayHistory(Long userId);

    /**
     * 获取最近播放的音乐
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 音乐列表
     */
    Result<List<MusicVO>> getRecentPlayMusic(Long userId, Integer limit);

}