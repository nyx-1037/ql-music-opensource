package com.musicmanager.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.musicmanager.common.annotation.Excel;
import com.musicmanager.common.core.domain.BaseEntity;

/**
 * 用户关注关系对象 ql_user_follow
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public class QlUserFollow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 关注者ID */
    @Excel(name = "关注者ID")
    private Long followerId;

    /** 被关注者ID */
    @Excel(name = "被关注者ID")
    private Long followingId;

    /** 关注时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "关注时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setFollowerId(Long followerId) 
    {
        this.followerId = followerId;
    }

    public Long getFollowerId() 
    {
        return followerId;
    }

    public void setFollowingId(Long followingId) 
    {
        this.followingId = followingId;
    }

    public Long getFollowingId() 
    {
        return followingId;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("followerId", getFollowerId())
            .append("followingId", getFollowingId())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
