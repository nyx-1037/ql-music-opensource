package com.nyx.musicPlatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Banner管理实体类
 *
 * @author nyx
 * @since 2025-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ql_banner")
@Schema(name = "Banner", description = "Banner管理")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Banner ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "Banner标题")
    @TableField("title")
    private String title;

    @Schema(description = "Banner描述")
    @TableField("description")
    private String description;

    @Schema(description = "Banner图片OSS地址")
    @TableField("image_url")
    private String imageUrl;

    @Schema(description = "按钮文字")
    @TableField("button_text")
    private String buttonText;

    @Schema(description = "点击动作类型")
    @TableField("action_type")
    private String actionType;

    @Schema(description = "跳转链接")
    @TableField("action_url")
    private String actionUrl;

    @Schema(description = "排序顺序")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "状态(0:禁用 1:启用)")
    @TableField("status")
    private Integer status;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建者ID")
    @TableField("creator_id")
    private Long creatorId;
}