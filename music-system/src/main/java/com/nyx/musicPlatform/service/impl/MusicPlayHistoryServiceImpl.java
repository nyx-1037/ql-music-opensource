package com.nyx.musicPlatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.entity.Music;
import com.nyx.musicPlatform.entity.MusicPlayHistory;
import com.nyx.musicPlatform.mapper.MusicMapper;
import com.nyx.musicPlatform.mapper.MusicPlayHistoryMapper;
import com.nyx.musicPlatform.service.MusicPlayHistoryService;
import com.nyx.musicPlatform.vo.MusicVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐播放历史服务实现类
 *
 * @author nyx
 * @since 2025-06
 */
@Service
public class MusicPlayHistoryServiceImpl extends ServiceImpl<MusicPlayHistoryMapper, MusicPlayHistory> implements MusicPlayHistoryService {

    @Autowired
    private MusicMapper musicMapper;

    @Override
    public Result<Void> recordPlayHistory(Long userId, Long musicId, Integer playDuration, Integer playPosition) {
        // 检查音乐是否存在
        Music music = musicMapper.selectById(musicId);
        if (music == null) {
            return Result.error(ResultCode.MUSIC_NOT_FOUND, "音乐不存在");
        }
        
        // 记录播放历史
        MusicPlayHistory playHistory = new MusicPlayHistory();
        playHistory.setUserId(userId);
        playHistory.setMusicId(musicId);
        playHistory.setPlayTime(LocalDateTime.now());
        playHistory.setPlayDuration(playDuration);
        playHistory.setPlayPosition(playPosition);
        
        boolean success = this.save(playHistory);
        
        // 更新音乐播放次数
        if (success) {
            music.setPlayCount(music.getPlayCount() + 1);
            musicMapper.updateById(music);
        }
        
        return success ? Result.success() : Result.error(ResultCode.OPERATION_NOT_ALLOWED, "记录播放历史失败");
    }

    @Override
    public Result<List<MusicVO>> getPlayHistory(Long userId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 50; // 默认限制50条
        }
        
        List<MusicPlayHistory> historyList = baseMapper.getPlayHistoryByUserId(userId, limit);
        List<MusicVO> musicVOList = new ArrayList<>();
        
        for (MusicPlayHistory history : historyList) {
            Music music = musicMapper.selectById(history.getMusicId());
            if (music != null) {
                MusicVO musicVO = new MusicVO();
                BeanUtils.copyProperties(music, musicVO);
                musicVOList.add(musicVO);
            }
        }
        
        return Result.success(musicVOList);
    }

    @Override
    public Result<Void> clearPlayHistory(Long userId) {
        QueryWrapper<MusicPlayHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        
        boolean success = this.remove(queryWrapper);
        return success ? Result.success() : Result.error(ResultCode.OPERATION_NOT_ALLOWED, "清空播放历史失败");
    }

    @Override
    public Result<List<MusicVO>> getRecentPlayMusic(Long userId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认限制10条
        }
        
        List<Long> musicIds = baseMapper.getRecentPlayMusicIds(userId, limit);
        
        if (musicIds.isEmpty()) {
            return Result.success(new ArrayList<>());
        }
        
        List<Music> musicList = musicMapper.selectBatchIds(musicIds);
        List<MusicVO> musicVOList = new ArrayList<>();
        
        for (Music music : musicList) {
            MusicVO musicVO = new MusicVO();
            BeanUtils.copyProperties(music, musicVO);
            musicVOList.add(musicVO);
        }
        
        return Result.success(musicVOList);
    }

}