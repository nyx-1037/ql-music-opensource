package com.musicmanager.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmanager.system.mapper.QlMusicPlayHistoryMapper;
import com.musicmanager.system.domain.QlMusicPlayHistory;
import com.musicmanager.system.service.IQlMusicPlayHistoryService;

/**
 * 音乐播放历史Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@Service
public class QlMusicPlayHistoryServiceImpl implements IQlMusicPlayHistoryService 
{
    @Autowired
    private QlMusicPlayHistoryMapper qlMusicPlayHistoryMapper;

    /**
     * 查询音乐播放历史
     * 
     * @param id 音乐播放历史主键
     * @return 音乐播放历史
     */
    @Override
    public QlMusicPlayHistory selectQlMusicPlayHistoryById(Long id)
    {
        return qlMusicPlayHistoryMapper.selectQlMusicPlayHistoryById(id);
    }

    /**
     * 查询音乐播放历史列表
     * 
     * @param qlMusicPlayHistory 音乐播放历史
     * @return 音乐播放历史
     */
    @Override
    public List<QlMusicPlayHistory> selectQlMusicPlayHistoryList(QlMusicPlayHistory qlMusicPlayHistory)
    {
        return qlMusicPlayHistoryMapper.selectQlMusicPlayHistoryList(qlMusicPlayHistory);
    }

    /**
     * 新增音乐播放历史
     * 
     * @param qlMusicPlayHistory 音乐播放历史
     * @return 结果
     */
    @Override
    public int insertQlMusicPlayHistory(QlMusicPlayHistory qlMusicPlayHistory)
    {
        return qlMusicPlayHistoryMapper.insertQlMusicPlayHistory(qlMusicPlayHistory);
    }

    /**
     * 修改音乐播放历史
     * 
     * @param qlMusicPlayHistory 音乐播放历史
     * @return 结果
     */
    @Override
    public int updateQlMusicPlayHistory(QlMusicPlayHistory qlMusicPlayHistory)
    {
        return qlMusicPlayHistoryMapper.updateQlMusicPlayHistory(qlMusicPlayHistory);
    }

    /**
     * 批量删除音乐播放历史
     * 
     * @param ids 需要删除的音乐播放历史主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicPlayHistoryByIds(Long[] ids)
    {
        return qlMusicPlayHistoryMapper.deleteQlMusicPlayHistoryByIds(ids);
    }

    /**
     * 删除音乐播放历史信息
     * 
     * @param id 音乐播放历史主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicPlayHistoryById(Long id)
    {
        return qlMusicPlayHistoryMapper.deleteQlMusicPlayHistoryById(id);
    }
}
