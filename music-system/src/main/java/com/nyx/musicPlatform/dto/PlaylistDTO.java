package com.nyx.musicPlatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 歌单数据传输对象
 *
 * @author nyx
 * @since 2025-06
 */
@Data
@Schema(description = "歌单数据传输对象")
public class PlaylistDTO {

    @Schema(description = "歌单名称")
    @NotBlank(message = "歌单名称不能为空")
    @Size(max = 100, message = "歌单名称长度不能超过100个字符")
    private String name;

    @Schema(description = "歌单描述")
    @Size(max = 500, message = "歌单描述长度不能超过500个字符")
    private String description;

    @Schema(description = "是否公开")
    @NotNull(message = "是否公开不能为空")
    private Boolean isPublic;

    @Schema(description = "封面图片URL")
    private String coverUrl;
}