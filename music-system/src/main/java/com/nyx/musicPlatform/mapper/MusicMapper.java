package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nyx.musicPlatform.entity.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 音乐Mapper接口
 * 
 * @author nyx
 * @since 2025-06
 */
@Mapper
public interface MusicMapper extends BaseMapper<Music> {

    /**
     * 根据用户ID分页查询音乐列表
     * @param page 分页对象
     * @param userId 用户ID
     * @return 音乐分页列表
     */
    @Select("SELECT id, title, artist, album, duration, file_path, file_name, " +
            "original_file_name, file_size, file_type, upload_user_id as userId, " +
            "play_count, status, create_time, update_time, is_deleted " +
            "FROM ql_music WHERE upload_user_id = #{userId} AND is_deleted = 0 ORDER BY create_time DESC")
    IPage<Music> findByUserId(Page<Music> page, @Param("userId") Long userId);

    /**
     * 根据标题模糊查询音乐
     * @param page 分页对象
     * @param title 音乐标题
     * @return 音乐分页列表
     */
    @Select("SELECT * FROM ql_music WHERE title LIKE CONCAT('%', #{title}, '%') AND status = 1 AND is_deleted = 0 ORDER BY create_time DESC")
    IPage<Music> findByTitleLike(Page<Music> page, @Param("title") String title);

    /**
     * 根据艺术家模糊查询音乐
     * @param page 分页对象
     * @param artist 艺术家
     * @return 音乐分页列表
     */
    @Select("SELECT * FROM ql_music WHERE artist LIKE CONCAT('%', #{artist}, '%') AND status = 1 AND is_deleted = 0 ORDER BY create_time DESC")
    IPage<Music> findByArtistLike(Page<Music> page, @Param("artist") String artist);

    /**
     * 根据专辑模糊查询音乐
     * @param page 分页对象
     * @param album 专辑
     * @return 音乐分页列表
     */
    @Select("SELECT * FROM ql_music WHERE album LIKE CONCAT('%', #{album}, '%') AND status = 1 AND is_deleted = 0 ORDER BY create_time DESC")
    IPage<Music> findByAlbumLike(Page<Music> page, @Param("album") String album);

    /**
     * 获取热门音乐列表（按播放次数排序）
     * @param page 分页对象
     * @return 热门音乐分页列表
     */
    @Select("SELECT * FROM ql_music WHERE status = 1 AND is_deleted = 0 ORDER BY play_count DESC, create_time DESC")
    IPage<Music> findHotMusic(Page<Music> page);

    /**
     * 获取最新音乐列表
     * @param page 分页对象
     * @return 最新音乐分页列表
     */
    @Select("SELECT * FROM ql_music WHERE status = 1 AND is_deleted = 0 ORDER BY create_time DESC")
    IPage<Music> findLatestMusic(Page<Music> page);

    /**
     * 增加播放次数
     * @param musicId 音乐ID
     * @return 更新行数
     */
    @Update("UPDATE ql_music SET play_count = play_count + 1 WHERE id = #{musicId} AND is_deleted = 0")
    int incrementPlayCount(@Param("musicId") Long musicId);

    /**
     * 根据文件名查询音乐
     * @param fileName 文件名
     * @return 音乐信息
     */
    @Select("SELECT * FROM ql_music WHERE file_name = #{fileName} AND is_deleted = 0")
    Music findByFileName(@Param("fileName") String fileName);

    /**
     * 检查音乐是否存在（根据标题和艺术家）
     * @param title 音乐标题
     * @param artist 艺术家
     * @param userId 用户ID
     * @return 存在返回true，不存在返回false
     */
    @Select("SELECT COUNT(*) > 0 FROM ql_music WHERE title = #{title} AND artist = #{artist} AND upload_user_id = #{userId} AND is_deleted = 0")
    boolean existsByTitleAndArtistAndUserId(@Param("title") String title, @Param("artist") String artist, @Param("userId") Long userId);

    /**
     * 获取用户音乐总数
     * @param userId 用户ID
     * @return 音乐总数
     */
    @Select("SELECT COUNT(*) FROM ql_music WHERE upload_user_id = #{userId} AND is_deleted = 0")
    long countByUserId(@Param("userId") Long userId);

    /**
     * 获取系统音乐总数
     * @return 音乐总数
     */
    @Select("SELECT COUNT(*) FROM ql_music WHERE is_deleted = 0")
    long countAll();

    /**
     * 获取启用状态的音乐总数
     * @return 启用音乐总数
     */
    @Select("SELECT COUNT(*) FROM ql_music WHERE status = 1 AND is_deleted = 0")
    long countEnabled();

    /**
     * 根据关键词搜索音乐（标题、艺术家、专辑）
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @return 音乐分页列表
     */
    @Select("SELECT * FROM ql_music WHERE (title LIKE CONCAT('%', #{keyword}, '%') OR artist LIKE CONCAT('%', #{keyword}, '%') OR album LIKE CONCAT('%', #{keyword}, '%')) AND status = 1 AND is_deleted = 0 ORDER BY play_count DESC, create_time DESC")
    IPage<Music> searchMusic(Page<Music> page, @Param("keyword") String keyword);

    /**
     * 获取推荐音乐列表（随机推荐）
     * @param limit 限制数量
     * @return 推荐音乐列表
     */
    @Select("SELECT * FROM ql_music WHERE status = 1 AND is_deleted = 0 ORDER BY RAND() LIMIT #{limit}")
    List<Music> findRecommendMusic(@Param("limit") int limit);
    
    /**
     * 获取总播放次数
     * @return 总播放次数
     */
    @Select("SELECT COALESCE(SUM(play_count), 0) FROM ql_music WHERE is_deleted = 0")
    long getTotalPlayCount();
    
    /**
     * 获取总点赞数
     * @return 总点赞数
     */
    @Select("SELECT COALESCE(SUM(like_count), 0) FROM ql_music WHERE is_deleted = 0")
    long getTotalLikeCount();
    
    /**
     * 获取音乐类型统计
     * @return 类型统计列表
     */
    @Select("SELECT genre as name, COUNT(*) as count FROM ql_music WHERE status = 1 AND is_deleted = 0 AND genre IS NOT NULL GROUP BY genre ORDER BY count DESC")
    List<Map<String, Object>> getGenreStats();
}