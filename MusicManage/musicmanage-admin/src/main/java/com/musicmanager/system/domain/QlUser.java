package com.musicmanager.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.musicmanager.common.annotation.Excel;
import com.musicmanager.common.core.domain.BaseEntity;

/**
 * 用户对象 ql_user
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public class QlUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 密码（加密） */
    @Excel(name = "密码", readConverterExp = "加=密")
    private String password;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickname;

    /** 头像URL */
    @Excel(name = "头像URL")
    private String avatarUrl;

    /** 账号状态（0-禁用，1-启用） */
    @Excel(name = "账号状态", readConverterExp = "0=-禁用，1-启用")
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

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setNickname(String nickname) 
    {
        this.nickname = nickname;
    }

    public String getNickname() 
    {
        return nickname;
    }

    public void setAvatarUrl(String avatarUrl) 
    {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() 
    {
        return avatarUrl;
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
            .append("username", getUsername())
            .append("password", getPassword())
            .append("email", getEmail())
            .append("nickname", getNickname())
            .append("avatarUrl", getAvatarUrl())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDeleted", getIsDeleted())
            .toString();
    }
}
