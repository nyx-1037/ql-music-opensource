package com.musicmanager.system.mapper;

import java.util.List;
import com.musicmanager.system.domain.QlMusicCategory;

/**
 * 音乐分类Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface QlMusicCategoryMapper 
{
    /**
     * 查询音乐分类
     * 
     * @param id 音乐分类主键
     * @return 音乐分类
     */
    public QlMusicCategory selectQlMusicCategoryById(Long id);

    /**
     * 查询音乐分类列表
     * 
     * @param qlMusicCategory 音乐分类
     * @return 音乐分类集合
     */
    public List<QlMusicCategory> selectQlMusicCategoryList(QlMusicCategory qlMusicCategory);

    /**
     * 新增音乐分类
     * 
     * @param qlMusicCategory 音乐分类
     * @return 结果
     */
    public int insertQlMusicCategory(QlMusicCategory qlMusicCategory);

    /**
     * 修改音乐分类
     * 
     * @param qlMusicCategory 音乐分类
     * @return 结果
     */
    public int updateQlMusicCategory(QlMusicCategory qlMusicCategory);

    /**
     * 删除音乐分类
     * 
     * @param id 音乐分类主键
     * @return 结果
     */
    public int deleteQlMusicCategoryById(Long id);

    /**
     * 批量删除音乐分类
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQlMusicCategoryByIds(Long[] ids);
}
