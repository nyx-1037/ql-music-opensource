package com.nyx.musicPlatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 歌单音乐关联实体类
 *
 * @author nyx
 * @since 2025-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ql_playlist_music")
@Schema(description = "歌单音乐关联实体")
public class PlaylistMusic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "关联ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "歌单ID")
    @TableField("playlist_id")
    private Long playlistId;

    @Schema(description = "音乐ID")
    @TableField("music_id")
    private Long musicId;

    @Schema(description = "排序顺序")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "添加时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}