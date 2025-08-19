package com.musicmanager.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.musicmanager.common.annotation.Excel;
import com.musicmanager.common.core.domain.BaseEntity;

/**
 * 歌单对象 ql_playlist
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public class QlPlaylist extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 歌单ID */
    private Long id;

    /** 歌单名称 */
    @Excel(name = "歌单名称")
    private String name;

    /** 歌单描述 */
    @Excel(name = "歌单描述")
    private String description;

    /** 封面图片URL */
    @Excel(name = "封面图片URL")
    private String coverUrl;

    /** 创建用户ID */
    @Excel(name = "创建用户ID")
    private Long userId;

    /** 是否公开（0-私有，1-公开） */
    @Excel(name = "是否公开", readConverterExp = "0=-私有，1-公开")
    private Long isPublic;

    /** 播放次数 */
    @Excel(name = "播放次数")
    private Long playCount;

    /** 歌曲数量 */
    @Excel(name = "歌曲数量")
    private Long musicCount;

    /** 状态（0-禁用，1-启用） */
    @Excel(name = "状态", readConverterExp = "0=-禁用，1-启用")
    private Long status;

    /** 逻辑删除标志（0-未删除，1-已删除） */
    @Excel(name = "逻辑删除标志", readConverterExp = "0=-未删除，1-已删除")
    private Long isDeleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setCoverUrl(String coverUrl) 
    {
        this.coverUrl = coverUrl;
    }

    public String getCoverUrl() 
    {
        return coverUrl;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setIsPublic(Long isPublic) 
    {
        this.isPublic = isPublic;
    }

    public Long getIsPublic() 
    {
        return isPublic;
    }

    public void setPlayCount(Long playCount) 
    {
        this.playCount = playCount;
    }

    public Long getPlayCount() 
    {
        return playCount;
    }

    public void setMusicCount(Long musicCount) 
    {
        this.musicCount = musicCount;
    }

    public Long getMusicCount() 
    {
        return musicCount;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setIsDeleted(Long isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() 
    {
        return isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("description", getDescription())
            .append("coverUrl", getCoverUrl())
            .append("userId", getUserId())
            .append("isPublic", getIsPublic())
            .append("playCount", getPlayCount())
            .append("musicCount", getMusicCount())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDeleted", getIsDeleted())
            .toString();
    }
}
