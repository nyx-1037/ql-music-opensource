package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nyx.musicPlatform.entity.MusicCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 音乐分类 Mapper 接口
 *
 * @author nyx
 * @since 2025-06
 */
@Mapper
public interface MusicCategoryMapper extends BaseMapper<MusicCategory> {

    /**
     * 获取启用状态的分类列表
     *
     * @return 分类列表
     */
    List<MusicCategory> getEnabledCategories();

    /**
     * 根据音乐ID获取分类列表
     *
     * @param musicId 音乐ID
     * @return 分类列表
     */
    List<MusicCategory> getCategoriesByMusicId(@Param("musicId") Long musicId);

}