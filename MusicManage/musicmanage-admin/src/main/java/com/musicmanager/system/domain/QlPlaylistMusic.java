package com.musicmanager.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.musicmanager.common.annotation.Excel;
import com.musicmanager.common.core.domain.BaseEntity;

/**
 * 歌单音乐关联对象 ql_playlist_music
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public class QlPlaylistMusic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联ID */
    private Long id;

    /** 歌单ID */
    @Excel(name = "歌单ID")
    private Long playlistId;

    /** 音乐ID */
    @Excel(name = "音乐ID")
    private Long musicId;

    /** 排序顺序 */
    @Excel(name = "排序顺序")
    private Long sortOrder;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPlaylistId(Long playlistId) 
    {
        this.playlistId = playlistId;
    }

    public Long getPlaylistId() 
    {
        return playlistId;
    }

    public void setMusicId(Long musicId) 
    {
        this.musicId = musicId;
    }

    public Long getMusicId() 
    {
        return musicId;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("playlistId", getPlaylistId())
            .append("musicId", getMusicId())
            .append("sortOrder", getSortOrder())
            .append("createTime", getCreateTime())
            .toString();
    }
}
