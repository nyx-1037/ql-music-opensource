package com.musicmanager.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.musicmanager.common.annotation.Excel;
import com.musicmanager.common.core.domain.BaseEntity;

/**
 * 音乐收藏对象 ql_music_favorite
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public class QlMusicFavorite extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 音乐ID */
    @Excel(name = "音乐ID")
    private Long musicId;

    /** 歌单ID */
    @Excel(name = "歌单ID")
    private Long playlistId;

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

    public void setPlaylistId(Long playlistId) 
    {
        this.playlistId = playlistId;
    }

    public Long getPlaylistId() 
    {
        return playlistId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("musicId", getMusicId())
            .append("playlistId", getPlaylistId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
