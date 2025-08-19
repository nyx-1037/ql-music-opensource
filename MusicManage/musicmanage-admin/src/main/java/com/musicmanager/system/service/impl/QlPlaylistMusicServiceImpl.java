package com.musicmanager.system.service.impl;

import java.util.List;
import com.musicmanager.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmanager.system.mapper.QlPlaylistMusicMapper;
import com.musicmanager.system.domain.QlPlaylistMusic;
import com.musicmanager.system.service.IQlPlaylistMusicService;

/**
 * 歌单音乐关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@Service
public class QlPlaylistMusicServiceImpl implements IQlPlaylistMusicService 
{
    @Autowired
    private QlPlaylistMusicMapper qlPlaylistMusicMapper;

    /**
     * 查询歌单音乐关联
     * 
     * @param id 歌单音乐关联主键
     * @return 歌单音乐关联
     */
    @Override
    public QlPlaylistMusic selectQlPlaylistMusicById(Long id)
    {
        return qlPlaylistMusicMapper.selectQlPlaylistMusicById(id);
    }

    /**
     * 查询歌单音乐关联列表
     * 
     * @param qlPlaylistMusic 歌单音乐关联
     * @return 歌单音乐关联
     */
    @Override
    public List<QlPlaylistMusic> selectQlPlaylistMusicList(QlPlaylistMusic qlPlaylistMusic)
    {
        return qlPlaylistMusicMapper.selectQlPlaylistMusicList(qlPlaylistMusic);
    }

    /**
     * 新增歌单音乐关联
     * 
     * @param qlPlaylistMusic 歌单音乐关联
     * @return 结果
     */
    @Override
    public int insertQlPlaylistMusic(QlPlaylistMusic qlPlaylistMusic)
    {
        qlPlaylistMusic.setCreateTime(DateUtils.getNowDate());
        return qlPlaylistMusicMapper.insertQlPlaylistMusic(qlPlaylistMusic);
    }

    /**
     * 修改歌单音乐关联
     * 
     * @param qlPlaylistMusic 歌单音乐关联
     * @return 结果
     */
    @Override
    public int updateQlPlaylistMusic(QlPlaylistMusic qlPlaylistMusic)
    {
        return qlPlaylistMusicMapper.updateQlPlaylistMusic(qlPlaylistMusic);
    }

    /**
     * 批量删除歌单音乐关联
     * 
     * @param ids 需要删除的歌单音乐关联主键
     * @return 结果
     */
    @Override
    public int deleteQlPlaylistMusicByIds(Long[] ids)
    {
        return qlPlaylistMusicMapper.deleteQlPlaylistMusicByIds(ids);
    }

    /**
     * 删除歌单音乐关联信息
     * 
     * @param id 歌单音乐关联主键
     * @return 结果
     */
    @Override
    public int deleteQlPlaylistMusicById(Long id)
    {
        return qlPlaylistMusicMapper.deleteQlPlaylistMusicById(id);
    }
}
