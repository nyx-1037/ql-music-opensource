package com.musicmanager.system.mapper;

import java.util.List;
import com.musicmanager.system.domain.QlMusicCategoryRelation;

/**
 * 音乐分类关联Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface QlMusicCategoryRelationMapper 
{
    /**
     * 查询音乐分类关联
     * 
     * @param id 音乐分类关联主键
     * @return 音乐分类关联
     */
    public QlMusicCategoryRelation selectQlMusicCategoryRelationById(Long id);

    /**
     * 查询音乐分类关联列表
     * 
     * @param qlMusicCategoryRelation 音乐分类关联
     * @return 音乐分类关联集合
     */
    public List<QlMusicCategoryRelation> selectQlMusicCategoryRelationList(QlMusicCategoryRelation qlMusicCategoryRelation);

    /**
     * 新增音乐分类关联
     * 
     * @param qlMusicCategoryRelation 音乐分类关联
     * @return 结果
     */
    public int insertQlMusicCategoryRelation(QlMusicCategoryRelation qlMusicCategoryRelation);

    /**
     * 修改音乐分类关联
     * 
     * @param qlMusicCategoryRelation 音乐分类关联
     * @return 结果
     */
    public int updateQlMusicCategoryRelation(QlMusicCategoryRelation qlMusicCategoryRelation);

    /**
     * 删除音乐分类关联
     * 
     * @param id 音乐分类关联主键
     * @return 结果
     */
    public int deleteQlMusicCategoryRelationById(Long id);

    /**
     * 批量删除音乐分类关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQlMusicCategoryRelationByIds(Long[] ids);
}
