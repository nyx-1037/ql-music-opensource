package com.musicmanager.system.mapper;

import java.util.List;
import com.musicmanager.system.domain.QlMusic;

/**
 * 音乐Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface QlMusicMapper 
{
    /**
     * 查询音乐
     * 
     * @param id 音乐主键
     * @return 音乐
     */
    public QlMusic selectQlMusicById(Long id);

    /**
     * 查询音乐列表
     * 
     * @param qlMusic 音乐
     * @return 音乐集合
     */
    public List<QlMusic> selectQlMusicList(QlMusic qlMusic);

    /**
     * 新增音乐
     * 
     * @param qlMusic 音乐
     * @return 结果
     */
    public int insertQlMusic(QlMusic qlMusic);

    /**
     * 修改音乐
     * 
     * @param qlMusic 音乐
     * @return 结果
     */
    public int updateQlMusic(QlMusic qlMusic);

    /**
     * 删除音乐
     * 
     * @param id 音乐主键
     * @return 结果
     */
    public int deleteQlMusicById(Long id);

    /**
     * 批量删除音乐
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQlMusicByIds(Long[] ids);
}
