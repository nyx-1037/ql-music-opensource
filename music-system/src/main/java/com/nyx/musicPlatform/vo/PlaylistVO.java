package com.nyx.musicPlatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 歌单视图对象
 *
 * @author nyx
 * @since 2025-06
 */
@Data
@Schema(description = "歌单视图对象")
public class PlaylistVO {

    @Schema(description = "歌单ID")
    private Long id;

    @Schema(description = "歌单名称")
    private String name;

    @Schema(description = "歌单描述")
    private String description;

    @Schema(description = "封面图片URL")
    private String coverUrl;

    @Schema(description = "创建用户ID")
    private Long userId;

    @Schema(description = "创建用户名")
    private String username;

    @Schema(description = "创建用户昵称")
    private String nickname;

    @Schema(description = "是否公开")
    private Boolean isPublic;

    @Schema(description = "播放次数")
    private Long playCount;

    @Schema(description = "歌曲数量")
    private Integer musicCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "歌单中的音乐列表")
    private List<MusicVO> musics;

    @Schema(description = "是否为当前用户创建")
    private Boolean isOwner;
}