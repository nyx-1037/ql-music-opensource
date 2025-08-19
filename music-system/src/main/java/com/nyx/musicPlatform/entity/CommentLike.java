package com.nyx.musicPlatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论点赞实体类
 * 对应数据库表：ql_comment_like
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ql_comment_like")
public class CommentLike {
    
    /**
     * 点赞ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 评论ID
     */
    @TableField("comment_id")
    private Long commentId;
    
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 构造函数
     */
    public CommentLike(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}