package com.musicmanager.system.mapper;

import java.util.List;
import com.musicmanager.system.domain.QlMusicFavorite;

/**
 * 音乐收藏Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface QlMusicFavoriteMapper 
{
    /**
     * 查询音乐收藏
     * 
     * @param id 音乐收藏主键
     * @return 音乐收藏
     */
    public QlMusicFavorite selectQlMusicFavoriteById(Long id);

    /**
     * 查询音乐收藏列表
     * 
     * @param qlMusicFavorite 音乐收藏
     * @return 音乐收藏集合
     */
    public List<QlMusicFavorite> selectQlMusicFavoriteList(QlMusicFavorite qlMusicFavorite);

    /**
     * 新增音乐收藏
     * 
     * @param qlMusicFavorite 音乐收藏
     * @return 结果
     */
    public int insertQlMusicFavorite(QlMusicFavorite qlMusicFavorite);

    /**
     * 修改音乐收藏
     * 
     * @param qlMusicFavorite 音乐收藏
     * @return 结果
     */
    public int updateQlMusicFavorite(QlMusicFavorite qlMusicFavorite);

    /**
     * 删除音乐收藏
     * 
     * @param id 音乐收藏主键
     * @return 结果
     */
    public int deleteQlMusicFavoriteById(Long id);

    /**
     * 批量删除音乐收藏
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQlMusicFavoriteByIds(Long[] ids);
}
