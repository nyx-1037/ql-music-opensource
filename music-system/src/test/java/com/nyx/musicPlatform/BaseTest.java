package com.nyx.musicPlatform;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试基类
 * 提供通用的测试配置和注解
 * 
 * @author nyx
 * @since 2025-06
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MusicPlatformApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
public abstract class BaseTest {
    
    /**
     * 测试用户名前缀
     */
    protected static final String TEST_USERNAME_PREFIX = "test_user_";
    
    /**
     * 测试邮箱前缀
     */
    protected static final String TEST_EMAIL_PREFIX = "test_";
    
    /**
     * 测试邮箱后缀
     */
    protected static final String TEST_EMAIL_SUFFIX = "@example.com";
    
    /**
     * 测试音乐标题前缀
     */
    protected static final String TEST_MUSIC_TITLE_PREFIX = "Test Song ";
    
    /**
     * 测试艺术家前缀
     */
    protected static final String TEST_ARTIST_PREFIX = "Test Artist ";
    
    /**
     * 测试专辑前缀
     */
    protected static final String TEST_ALBUM_PREFIX = "Test Album ";
    
    /**
     * 生成测试用户名
     * @param suffix 后缀
     * @return 测试用户名
     */
    protected String generateTestUsername(String suffix) {
        return TEST_USERNAME_PREFIX + suffix;
    }
    
    /**
     * 生成测试邮箱
     * @param prefix 前缀
     * @return 测试邮箱
     */
    protected String generateTestEmail(String prefix) {
        return TEST_EMAIL_PREFIX + prefix + TEST_EMAIL_SUFFIX;
    }
    
    /**
     * 生成测试音乐标题
     * @param suffix 后缀
     * @return 测试音乐标题
     */
    protected String generateTestMusicTitle(String suffix) {
        return TEST_MUSIC_TITLE_PREFIX + suffix;
    }
    
    /**
     * 生成测试艺术家名称
     * @param suffix 后缀
     * @return 测试艺术家名称
     */
    protected String generateTestArtist(String suffix) {
        return TEST_ARTIST_PREFIX + suffix;
    }
    
    /**
     * 生成测试专辑名称
     * @param suffix 后缀
     * @return 测试专辑名称
     */
    protected String generateTestAlbum(String suffix) {
        return TEST_ALBUM_PREFIX + suffix;
    }
    
    /**
     * 生成唯一的时间戳后缀
     * @return 时间戳字符串
     */
    protected String generateTimestampSuffix() {
        return String.valueOf(System.currentTimeMillis());
    }
}