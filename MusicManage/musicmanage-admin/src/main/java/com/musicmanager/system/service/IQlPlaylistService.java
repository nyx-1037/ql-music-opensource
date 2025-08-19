package com.musicmanager.system.service;

import java.util.List;
import com.musicmanager.system.domain.QlPlaylist;

/**
 * 歌单Service接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface IQlPlaylistService 
{
    /**
     * 查询歌单
     * 
     * @param id 歌单主键
     * @return 歌单
     */
    public QlPlaylist selectQlPlaylistById(Long id);

    /**
     * 查询歌单列表
     * 
     * @param qlPlaylist 歌单
     * @return 歌单集合
     */
    public List<QlPlaylist> selectQlPlaylistList(QlPlaylist qlPlaylist);

    /**
     * 新增歌单
     * 
     * @param qlPlaylist 歌单
     * @return 结果
     */
    public int insertQlPlaylist(QlPlaylist qlPlaylist);

    /**
     * 修改歌单
     * 
     * @param qlPlaylist 歌单
     * @return 结果
     */
    public int updateQlPlaylist(QlPlaylist qlPlaylist);

    /**
     * 批量删除歌单
     * 
     * @param ids 需要删除的歌单主键集合
     * @return 结果
     */
    public int deleteQlPlaylistByIds(Long[] ids);

    /**
     * 删除歌单信息
     * 
     * @param id 歌单主键
     * @return 结果
     */
    public int deleteQlPlaylistById(Long id);
}
