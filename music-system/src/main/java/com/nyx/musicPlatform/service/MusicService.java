package com.nyx.musicPlatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nyx.musicPlatform.entity.Music;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 音乐服务接口
 * 
 * @author nyx
 * @since 2025-06
 */
public interface MusicService extends IService<Music> {

    /**
     * 上传音乐文件
     * @param file 音乐文件
     * @param title 音乐标题
     * @param artist 艺术家
     * @param album 专辑
     * @param cover 封面图片
     * @param genre 音乐流派
     * @param releaseYear 发布年份
     * @param description 音乐描述
     * @param tags 音乐标签
     * @param isPublic 是否公开
     * @param allowDownload 允许下载
     * @param allowComment 允许评论
     * @param userId 上传用户ID
     * @return 上传结果
     */
    Music uploadMusic(MultipartFile file, String title, String artist, String album, 
                     MultipartFile cover, String genre, String releaseYear, String description, 
                     String tags, Boolean isPublic, Boolean allowDownload, Boolean allowComment, Long userId);

    /**
     * 根据用户ID分页查询音乐列表
     * @param page 分页对象
     * @param userId 用户ID
     * @return 音乐分页列表
     */
    IPage<Music> getMusicByUserId(Page<Music> page, Long userId);

    /**
     * 获取音乐列表
     * @param page 分页对象
     * @return 音乐分页列表
     */
    IPage<Music> getMusicList(Page<Music> page);

    /**
     * 根据标题模糊查询音乐
     * @param page 分页对象
     * @param title 音乐标题
     * @return 音乐分页列表
     */
    IPage<Music> searchByTitle(Page<Music> page, String title);

    /**
     * 根据艺术家模糊查询音乐
     * @param page 分页对象
     * @param artist 艺术家
     * @return 音乐分页列表
     */
    IPage<Music> searchByArtist(Page<Music> page, String artist);

    /**
     * 根据专辑模糊查询音乐
     * @param page 分页对象
     * @param album 专辑
     * @return 音乐分页列表
     */
    IPage<Music> searchByAlbum(Page<Music> page, String album);

    /**
     * 获取热门音乐列表
     * @param page 分页对象
     * @return 热门音乐分页列表
     */
    IPage<Music> getHotMusic(Page<Music> page);

    /**
     * 获取最新音乐列表
     * @param page 分页对象
     * @return 最新音乐分页列表
     */
    IPage<Music> getLatestMusic(Page<Music> page);

    /**
     * 获取带筛选条件的音乐列表
     * @param page 分页对象
     * @param sort 排序方式
     * @param genre 音乐流派
     * @param duration 时长筛选
     * @param search 搜索关键词
     * @return 音乐分页列表
     */
    IPage<Music> getMusicListWithFilter(Page<Music> page, String sort, String genre, String duration, String search);

    /**
     * 获取所有音乐流派
     * @return 流派列表
     */
    List<String> getAllGenres();

    /**
     * 播放音乐（增加播放次数）
     * @param musicId 音乐ID
     * @return 操作结果
     */
    boolean playMusic(Long musicId);

    /**
     * 删除音乐（逻辑删除）
     * @param musicId 音乐ID
     * @param userId 用户ID（权限验证）
     * @return 删除结果
     */
    boolean deleteMusic(Long musicId, Long userId);

    /**
     * 批量删除音乐（逻辑删除）
     * @param musicIds 音乐ID列表
     * @param userId 用户ID（权限验证）
     * @return 删除结果
     */
    boolean deleteMusicBatch(List<Long> musicIds, Long userId);

    /**
     * 更新音乐信息
     * @param musicId 音乐ID
     * @param title 音乐标题
     * @param artist 艺术家
     * @param album 专辑
     * @param userId 用户ID（权限验证）
     * @return 更新结果
     */
    boolean updateMusicInfo(Long musicId, String title, String artist, String album, Long userId);

    /**
     * 启用/禁用音乐
     * @param musicId 音乐ID
     * @param status 状态（0-禁用，1-启用）
     * @return 操作结果
     */
    boolean updateMusicStatus(Long musicId, Integer status);

    /**
     * 根据文件名查询音乐
     * @param fileName 文件名
     * @return 音乐信息
     */
    Music getMusicByFileName(String fileName);

    /**
     * 检查音乐是否存在
     * @param title 音乐标题
     * @param artist 艺术家
     * @param userId 用户ID
     * @return 存在返回true，不存在返回false
     */
    boolean existsByTitleAndArtist(String title, String artist, Long userId);

    /**
     * 获取用户音乐总数
     * @param userId 用户ID
     * @return 音乐总数
     */
    long getUserMusicCount(Long userId);

    /**
     * 获取系统音乐总数
     * @return 音乐总数
     */
    long getTotalMusicCount();

    /**
     * 根据关键词搜索音乐
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @return 音乐分页列表
     */
    IPage<Music> searchMusic(Page<Music> page, String keyword);

    /**
     * 获取推荐音乐列表
     * @param limit 限制数量
     * @return 推荐音乐列表
     */
    List<Music> getRecommendMusic(int limit);

    /**
     * 获取随机推荐音乐列表
     * @param page 分页对象
     * @return 随机推荐音乐分页列表
     */
    IPage<Music> getRandomRecommendMusic(Page<Music> page);

    /**
     * 获取音乐文件流
     * @param musicId 音乐ID
     * @return 文件字节数组
     */
    byte[] getMusicFile(Long musicId);

    /**
     * 获取音乐文件路径
     * @param musicId 音乐ID
     * @return 文件路径
     */
    String getMusicFilePath(Long musicId);

    /**
     * 验证文件格式
     * @param file 文件
     * @return 验证结果
     */
    boolean validateFileFormat(MultipartFile file);

    /**
     * 验证文件大小
     * @param file 文件
     * @return 验证结果
     */
    boolean validateFileSize(MultipartFile file);

    /**
     * 生成唯一文件名
     * @param originalFilename 原始文件名
     * @return UUID文件名
     */
    String generateUniqueFileName(String originalFilename);



    /**
     * 获取音乐统计信息
     * @return 音乐统计信息
     */
    Object getMusicStatistics();

    /**
     * 上传音乐封面图片
     * @param musicId 音乐ID
     * @param file 封面图片文件
     * @param userId 用户ID（权限验证）
     * @return 封面图片保存路径
     */
    String uploadMusicCover(Long musicId, MultipartFile file, Long userId);

    /**
     * 获取音乐封面图片路径
     * @param musicId 音乐ID
     * @return 封面图片路径
     */
    String getMusicCoverPath(Long musicId);
}