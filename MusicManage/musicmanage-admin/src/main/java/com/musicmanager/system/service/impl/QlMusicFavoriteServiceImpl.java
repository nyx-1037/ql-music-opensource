package com.musicmanager.system.service.impl;

import java.util.List;
import com.musicmanager.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmanager.system.mapper.QlMusicFavoriteMapper;
import com.musicmanager.system.domain.QlMusicFavorite;
import com.musicmanager.system.service.IQlMusicFavoriteService;

/**
 * 音乐收藏Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@Service
public class QlMusicFavoriteServiceImpl implements IQlMusicFavoriteService 
{
    @Autowired
    private QlMusicFavoriteMapper qlMusicFavoriteMapper;

    /**
     * 查询音乐收藏
     * 
     * @param id 音乐收藏主键
     * @return 音乐收藏
     */
    @Override
    public QlMusicFavorite selectQlMusicFavoriteById(Long id)
    {
        return qlMusicFavoriteMapper.selectQlMusicFavoriteById(id);
    }

    /**
     * 查询音乐收藏列表
     * 
     * @param qlMusicFavorite 音乐收藏
     * @return 音乐收藏
     */
    @Override
    public List<QlMusicFavorite> selectQlMusicFavoriteList(QlMusicFavorite qlMusicFavorite)
    {
        return qlMusicFavoriteMapper.selectQlMusicFavoriteList(qlMusicFavorite);
    }

    /**
     * 新增音乐收藏
     * 
     * @param qlMusicFavorite 音乐收藏
     * @return 结果
     */
    @Override
    public int insertQlMusicFavorite(QlMusicFavorite qlMusicFavorite)
    {
        qlMusicFavorite.setCreateTime(DateUtils.getNowDate());
        return qlMusicFavoriteMapper.insertQlMusicFavorite(qlMusicFavorite);
    }

    /**
     * 修改音乐收藏
     * 
     * @param qlMusicFavorite 音乐收藏
     * @return 结果
     */
    @Override
    public int updateQlMusicFavorite(QlMusicFavorite qlMusicFavorite)
    {
        return qlMusicFavoriteMapper.updateQlMusicFavorite(qlMusicFavorite);
    }

    /**
     * 批量删除音乐收藏
     * 
     * @param ids 需要删除的音乐收藏主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicFavoriteByIds(Long[] ids)
    {
        return qlMusicFavoriteMapper.deleteQlMusicFavoriteByIds(ids);
    }

    /**
     * 删除音乐收藏信息
     * 
     * @param id 音乐收藏主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicFavoriteById(Long id)
    {
        return qlMusicFavoriteMapper.deleteQlMusicFavoriteById(id);
    }
}
