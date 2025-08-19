package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nyx.musicPlatform.BaseTest;
import com.nyx.musicPlatform.entity.Music;
import com.nyx.musicPlatform.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MusicMapper测试类
 * 
 * @author nyx
 * @since 2025-06
 */
@DisplayName("音乐Mapper测试")
class MusicMapperTest extends BaseTest {

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private UserMapper userMapper;

    private Long testUserId;
    private Music testMusic1;
    private Music testMusic2;

    @BeforeEach
    void setUp() {
        // 创建测试用户
        User testUser = createTestUser("testuser", "test@example.com");
        userMapper.insert(testUser);
        testUserId = testUser.getId();

        // 创建测试音乐
        testMusic1 = createTestMusic("Test Song 1", "Test Artist 1", "Test Album 1", testUserId);
        testMusic2 = createTestMusic("Test Song 2", "Test Artist 2", "Test Album 2", testUserId);
        musicMapper.insert(testMusic1);
        musicMapper.insert(testMusic2);
    }

    /**
     * 测试根据用户ID分页查询音乐列表
     */
    @Test
    @DisplayName("should_return_music_list_when_find_by_user_id")
    void should_return_music_list_when_find_by_user_id() {
        // When
        Page<Music> page = new Page<>(1, 10);
        IPage<Music> result = musicMapper.findByUserId(page, testUserId);

        // Then
        assertNotNull(result);
        System.out.println("Expected testUserId: " + testUserId);
        System.out.println("Result size: " + result.getRecords().size());
        
        // 添加调试信息
        result.getRecords().forEach(music -> {
            System.out.println("Music: " + music.getTitle() + ", UserId: " + music.getUserId() + ", ID: " + music.getId());
        });
        
        // 检查插入的测试数据
        System.out.println("TestMusic1 - Title: " + testMusic1.getTitle() + ", UserId: " + testMusic1.getUserId() + ", ID: " + testMusic1.getId());
        System.out.println("TestMusic2 - Title: " + testMusic2.getTitle() + ", UserId: " + testMusic2.getUserId() + ", ID: " + testMusic2.getId());
        
        assertEquals(2, result.getRecords().size());
        
        // 详细检查每个音乐记录
        for (Music music : result.getRecords()) {
            System.out.println("Checking music: " + music.getTitle());
            System.out.println("  - music.getUserId(): " + music.getUserId());
            System.out.println("  - testUserId: " + testUserId);
            System.out.println("  - getUserId() != null: " + (music.getUserId() != null));
            System.out.println("  - equals(testUserId): " + (music.getUserId() != null && music.getUserId().equals(testUserId)));
        }
        
        assertTrue(result.getRecords().stream().allMatch(music -> 
            music.getUserId() != null && music.getUserId().equals(testUserId)));
    }

    /**
     * 测试根据标题模糊查询音乐
     */
    @Test
    @DisplayName("should_return_music_list_when_find_by_title_like")
    void should_return_music_list_when_find_by_title_like() {
        // Given
        Page<Music> page = new Page<>(1, 10);
        String titleKeyword = "Song";

        // When
        IPage<Music> result = musicMapper.findByTitleLike(page, titleKeyword);

        // Then
        assertNotNull(result);
        assertTrue(result.getRecords().size() >= 2);
        assertTrue(result.getRecords().stream().allMatch(music -> music.getTitle().contains(titleKeyword)));
    }

    /**
     * 测试根据艺术家模糊查询音乐
     */
    @Test
    @DisplayName("should_return_music_list_when_find_by_artist_like")
    void should_return_music_list_when_find_by_artist_like() {
        // Given
        Page<Music> page = new Page<>(1, 10);
        String artistKeyword = "Artist";

        // When
        IPage<Music> result = musicMapper.findByArtistLike(page, artistKeyword);

        // Then
        assertNotNull(result);
        assertTrue(result.getRecords().size() >= 2);
        assertTrue(result.getRecords().stream().allMatch(music -> music.getArtist().contains(artistKeyword)));
    }

    /**
     * 测试根据专辑模糊查询音乐
     */
    @Test
    @DisplayName("should_return_music_list_when_find_by_album_like")
    void should_return_music_list_when_find_by_album_like() {
        // Given
        Page<Music> page = new Page<>(1, 10);
        String albumKeyword = "Album";

        // When
        IPage<Music> result = musicMapper.findByAlbumLike(page, albumKeyword);

        // Then
        assertNotNull(result);
        assertTrue(result.getRecords().size() >= 2);
        assertTrue(result.getRecords().stream().allMatch(music -> music.getAlbum().contains(albumKeyword)));
    }

    /**
     * 测试获取热门音乐列表
     */
    @Test
    @DisplayName("should_return_hot_music_list_ordered_by_play_count")
    void should_return_hot_music_list_ordered_by_play_count() {
        // Given
        testMusic1.setPlayCount(100);
        testMusic2.setPlayCount(200);
        musicMapper.updateById(testMusic1);
        musicMapper.updateById(testMusic2);
        Page<Music> page = new Page<>(1, 10);

        // When
        IPage<Music> result = musicMapper.findHotMusic(page);

        // Then
        assertNotNull(result);
        assertTrue(result.getRecords().size() >= 2);
        // 验证按播放次数降序排列
        for (int i = 0; i < result.getRecords().size() - 1; i++) {
            assertTrue(result.getRecords().get(i).getPlayCount() >= result.getRecords().get(i + 1).getPlayCount());
        }
    }

    /**
     * 测试获取最新音乐列表
     */
    @Test
    @DisplayName("should_return_latest_music_list_ordered_by_create_time")
    void should_return_latest_music_list_ordered_by_create_time() {
        // Given
        Page<Music> page = new Page<>(1, 10);

        // When
        IPage<Music> result = musicMapper.findLatestMusic(page);

        // Then
        assertNotNull(result);
        assertTrue(result.getRecords().size() >= 2);
        // 验证按创建时间降序排列
        for (int i = 0; i < result.getRecords().size() - 1; i++) {
            assertTrue(result.getRecords().get(i).getCreateTime().isAfter(result.getRecords().get(i + 1).getCreateTime()) ||
                      result.getRecords().get(i).getCreateTime().equals(result.getRecords().get(i + 1).getCreateTime()));
        }
    }

    /**
     * 测试增加播放次数
     */
    @Test
    @DisplayName("should_increment_play_count_when_music_exists")
    void should_increment_play_count_when_music_exists() {
        // Given
        Integer originalPlayCount = testMusic1.getPlayCount();

        // When
        int result = musicMapper.incrementPlayCount(testMusic1.getId());

        // Then
        assertEquals(1, result);
        Music updatedMusic = musicMapper.selectById(testMusic1.getId());
        assertEquals(originalPlayCount + 1, updatedMusic.getPlayCount());
    }

    /**
     * 测试对不存在的音乐增加播放次数
     */
    @Test
    @DisplayName("should_return_zero_when_increment_play_count_for_non_existing_music")
    void should_return_zero_when_increment_play_count_for_non_existing_music() {
        // When
        int result = musicMapper.incrementPlayCount(99999L);

        // Then
        assertEquals(0, result);
    }

    /**
     * 测试根据文件名查询音乐
     */
    @Test
    @DisplayName("should_return_music_when_find_by_existing_file_name")
    void should_return_music_when_find_by_existing_file_name() {
        // When
        Music foundMusic = musicMapper.findByFileName(testMusic1.getFileName());

        // Then
        assertNotNull(foundMusic);
        assertEquals(testMusic1.getFileName(), foundMusic.getFileName());
        assertEquals(testMusic1.getTitle(), foundMusic.getTitle());
    }

    /**
     * 测试根据不存在的文件名查询音乐
     */
    @Test
    @DisplayName("should_return_null_when_find_by_non_existing_file_name")
    void should_return_null_when_find_by_non_existing_file_name() {
        // When
        Music foundMusic = musicMapper.findByFileName("nonexistent.mp3");

        // Then
        assertNull(foundMusic);
    }

    /**
     * 测试检查音乐是否存在
     */
    @Test
    @DisplayName("should_return_true_when_music_exists_by_title_artist_and_user")
    void should_return_true_when_music_exists_by_title_artist_and_user() {
        // When
        boolean exists = musicMapper.existsByTitleAndArtistAndUserId(
            testMusic1.getTitle(), testMusic1.getArtist(), testUserId);

        // Then
        assertTrue(exists);
    }

    /**
     * 测试检查不存在的音乐
     */
    @Test
    @DisplayName("should_return_false_when_music_not_exists_by_title_artist_and_user")
    void should_return_false_when_music_not_exists_by_title_artist_and_user() {
        // When
        boolean exists = musicMapper.existsByTitleAndArtistAndUserId(
            "Non Existing Song", "Non Existing Artist", testUserId);

        // Then
        assertFalse(exists);
    }

    /**
     * 测试获取用户音乐总数
     */
    @Test
    @DisplayName("should_return_correct_count_when_count_by_user_id")
    void should_return_correct_count_when_count_by_user_id() {
        // When
        long count = musicMapper.countByUserId(testUserId);

        // Then
        assertEquals(2, count);
    }

    /**
     * 测试获取系统音乐总数
     */
    @Test
    @DisplayName("should_return_correct_count_when_count_all")
    void should_return_correct_count_when_count_all() {
        // When
        long count = musicMapper.countAll();

        // Then
        assertTrue(count >= 2);
    }

    /**
     * 测试获取启用状态的音乐总数
     */
    @Test
    @DisplayName("should_return_correct_count_when_count_enabled")
    void should_return_correct_count_when_count_enabled() {
        // When
        long count = musicMapper.countEnabled();

        // Then
        assertTrue(count >= 2);
    }

    /**
     * 测试根据关键词搜索音乐
     */
    @Test
    @DisplayName("should_return_music_list_when_search_by_keyword")
    void should_return_music_list_when_search_by_keyword() {
        // Given
        Page<Music> page = new Page<>(1, 10);
        String keyword = "Test";

        // When
        IPage<Music> result = musicMapper.searchMusic(page, keyword);

        // Then
        assertNotNull(result);
        assertTrue(result.getRecords().size() >= 2);
        assertTrue(result.getRecords().stream().allMatch(music -> 
            music.getTitle().contains(keyword) || 
            music.getArtist().contains(keyword) || 
            music.getAlbum().contains(keyword)));
    }

    /**
     * 测试获取推荐音乐列表
     */
    @Test
    @DisplayName("should_return_recommend_music_list_with_correct_limit")
    void should_return_recommend_music_list_with_correct_limit() {
        // Given
        int limit = 5;

        // When
        List<Music> result = musicMapper.findRecommendMusic(limit);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= limit);
        assertTrue(result.size() >= 2); // 至少有我们创建的两首测试音乐
    }

    /**
     * 测试逻辑删除的音乐不会被查询到
     */
    @Test
    @DisplayName("should_not_find_deleted_music")
    void should_not_find_deleted_music() {
        // Given
        testMusic1.setDeleted(1);
        musicMapper.updateById(testMusic1);

        // When
        Music foundMusic = musicMapper.findByFileName(testMusic1.getFileName());
        Page<Music> page = new Page<>(1, 10);
        IPage<Music> userMusicList = musicMapper.findByUserId(page, testUserId);

        // Then
        assertNull(foundMusic);
        assertEquals(1, userMusicList.getRecords().size()); // 只剩下一首未删除的音乐
    }

    /**
     * 创建测试用户
     */
    private User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("$2a$10$encrypted.password");
        user.setEmail(email);
        user.setNickname("Test User");
        user.setStatus(1);
        user.setDeleted(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }

    /**
     * 创建测试音乐
     */
    private Music createTestMusic(String title, String artist, String album, Long userId) {
        Music music = new Music();
        music.setTitle(title);
        music.setArtist(artist);
        music.setAlbum(album);
        music.setDuration(180);
        String uniqueFileName = java.util.UUID.randomUUID().toString() + ".mp3";
        music.setFilePath("/music/" + uniqueFileName);
        music.setFileName(uniqueFileName);
        music.setOriginalName(title + ".mp3");
        music.setFileSize(5242880L); // 5MB
        music.setFileType("mp3"); // 添加文件类型
        music.setUserId(userId);
        music.setStatus(1);
        music.setPlayCount(0);
        music.setDeleted(0);
        music.setCreateTime(LocalDateTime.now());
        music.setUpdateTime(LocalDateTime.now());
        return music;
    }
}