package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nyx.musicPlatform.entity.MusicFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 音乐收藏 Mapper 接口
 *
 * @author nyx
 * @since 2025-06
 */
@Mapper
public interface MusicFavoriteMapper extends BaseMapper<MusicFavorite> {

    /**
     * 根据用户ID获取收藏的音乐ID列表
     *
     * @param userId 用户ID
     * @return 音乐ID列表
     */
    List<Long> getFavoriteMusicIdsByUserId(@Param("userId") Long userId);

    /**
     * 检查用户是否已收藏某首音乐
     *
     * @param userId 用户ID
     * @param musicId 音乐ID
     * @return 收藏记录数量
     */
    int checkFavoriteExists(@Param("userId") Long userId, @Param("musicId") Long musicId);

}