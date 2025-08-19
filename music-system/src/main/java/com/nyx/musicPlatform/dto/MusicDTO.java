package com.nyx.musicPlatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 音乐数据传输对象
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@Schema(description = "音乐信息")
public class MusicDTO {

    @Schema(description = "音乐ID")
    private Long id;

    @Schema(description = "标题")
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;

    @Schema(description = "艺术家")
    @NotBlank(message = "艺术家不能为空")
    @Size(max = 100, message = "艺术家长度不能超过100个字符")
    private String artist;

    @Schema(description = "专辑")
    @Size(max = 100, message = "专辑长度不能超过100个字符")
    private String album;

    @Schema(description = "时长（秒）")
    private Integer duration;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件名（UUID）")
    private String fileName;

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
}