package com.nyx.musicPlatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.entity.Music;
import com.nyx.musicPlatform.entity.MusicCategory;
import com.nyx.musicPlatform.entity.MusicCategoryRelation;
import com.nyx.musicPlatform.mapper.MusicCategoryMapper;
import com.nyx.musicPlatform.mapper.MusicCategoryRelationMapper;
import com.nyx.musicPlatform.mapper.MusicMapper;
import com.nyx.musicPlatform.service.MusicCategoryService;
import com.nyx.musicPlatform.vo.MusicVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐分类服务实现类
 *
 * @author nyx
 * @since 2025-06-01
 */
@Service
public class MusicCategoryServiceImpl extends ServiceImpl<MusicCategoryMapper, MusicCategory> implements MusicCategoryService {

    @Autowired
    private MusicCategoryRelationMapper relationMapper;
    
    @Autowired
    private MusicMapper musicMapper;

    @Override
    public Result<List<MusicCategory>> getAllCategories() {
        List<MusicCategory> categories = baseMapper.getEnabledCategories();
        return Result.success(categories);
    }

    @Override
    public Result<List<MusicVO>> getMusicByCategory(Long categoryId) {
        List<Long> musicIds = relationMapper.getMusicIdsByCategoryId(categoryId);
        
        if (musicIds.isEmpty()) {
            return Result.success(new ArrayList<>());
        }
        
        List<Music> musicList = musicMapper.selectBatchIds(musicIds);
        List<MusicVO> musicVOList = new ArrayList<>();
        
        for (Music music : musicList) {
            MusicVO musicVO = new MusicVO();
            BeanUtils.copyProperties(music, musicVO);
            musicVOList.add(musicVO);
        }
        
        return Result.success(musicVOList);
    }

    @Override
    @Transactional
    public Result<Void> setMusicCategories(Long musicId, List<Long> categoryIds) {
        // 检查音乐是否存在
        Music music = musicMapper.selectById(musicId);
        if (music == null) {
            return Result.error(ResultCode.MUSIC_NOT_FOUND, "音乐不存在");
        }
        
        // 删除原有的分类关系
        relationMapper.deleteByMusicId(musicId);
        
        // 添加新的分类关系
        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<MusicCategoryRelation> relations = new ArrayList<>();
            for (Long categoryId : categoryIds) {
                MusicCategoryRelation relation = new MusicCategoryRelation();
                relation.setMusicId(musicId);
                relation.setCategoryId(categoryId);
                relation.setCreateTime(LocalDateTime.now());
                relations.add(relation);
            }
            relationMapper.batchInsert(relations);
        }
        
        return Result.success();
    }

    @Override
    public Result<List<MusicCategory>> getMusicCategories(Long musicId) {
        List<MusicCategory> categories = baseMapper.getCategoriesByMusicId(musicId);
        return Result.success(categories);
    }

}