package com.musicmanager.system.service;

import java.util.List;
import com.musicmanager.system.domain.QlPlaylistMusic;

/**
 * 歌单音乐关联Service接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface IQlPlaylistMusicService 
{
    /**
     * 查询歌单音乐关联
     * 
     * @param id 歌单音乐关联主键
     * @return 歌单音乐关联
     */
    public QlPlaylistMusic selectQlPlaylistMusicById(Long id);

    /**
     * 查询歌单音乐关联列表
     * 
     * @param qlPlaylistMusic 歌单音乐关联
     * @return 歌单音乐关联集合
     */
    public List<QlPlaylistMusic> selectQlPlaylistMusicList(QlPlaylistMusic qlPlaylistMusic);

    /**
     * 新增歌单音乐关联
     * 
     * @param qlPlaylistMusic 歌单音乐关联
     * @return 结果
     */
    public int insertQlPlaylistMusic(QlPlaylistMusic qlPlaylistMusic);

    /**
     * 修改歌单音乐关联
     * 
     * @param qlPlaylistMusic 歌单音乐关联
     * @return 结果
     */
    public int updateQlPlaylistMusic(QlPlaylistMusic qlPlaylistMusic);

    /**
     * 批量删除歌单音乐关联
     * 
     * @param ids 需要删除的歌单音乐关联主键集合
     * @return 结果
     */
    public int deleteQlPlaylistMusicByIds(Long[] ids);

    /**
     * 删除歌单音乐关联信息
     * 
     * @param id 歌单音乐关联主键
     * @return 结果
     */
    public int deleteQlPlaylistMusicById(Long id);
}
