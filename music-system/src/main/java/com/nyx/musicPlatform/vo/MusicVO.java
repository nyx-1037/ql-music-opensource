package com.nyx.musicPlatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 音乐响应视图对象
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@Schema(description = "音乐响应")
public class MusicVO {

    @Schema(description = "音乐ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "艺术家")
    private String artist;

    @Schema(description = "专辑")
    private String album;

    @Schema(description = "时长（秒）")
    private Integer duration;

    @Schema(description = "原始文件名")
    private String originalFileName;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "上传用户ID")
    private Long uploadUserId;

    @Schema(description = "上传用户名")
    private String uploadUsername;

    @Schema(description = "播放次数")
    private Long playCount;

    @Schema(description = "状态（0-禁用，1-启用）")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "播放URL")
    private String playUrl;

    @Schema(description = "下载URL")
    private String downloadUrl;

    @Schema(description = "封面URL")
    private String coverUrl;
}