package com.nyx.musicPlatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 音乐实体类
 * 
 * @author nyx
 * @since 2025-06
 */
@Schema(description = "音乐实体")
@TableName("ql_music")
public class Music {

    @Schema(description = "音乐ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "音乐标题")
    @TableField("title")
    private String title;

    @Schema(description = "艺术家")
    @TableField("artist")
    private String artist;

    @Schema(description = "专辑")
    @TableField("album")
    private String album;

    @Schema(description = "音乐时长（秒）")
    @TableField("duration")
    private Integer duration;

    @Schema(description = "文件路径")
    @TableField("file_path")
    private String filePath;

    @Schema(description = "文件名（UUID）")
    @TableField("file_name")
    private String fileName;

    @Schema(description = "原始文件名")
    @TableField("original_file_name")
    private String originalName;

    @Schema(description = "文件大小（字节）")
    @TableField("file_size")
    private Long fileSize;



    @Schema(description = "文件类型")
    @TableField("file_type")
    private String fileType;

    @Schema(description = "上传用户ID")
    @TableField("upload_user_id")
    private Long userId;

    @Schema(description = "播放次数")
    @TableField("play_count")
    private Integer playCount;

    @Schema(description = "音乐状态：0-禁用，1-启用")
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

    @Schema(description = "逻辑删除标志：0-未删除，1-已删除")
    @TableField("is_deleted")
    @TableLogic
    private Integer deleted;

    @Schema(description = "音乐流派")
    @TableField("genre")
    private String genre;

    @Schema(description = "发布年份")
    @TableField("release_year")
    private Integer releaseYear;

    @Schema(description = "音乐描述")
    @TableField("description")
    private String description;

    @Schema(description = "标签（逗号分隔）")
    @TableField("tags")
    private String tags;

    @Schema(description = "是否公开：0-私有，1-公开")
    @TableField("is_public")
    private Integer isPublic;

    @Schema(description = "是否允许下载：0-不允许，1-允许")
    @TableField("allow_download")
    private Integer allowDownload;

    @Schema(description = "是否允许评论：0-不允许，1-允许")
    @TableField("allow_comment")
    private Integer allowComment;

    @Schema(description = "封面图片URL")
    @TableField("cover_url")
    private String coverUrl;

    // 构造函数
    public Music() {}

    public Music(String title, String artist, String album, String filePath, String fileName, String originalName, Long fileSize, String fileType, Long userId) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.filePath = filePath;
        this.fileName = fileName;
        this.originalName = originalName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.userId = userId;
        this.playCount = 0;
        this.status = 1; // 默认启用
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }



    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getAllowDownload() {
        return allowDownload;
    }

    public void setAllowDownload(Integer allowDownload) {
        this.allowDownload = allowDownload;
    }

    public Integer getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", originalName='" + originalName + '\'' +
                ", fileSize=" + fileSize +

                ", fileType='" + fileType + '\'' +
                ", userId=" + userId +
                ", playCount=" + playCount +
                ", status=" + status +
                ", genre='" + genre + '\'' +
                ", releaseYear=" + releaseYear +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", isPublic=" + isPublic +
                ", allowDownload=" + allowDownload +
                ", allowComment=" + allowComment +
                ", coverUrl='" + coverUrl + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                '}';
    }
}