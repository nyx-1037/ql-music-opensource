package com.musicmanager.system.mapper;

import java.util.List;
import com.musicmanager.system.domain.QlPlaylist;

/**
 * 歌单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface QlPlaylistMapper 
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
     * 删除歌单
     * 
     * @param id 歌单主键
     * @return 结果
     */
    public int deleteQlPlaylistById(Long id);

    /**
     * 批量删除歌单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQlPlaylistByIds(Long[] ids);
}
