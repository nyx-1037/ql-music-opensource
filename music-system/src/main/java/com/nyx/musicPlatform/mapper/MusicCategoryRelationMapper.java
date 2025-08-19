package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nyx.musicPlatform.entity.MusicCategoryRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 音乐分类关系 Mapper 接口
 *
 * @author nyx
 * @since 2025-06-01
 */
@Mapper
public interface MusicCategoryRelationMapper extends BaseMapper<MusicCategoryRelation> {

    /**
     * 根据音乐ID删除分类关系
     *
     * @param musicId 音乐ID
     * @return 删除数量
     */
    int deleteByMusicId(@Param("musicId") Long musicId);

    /**
     * 根据分类ID获取音乐ID列表
     *
     * @param categoryId 分类ID
     * @return 音乐ID列表
     */
    List<Long> getMusicIdsByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 批量插入音乐分类关系
     *
     * @param relations 关系列表
     * @return 插入数量
     */
    int batchInsert(@Param("relations") List<MusicCategoryRelation> relations);

}