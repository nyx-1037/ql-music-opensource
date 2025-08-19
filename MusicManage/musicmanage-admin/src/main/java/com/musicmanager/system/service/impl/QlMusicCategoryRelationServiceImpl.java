package com.musicmanager.system.service.impl;

import java.util.List;
import com.musicmanager.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmanager.system.mapper.QlMusicCategoryRelationMapper;
import com.musicmanager.system.domain.QlMusicCategoryRelation;
import com.musicmanager.system.service.IQlMusicCategoryRelationService;

/**
 * 音乐分类关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@Service
public class QlMusicCategoryRelationServiceImpl implements IQlMusicCategoryRelationService 
{
    @Autowired
    private QlMusicCategoryRelationMapper qlMusicCategoryRelationMapper;

    /**
     * 查询音乐分类关联
     * 
     * @param id 音乐分类关联主键
     * @return 音乐分类关联
     */
    @Override
    public QlMusicCategoryRelation selectQlMusicCategoryRelationById(Long id)
    {
        return qlMusicCategoryRelationMapper.selectQlMusicCategoryRelationById(id);
    }

    /**
     * 查询音乐分类关联列表
     * 
     * @param qlMusicCategoryRelation 音乐分类关联
     * @return 音乐分类关联
     */
    @Override
    public List<QlMusicCategoryRelation> selectQlMusicCategoryRelationList(QlMusicCategoryRelation qlMusicCategoryRelation)
    {
        return qlMusicCategoryRelationMapper.selectQlMusicCategoryRelationList(qlMusicCategoryRelation);
    }

    /**
     * 新增音乐分类关联
     * 
     * @param qlMusicCategoryRelation 音乐分类关联
     * @return 结果
     */
    @Override
    public int insertQlMusicCategoryRelation(QlMusicCategoryRelation qlMusicCategoryRelation)
    {
        qlMusicCategoryRelation.setCreateTime(DateUtils.getNowDate());
        return qlMusicCategoryRelationMapper.insertQlMusicCategoryRelation(qlMusicCategoryRelation);
    }

    /**
     * 修改音乐分类关联
     * 
     * @param qlMusicCategoryRelation 音乐分类关联
     * @return 结果
     */
    @Override
    public int updateQlMusicCategoryRelation(QlMusicCategoryRelation qlMusicCategoryRelation)
    {
        return qlMusicCategoryRelationMapper.updateQlMusicCategoryRelation(qlMusicCategoryRelation);
    }

    /**
     * 批量删除音乐分类关联
     * 
     * @param ids 需要删除的音乐分类关联主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicCategoryRelationByIds(Long[] ids)
    {
        return qlMusicCategoryRelationMapper.deleteQlMusicCategoryRelationByIds(ids);
    }

    /**
     * 删除音乐分类关联信息
     * 
     * @param id 音乐分类关联主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicCategoryRelationById(Long id)
    {
        return qlMusicCategoryRelationMapper.deleteQlMusicCategoryRelationById(id);
    }
}
