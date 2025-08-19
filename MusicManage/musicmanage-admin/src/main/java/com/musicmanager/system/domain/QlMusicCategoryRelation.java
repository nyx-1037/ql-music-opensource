package com.musicmanager.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.musicmanager.common.annotation.Excel;
import com.musicmanager.common.core.domain.BaseEntity;

/**
 * 音乐分类关联对象 ql_music_category_relation
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public class QlMusicCategoryRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联ID */
    private Long id;

    /** 音乐ID */
    @Excel(name = "音乐ID")
    private Long musicId;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setMusicId(Long musicId) 
    {
        this.musicId = musicId;
    }

    public Long getMusicId() 
    {
        return musicId;
    }

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("musicId", getMusicId())
            .append("categoryId", getCategoryId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
