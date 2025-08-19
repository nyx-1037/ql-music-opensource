package com.musicmanager.system.service.impl;

import java.util.List;
import com.musicmanager.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmanager.system.mapper.QlMusicCategoryMapper;
import com.musicmanager.system.domain.QlMusicCategory;
import com.musicmanager.system.service.IQlMusicCategoryService;

/**
 * 音乐分类Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@Service
public class QlMusicCategoryServiceImpl implements IQlMusicCategoryService 
{
    @Autowired
    private QlMusicCategoryMapper qlMusicCategoryMapper;

    /**
     * 查询音乐分类
     * 
     * @param id 音乐分类主键
     * @return 音乐分类
     */
    @Override
    public QlMusicCategory selectQlMusicCategoryById(Long id)
    {
        return qlMusicCategoryMapper.selectQlMusicCategoryById(id);
    }

    /**
     * 查询音乐分类列表
     * 
     * @param qlMusicCategory 音乐分类
     * @return 音乐分类
     */
    @Override
    public List<QlMusicCategory> selectQlMusicCategoryList(QlMusicCategory qlMusicCategory)
    {
        return qlMusicCategoryMapper.selectQlMusicCategoryList(qlMusicCategory);
    }

    /**
     * 新增音乐分类
     * 
     * @param qlMusicCategory 音乐分类
     * @return 结果
     */
    @Override
    public int insertQlMusicCategory(QlMusicCategory qlMusicCategory)
    {
        qlMusicCategory.setCreateTime(DateUtils.getNowDate());
        return qlMusicCategoryMapper.insertQlMusicCategory(qlMusicCategory);
    }

    /**
     * 修改音乐分类
     * 
     * @param qlMusicCategory 音乐分类
     * @return 结果
     */
    @Override
    public int updateQlMusicCategory(QlMusicCategory qlMusicCategory)
    {
        qlMusicCategory.setUpdateTime(DateUtils.getNowDate());
        return qlMusicCategoryMapper.updateQlMusicCategory(qlMusicCategory);
    }

    /**
     * 批量删除音乐分类
     * 
     * @param ids 需要删除的音乐分类主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicCategoryByIds(Long[] ids)
    {
        return qlMusicCategoryMapper.deleteQlMusicCategoryByIds(ids);
    }

    /**
     * 删除音乐分类信息
     * 
     * @param id 音乐分类主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicCategoryById(Long id)
    {
        return qlMusicCategoryMapper.deleteQlMusicCategoryById(id);
    }
}
