package com.nyx.musicPlatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计信息响应视图对象
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统计信息响应")
public class StatisticsVO {

    @Schema(description = "总用户数")
    private Long totalUsers;

    @Schema(description = "启用用户数")
    private Long activeUsers;

    @Schema(description = "禁用用户数")
    private Long inactiveUsers;

    @Schema(description = "总音乐数")
    private Long totalMusic;

    @Schema(description = "启用音乐数")
    private Long activeMusic;

    @Schema(description = "禁用音乐数")
    private Long inactiveMusic;

    @Schema(description = "用户上传音乐数")
    private Long userUploadMusic;

    @Schema(description = "系统音乐数")
    private Long systemMusic;

    @Schema(description = "总播放次数")
    private Long totalPlayCount;

    @Schema(description = "今日新增用户")
    private Long todayNewUsers;

    @Schema(description = "今日新增音乐")
    private Long todayNewMusic;

    @Schema(description = "今日播放次数")
    private Long todayPlayCount;
}