package com.musicmanager.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.musicmanager.common.annotation.Excel;
import com.musicmanager.common.core.domain.BaseEntity;

/**
 * 音乐播放历史对象 ql_music_play_history
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public class QlMusicPlayHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 播放历史ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 音乐ID */
    @Excel(name = "音乐ID")
    private Long musicId;

    /** 播放时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "播放时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date playTime;

    /** 播放时长(秒) */
    @Excel(name = "播放时长(秒)")
    private Long playDuration;

    /** 播放位置(秒) */
    @Excel(name = "播放位置(秒)")
    private Long playPosition;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setMusicId(Long musicId) 
    {
        this.musicId = musicId;
    }

    public Long getMusicId() 
    {
        return musicId;
    }

    public void setPlayTime(Date playTime) 
    {
        this.playTime = playTime;
    }

    public Date getPlayTime() 
    {
        return playTime;
    }

    public void setPlayDuration(Long playDuration) 
    {
        this.playDuration = playDuration;
    }

    public Long getPlayDuration() 
    {
        return playDuration;
    }

    public void setPlayPosition(Long playPosition) 
    {
        this.playPosition = playPosition;
    }

    public Long getPlayPosition() 
    {
        return playPosition;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("musicId", getMusicId())
            .append("playTime", getPlayTime())
            .append("playDuration", getPlayDuration())
            .append("playPosition", getPlayPosition())
            .toString();
    }
}
