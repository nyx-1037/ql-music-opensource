package com.nyx.musicPlatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 歌单实体类
 *
 * @author nyx
 * @since 2025-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ql_playlist")
@Schema(description = "歌单实体")
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "歌单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "歌单名称")
    @TableField("name")
    private String name;

    @Schema(description = "歌单描述")
    @TableField("description")
    private String description;

    @Schema(description = "封面图片URL")
    @TableField("cover_url")
    private String coverUrl;

    @Schema(description = "创建用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "是否公开（0-私有，1-公开）")
    @TableField("is_public")
    private Boolean isPublic;

    @Schema(description = "播放次数")
    @TableField("play_count")
    private Long playCount;

    @Schema(description = "歌曲数量")
    @TableField("music_count")
    private Integer musicCount;

    @Schema(description = "状态（0-禁用，1-启用）")
    @TableField("status")
    private Integer status;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除标志（0-未删除，1-已删除）")
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}