package com.musicmanager.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.musicmanager.common.annotation.Excel;
import com.musicmanager.common.core.domain.BaseEntity;

/**
 * 音乐对象 ql_music
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public class QlMusic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 音乐ID */
    private Long id;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 艺术家 */
    @Excel(name = "艺术家")
    private String artist;

    /** 专辑 */
    @Excel(name = "专辑")
    private String album;

    /** 时长（秒） */
    @Excel(name = "时长", readConverterExp = "秒=")
    private Long duration;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    /** 文件名（UUID） */
    @Excel(name = "文件名", readConverterExp = "U=UID")
    private String fileName;

    /** 原始文件名 */
    @Excel(name = "原始文件名")
    private String originalFileName;

    /** 文件大小（字节） */
    @Excel(name = "文件大小", readConverterExp = "字=节")
    private Long fileSize;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String fileType;

    /** 上传用户ID */
    @Excel(name = "上传用户ID")
    private Long uploadUserId;

    /** 播放次数 */
    @Excel(name = "播放次数")
    private Long playCount;

    /** 状态（0-禁用，1-启用） */
    @Excel(name = "状态", readConverterExp = "0=-禁用，1-启用")
    private Long status;

    /** 逻辑删除标志（0-未删除，1-已删除） */
    @Excel(name = "逻辑删除标志", readConverterExp = "0=-未删除，1-已删除")
    private Long isDeleted;

    /** 音乐流派 */
    @Excel(name = "音乐流派")
    private String genre;

    /** 发布年份 */
    @Excel(name = "发布年份")
    private Long releaseYear;

    /** 音乐描述 */
    @Excel(name = "音乐描述")
    private String description;

    /** 标签(逗号分隔) */
    @Excel(name = "标签(逗号分隔)")
    private String tags;

    /** 是否公开(0:私有 1:公开) */
    @Excel(name = "是否公开(0:私有 1:公开)")
    private Long isPublic;

    /** 是否允许下载(0:不允许 1:允许) */
    @Excel(name = "是否允许下载(0:不允许 1:允许)")
    private Long allowDownload;

    /** 是否允许评论(0:不允许 1:允许) */
    @Excel(name = "是否允许评论(0:不允许 1:允许)")
    private Long allowComment;

    /** 封面图片URL */
    @Excel(name = "封面图片URL")
    private String coverUrl;

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

    public void setArtist(String artist) 
    {
        this.artist = artist;
    }

    public String getArtist() 
    {
        return artist;
    }

    public void setAlbum(String album) 
    {
        this.album = album;
    }

    public String getAlbum() 
    {
        return album;
    }

    public void setDuration(Long duration) 
    {
        this.duration = duration;
    }

    public Long getDuration() 
    {
        return duration;
    }

    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setOriginalFileName(String originalFileName) 
    {
        this.originalFileName = originalFileName;
    }

    public String getOriginalFileName() 
    {
        return originalFileName;
    }

    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setUploadUserId(Long uploadUserId) 
    {
        this.uploadUserId = uploadUserId;
    }

    public Long getUploadUserId() 
    {
        return uploadUserId;
    }

    public void setPlayCount(Long playCount) 
    {
        this.playCount = playCount;
    }

    public Long getPlayCount() 
    {
        return playCount;
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

    public void setGenre(String genre) 
    {
        this.genre = genre;
    }

    public String getGenre() 
    {
        return genre;
    }

    public void setReleaseYear(Long releaseYear) 
    {
        this.releaseYear = releaseYear;
    }

    public Long getReleaseYear() 
    {
        return releaseYear;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }

    public void setIsPublic(Long isPublic) 
    {
        this.isPublic = isPublic;
    }

    public Long getIsPublic() 
    {
        return isPublic;
    }

    public void setAllowDownload(Long allowDownload) 
    {
        this.allowDownload = allowDownload;
    }

    public Long getAllowDownload() 
    {
        return allowDownload;
    }

    public void setAllowComment(Long allowComment) 
    {
        this.allowComment = allowComment;
    }

    public Long getAllowComment() 
    {
        return allowComment;
    }

    public void setCoverUrl(String coverUrl) 
    {
        this.coverUrl = coverUrl;
    }

    public String getCoverUrl() 
    {
        return coverUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("artist", getArtist())
            .append("album", getAlbum())
            .append("duration", getDuration())
            .append("filePath", getFilePath())
            .append("fileName", getFileName())
            .append("originalFileName", getOriginalFileName())
            .append("fileSize", getFileSize())
            .append("fileType", getFileType())
            .append("uploadUserId", getUploadUserId())
            .append("playCount", getPlayCount())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDeleted", getIsDeleted())
            .append("genre", getGenre())
            .append("releaseYear", getReleaseYear())
            .append("description", getDescription())
            .append("tags", getTags())
            .append("isPublic", getIsPublic())
            .append("allowDownload", getAllowDownload())
            .append("allowComment", getAllowComment())
            .append("coverUrl", getCoverUrl())
            .toString();
    }
}
