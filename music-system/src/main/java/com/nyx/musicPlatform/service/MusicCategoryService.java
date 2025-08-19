package com.nyx.musicPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.entity.MusicCategory;
import com.nyx.musicPlatform.vo.MusicVO;

import java.util.List;

/**
 * 音乐分类服务接口
 *
 * @author nyx
 * @since 2025-06-01
 */
public interface MusicCategoryService extends IService<MusicCategory> {

    /**
     * 获取所有启用的分类
     *
     * @return 分类列表
     */
    Result<List<MusicCategory>> getAllCategories();

    /**
     * 根据分类ID获取音乐列表
     *
     * @param categoryId 分类ID
     * @return 音乐列表
     */
    Result<List<MusicVO>> getMusicByCategory(Long categoryId);

    /**
     * 为音乐设置分类
     *
     * @param musicId 音乐ID
     * @param categoryIds 分类ID列表
     * @return 操作结果
     */
    Result<Void> setMusicCategories(Long musicId, List<Long> categoryIds);

    /**
     * 获取音乐的分类列表
     *
     * @param musicId 音乐ID
     * @return 分类列表
     */
    Result<List<MusicCategory>> getMusicCategories(Long musicId);

}