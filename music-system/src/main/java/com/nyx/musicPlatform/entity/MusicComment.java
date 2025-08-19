package com.nyx.musicPlatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 音乐评论实体类
 * 
 * @author nyx
 * @since 2025-06
 */
@Schema(description = "音乐评论实体")
@TableName("ql_music_comment")
public class MusicComment {

    @Schema(description = "评论ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "音乐ID")
    @TableField("music_id")
    private Long musicId;

    @Schema(description = "评论内容")
    @TableField("content")
    private String content;

    @Schema(description = "父评论ID，用于回复功能")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "点赞数")
    @TableField("like_count")
    private Integer likeCount;

    @Schema(description = "状态：0-删除，1-正常")
    @TableField("status")
    private Integer status;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "当前用户是否已点赞")
    @TableField(exist = false)
    private Boolean isLiked;

    @Schema(description = "用户名")
    @TableField(exist = false)
    private String username;

    @Schema(description = "用户昵称")
    @TableField(exist = false)
    private String nickname;

    @Schema(description = "用户头像")
    @TableField(exist = false)
    private String avatar;

    // 构造函数
    public MusicComment() {}

    public MusicComment(Long userId, Long musicId, String content, Long parentId) {
        this.userId = userId;
        this.musicId = musicId;
        this.content = content;
        this.parentId = parentId;
        this.likeCount = 0;
        this.status = 1; // 默认正常状态
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMusicId() {
        return musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "MusicComment{" +
                "id=" + id +
                ", userId=" + userId +
                ", musicId=" + musicId +
                ", content='" + content + '\'' +
                ", parentId=" + parentId +
                ", likeCount=" + likeCount +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isLiked=" + isLiked +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}