package com.nyx.musicPlatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 音乐更新请求数据传输对象
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@Schema(description = "音乐更新请求")
public class MusicUpdateDTO {

    @Schema(description = "标题", example = "新歌名")
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;

    @Schema(description = "艺术家", example = "新艺术家")
    @NotBlank(message = "艺术家不能为空")
    @Size(max = 100, message = "艺术家长度不能超过100个字符")
    private String artist;

    @Schema(description = "专辑", example = "新专辑")
    @Size(max = 100, message = "专辑长度不能超过100个字符")
    private String album;


}