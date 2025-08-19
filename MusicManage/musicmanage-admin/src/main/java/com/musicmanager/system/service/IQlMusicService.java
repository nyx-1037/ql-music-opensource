package com.musicmanager.system.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.musicmanager.system.domain.QlMusic;

/**
 * 音乐Service接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface IQlMusicService 
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
     * 批量删除音乐
     * 
     * @param ids 需要删除的音乐主键集合
     * @return 结果
     */
    public int deleteQlMusicByIds(Long[] ids);

    /**
     * 删除音乐信息
     * 
     * @param id 音乐主键
     * @return 结果
     */
    public int deleteQlMusicById(Long id);

    /**
     * 上传音乐文件
     * 
     * @param file 音乐文件
     * @param title 音乐标题
     * @param artist 艺术家
     * @param album 专辑
     * @param cover 封面图片
     * @param genre 音乐流派
     * @param releaseYear 发布年份
     * @param description 音乐描述
     * @param tags 标签
     * @param isPublic 是否公开
     * @param allowDownload 是否允许下载
     * @param allowComment 是否允许评论
     * @param uploadUserId 上传用户ID
     * @return 音乐信息
     */
    public QlMusic uploadMusic(MultipartFile file, String title, String artist, String album, 
            MultipartFile cover, String genre, String releaseYear, String description, 
            String tags, Integer isPublic, Integer allowDownload, Integer allowComment, Long uploadUserId) throws Exception;
}
