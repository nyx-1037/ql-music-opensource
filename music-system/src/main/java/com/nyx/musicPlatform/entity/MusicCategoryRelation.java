package com.nyx.musicPlatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 音乐分类关系实体类
 *
 * @author nyx
 * @since 2025-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ql_music_category_relation")
@Schema(description = "音乐分类关系表")
public class MusicCategoryRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "关系ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "音乐ID")
    @TableField("music_id")
    private Long musicId;

    @Schema(description = "分类ID")
    @TableField("category_id")
    private Long categoryId;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

}