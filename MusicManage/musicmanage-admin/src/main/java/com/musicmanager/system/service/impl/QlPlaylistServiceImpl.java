package com.musicmanager.system.service.impl;

import java.util.List;
import com.musicmanager.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmanager.system.mapper.QlPlaylistMapper;
import com.musicmanager.system.domain.QlPlaylist;
import com.musicmanager.system.service.IQlPlaylistService;

/**
 * 歌单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@Service
public class QlPlaylistServiceImpl implements IQlPlaylistService 
{
    @Autowired
    private QlPlaylistMapper qlPlaylistMapper;

    /**
     * 查询歌单
     * 
     * @param id 歌单主键
     * @return 歌单
     */
    @Override
    public QlPlaylist selectQlPlaylistById(Long id)
    {
        return qlPlaylistMapper.selectQlPlaylistById(id);
    }

    /**
     * 查询歌单列表
     * 
     * @param qlPlaylist 歌单
     * @return 歌单
     */
    @Override
    public List<QlPlaylist> selectQlPlaylistList(QlPlaylist qlPlaylist)
    {
        return qlPlaylistMapper.selectQlPlaylistList(qlPlaylist);
    }

    /**
     * 新增歌单
     * 
     * @param qlPlaylist 歌单
     * @return 结果
     */
    @Override
    public int insertQlPlaylist(QlPlaylist qlPlaylist)
    {
        qlPlaylist.setCreateTime(DateUtils.getNowDate());
        return qlPlaylistMapper.insertQlPlaylist(qlPlaylist);
    }

    /**
     * 修改歌单
     * 
     * @param qlPlaylist 歌单
     * @return 结果
     */
    @Override
    public int updateQlPlaylist(QlPlaylist qlPlaylist)
    {
        qlPlaylist.setUpdateTime(DateUtils.getNowDate());
        return qlPlaylistMapper.updateQlPlaylist(qlPlaylist);
    }

    /**
     * 批量删除歌单
     * 
     * @param ids 需要删除的歌单主键
     * @return 结果
     */
    @Override
    public int deleteQlPlaylistByIds(Long[] ids)
    {
        return qlPlaylistMapper.deleteQlPlaylistByIds(ids);
    }

    /**
     * 删除歌单信息
     * 
     * @param id 歌单主键
     * @return 结果
     */
    @Override
    public int deleteQlPlaylistById(Long id)
    {
        return qlPlaylistMapper.deleteQlPlaylistById(id);
    }
}
