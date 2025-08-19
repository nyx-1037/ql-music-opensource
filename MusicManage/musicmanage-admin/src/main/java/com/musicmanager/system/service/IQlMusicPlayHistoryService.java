package com.musicmanager.system.service;

import java.util.List;
import com.musicmanager.system.domain.QlMusicPlayHistory;

/**
 * 音乐播放历史Service接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface IQlMusicPlayHistoryService 
{
    /**
     * 查询音乐播放历史
     * 
     * @param id 音乐播放历史主键
     * @return 音乐播放历史
     */
    public QlMusicPlayHistory selectQlMusicPlayHistoryById(Long id);

    /**
     * 查询音乐播放历史列表
     * 
     * @param qlMusicPlayHistory 音乐播放历史
     * @return 音乐播放历史集合
     */
    public List<QlMusicPlayHistory> selectQlMusicPlayHistoryList(QlMusicPlayHistory qlMusicPlayHistory);

    /**
     * 新增音乐播放历史
     * 
     * @param qlMusicPlayHistory 音乐播放历史
     * @return 结果
     */
    public int insertQlMusicPlayHistory(QlMusicPlayHistory qlMusicPlayHistory);

    /**
     * 修改音乐播放历史
     * 
     * @param qlMusicPlayHistory 音乐播放历史
     * @return 结果
     */
    public int updateQlMusicPlayHistory(QlMusicPlayHistory qlMusicPlayHistory);

    /**
     * 批量删除音乐播放历史
     * 
     * @param ids 需要删除的音乐播放历史主键集合
     * @return 结果
     */
    public int deleteQlMusicPlayHistoryByIds(Long[] ids);

    /**
     * 删除音乐播放历史信息
     * 
     * @param id 音乐播放历史主键
     * @return 结果
     */
    public int deleteQlMusicPlayHistoryById(Long id);
}
