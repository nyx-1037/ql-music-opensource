package com.nyx.musicPlatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 音乐收藏实体类
 *
 * @author nyx
 * @since 2025-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ql_music_favorite")
@Schema(description = "音乐收藏表")
public class MusicFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "收藏ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "音乐ID")
    @TableField("music_id")
    private Long musicId;

    @Schema(description = "歌单ID")
    @TableField("playlist_id")
    private Long playlistId;

    @Schema(description = "收藏时间")
    @TableField("create_time")
    private LocalDateTime createTime;

}