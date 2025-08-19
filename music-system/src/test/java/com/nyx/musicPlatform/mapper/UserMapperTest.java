package com.nyx.musicPlatform.mapper;

import com.nyx.musicPlatform.BaseTest;
import com.nyx.musicPlatform.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserMapper测试类
 * 
 * @author nyx
 * @since 2025-06
 */
@DisplayName("用户Mapper测试")
class UserMapperTest extends BaseTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * 测试根据用户名查询用户
     */
    @Test
    @DisplayName("should_return_user_when_find_by_existing_username")
    void should_return_user_when_find_by_existing_username() {
        // Given
        User user = createTestUser("testuser", "test@example.com");
        userMapper.insert(user);

        // When
        User foundUser = userMapper.findByUsername("testuser");

        // Then
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        assertEquals("test@example.com", foundUser.getEmail());
    }

    /**
     * 测试根据不存在的用户名查询用户
     */
    @Test
    @DisplayName("should_return_null_when_find_by_non_existing_username")
    void should_return_null_when_find_by_non_existing_username() {
        // When
        User foundUser = userMapper.findByUsername("nonexistentuser");

        // Then
        assertNull(foundUser);
    }

    /**
     * 测试根据邮箱查询用户
     */
    @Test
    @DisplayName("should_return_user_when_find_by_existing_email")
    void should_return_user_when_find_by_existing_email() {
        // Given
        User user = createTestUser("testuser2", "test2@example.com");
        userMapper.insert(user);

        // When
        User foundUser = userMapper.findByEmail("test2@example.com");

        // Then
        assertNotNull(foundUser);
        assertEquals("testuser2", foundUser.getUsername());
        assertEquals("test2@example.com", foundUser.getEmail());
    }

    /**
     * 测试根据不存在的邮箱查询用户
     */
    @Test
    @DisplayName("should_return_null_when_find_by_non_existing_email")
    void should_return_null_when_find_by_non_existing_email() {
        // When
        User foundUser = userMapper.findByEmail("nonexistent@example.com");

        // Then
        assertNull(foundUser);
    }

    /**
     * 测试检查用户名是否存在
     */
    @Test
    @DisplayName("should_return_true_when_username_exists")
    void should_return_true_when_username_exists() {
        // Given
        User user = createTestUser("existinguser", "existing@example.com");
        userMapper.insert(user);

        // When
        boolean exists = userMapper.existsByUsername("existinguser");

        // Then
        assertTrue(exists);
    }

    /**
     * 测试检查不存在的用户名
     */
    @Test
    @DisplayName("should_return_false_when_username_not_exists")
    void should_return_false_when_username_not_exists() {
        // When
        boolean exists = userMapper.existsByUsername("nonexistentuser");

        // Then
        assertFalse(exists);
    }

    /**
     * 测试检查邮箱是否存在
     */
    @Test
    @DisplayName("should_return_true_when_email_exists")
    void should_return_true_when_email_exists() {
        // Given
        User user = createTestUser("testuser3", "existing2@example.com");
        userMapper.insert(user);

        // When
        boolean exists = userMapper.existsByEmail("existing2@example.com");

        // Then
        assertTrue(exists);
    }

    /**
     * 测试检查不存在的邮箱
     */
    @Test
    @DisplayName("should_return_false_when_email_not_exists")
    void should_return_false_when_email_not_exists() {
        // When
        boolean exists = userMapper.existsByEmail("nonexistent2@example.com");

        // Then
        assertFalse(exists);
    }

    /**
     * 测试根据用户ID查询用户状态
     */
    @Test
    @DisplayName("should_return_status_when_get_user_status_by_existing_id")
    void should_return_status_when_get_user_status_by_existing_id() {
        // Given
        User user = createTestUser("testuser4", "test4@example.com");
        user.setStatus(1);
        userMapper.insert(user);

        // When
        Integer status = userMapper.getUserStatus(user.getId());

        // Then
        assertNotNull(status);
        assertEquals(1, status);
    }

    /**
     * 测试根据不存在的用户ID查询用户状态
     */
    @Test
    @DisplayName("should_return_null_when_get_user_status_by_non_existing_id")
    void should_return_null_when_get_user_status_by_non_existing_id() {
        // When
        Integer status = userMapper.getUserStatus(99999L);

        // Then
        assertNull(status);
    }

    /**
     * 测试逻辑删除的用户不会被查询到
     */
    @Test
    @DisplayName("should_not_find_deleted_user")
    void should_not_find_deleted_user() {
        // Given
        User user = createTestUser("deleteduser", "deleted@example.com");
        user.setDeleted(1); // 设置为已删除
        userMapper.insert(user);

        // When
        User foundUser = userMapper.findByUsername("deleteduser");
        boolean exists = userMapper.existsByUsername("deleteduser");

        // Then
        assertNull(foundUser);
        assertFalse(exists);
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
}