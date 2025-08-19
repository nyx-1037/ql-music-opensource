package com.nyx.musicPlatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 音乐播放历史实体类
 *
 * @author nyx
 * @since 2025-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ql_music_play_history")
@Schema(description = "音乐播放历史表")
public class MusicPlayHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "播放历史ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "音乐ID")
    @TableField("music_id")
    private Long musicId;

    @Schema(description = "播放时间")
    @TableField("play_time")
    private LocalDateTime playTime;

    @Schema(description = "播放时长(秒)")
    @TableField("play_duration")
    private Integer playDuration;

    @Schema(description = "播放位置(秒)")
    @TableField("play_position")
    private Integer playPosition;

}