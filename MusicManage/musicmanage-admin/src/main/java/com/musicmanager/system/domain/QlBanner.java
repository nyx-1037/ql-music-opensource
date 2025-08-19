package com.musicmanager.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.musicmanager.common.annotation.Excel;
import com.musicmanager.common.core.domain.BaseEntity;

/**
 * Banner对象 ql_banner
 * 
 * @author musicmanager
 * @date 2024-01-01
 */
public class QlBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Banner ID */
    private Long id;

    /** Banner标题 */
    @Excel(name = "Banner标题")
    private String title;

    /** Banner描述 */
    @Excel(name = "Banner描述")
    private String description;

    /** 图片URL */
    @Excel(name = "图片URL")
    private String imageUrl;

    /** 按钮文本 */
    @Excel(name = "按钮文本")
    private String buttonText;

    /** 动作类型（0链接 1弹窗 2下载） */
    @Excel(name = "动作类型", readConverterExp = "0=链接,1=弹窗,2=下载")
    private String actionType;

    /** 动作URL */
    @Excel(name = "动作URL")
    private String actionUrl;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }
    public void setButtonText(String buttonText) 
    {
        this.buttonText = buttonText;
    }

    public String getButtonText() 
    {
        return buttonText;
    }
    public void setActionType(String actionType) 
    {
        this.actionType = actionType;
    }

    public String getActionType() 
    {
        return actionType;
    }
    public void setActionUrl(String actionUrl) 
    {
        this.actionUrl = actionUrl;
    }

    public String getActionUrl() 
    {
        return actionUrl;
    }
    public void setSortOrder(Integer sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder() 
    {
        return sortOrder;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("description", getDescription())
            .append("imageUrl", getImageUrl())
            .append("buttonText", getButtonText())
            .append("actionType", getActionType())
            .append("actionUrl", getActionUrl())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}