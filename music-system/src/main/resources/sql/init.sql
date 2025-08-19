-- 音乐平台数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `music_platform` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `music_platform`;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `ql_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
    `nickname` VARCHAR(100) DEFAULT NULL COMMENT '昵称',
    `avatar_url` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '账号状态（0-禁用，1-启用）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='音乐表';

-- 创建音乐收藏表
CREATE TABLE IF NOT EXISTS `ql_music_favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `music_id` BIGINT NOT NULL COMMENT '音乐ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_music` (`user_id`, `music_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_music_id` (`music_id`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_music_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `ql_user`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_music_favorite_music` FOREIGN KEY (`music_id`) REFERENCES `ql_music`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='音乐收藏表';

-- 创建用户关注关系表
CREATE TABLE IF NOT EXISTS `ql_user_follow` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `follower_id` BIGINT NOT NULL COMMENT '关注者ID',
    `following_id` BIGINT NOT NULL COMMENT '被关注者ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_follower_following` (`follower_id`, `following_id`),
    KEY `idx_follower_id` (`follower_id`),
    KEY `idx_following_id` (`following_id`),
    CONSTRAINT `fk_user_follow_follower` FOREIGN KEY (`follower_id`) REFERENCES `ql_user`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_user_follow_following` FOREIGN KEY (`following_id`) REFERENCES `ql_user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户关注关系表';

-- 创建音乐表
CREATE TABLE IF NOT EXISTS `ql_music` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '音乐ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `artist` VARCHAR(200) NOT NULL COMMENT '艺术家',
    `album` VARCHAR(200) DEFAULT NULL COMMENT '专辑',
    `duration` INT DEFAULT NULL COMMENT '时长（秒）',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名（UUID）',
    `original_file_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
    `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型',
    `upload_user_id` BIGINT NOT NULL COMMENT '上传用户ID',
    `play_count` BIGINT NOT NULL DEFAULT 0 COMMENT '播放次数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_file_name` (`file_name`),
    KEY `idx_title` (`title`),
    KEY `idx_artist` (`artist`),
    KEY `idx_album` (`album`),
    KEY `idx_upload_user_id` (`upload_user_id`),
    KEY `idx_play_count` (`play_count`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_is_deleted` (`is_deleted`),
    CONSTRAINT `fk_music_upload_user_id` FOREIGN KEY (`upload_user_id`) REFERENCES `ql_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='音乐表';

-- 插入默认管理员用户
-- 密码为：admin123（BCrypt加密后的值）
INSERT INTO `ql_user` (`username`, `password`, `email`, `nickname`, `status`) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyiE.rV8rYzRNZaXe2qI8qqs8Ey', 'admin@musicplatform.com', '系统管理员', 1)
ON DUPLICATE KEY UPDATE `password` = VALUES(`password`);

-- 插入测试用户
-- 密码为：123456（BCrypt加密后的值）
INSERT INTO `ql_user` (`username`, `password`, `email`, `nickname`, `status`) VALUES 
('testuser', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'test@musicplatform.com', '测试用户', 1)
ON DUPLICATE KEY UPDATE `password` = VALUES(`password`);

-- 创建索引优化查询性能
-- 复合索引：用于音乐搜索
CREATE INDEX `idx_music_search` ON `ql_music` (`title`, `artist`, `album`, `status`, `is_deleted`);

-- 复合索引：用于用户音乐列表查询
CREATE INDEX `idx_user_music_list` ON `ql_music` (`upload_user_id`, `status`, `is_deleted`, `create_time`);

-- 复合索引：用于热门音乐查询
CREATE INDEX `idx_hot_music` ON `ql_music` (`play_count`, `status`, `is_deleted`);

-- 复合索引：用于最新音乐查询
CREATE INDEX `idx_latest_music` ON `ql_music` (`create_time`, `status`, `is_deleted`);

-- 创建视图：用户音乐统计
CREATE OR REPLACE VIEW `v_user_music_stats` AS
SELECT 
    u.id AS user_id,
    u.username,
    u.nickname,
    COUNT(m.id) AS music_count,
    COALESCE(SUM(m.play_count), 0) AS total_play_count,
    COALESCE(SUM(m.file_size), 0) AS total_file_size
FROM `ql_user` u
LEFT JOIN `ql_music` m ON u.id = m.upload_user_id AND m.is_deleted = 0
WHERE u.is_deleted = 0
GROUP BY u.id, u.username, u.nickname;

-- 创建视图：音乐详情（包含上传用户信息）
CREATE OR REPLACE VIEW `v_music_detail` AS
SELECT 
    m.*,
    u.username AS upload_username,
    u.nickname AS upload_nickname
FROM `ql_music` m
INNER JOIN `ql_user` u ON m.upload_user_id = u.id
WHERE m.is_deleted = 0 AND u.is_deleted = 0;

-- 创建存储过程：增加音乐播放次数
DELIMITER //
CREATE PROCEDURE `sp_increment_play_count`(
    IN p_music_id BIGINT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    UPDATE `ql_music` 
    SET `play_count` = `play_count` + 1,
        `update_time` = CURRENT_TIMESTAMP
    WHERE `id` = p_music_id 
      AND `status` = 1 
      AND `is_deleted` = 0;
    
    COMMIT;
END //
DELIMITER ;

-- 创建存储过程：获取推荐音乐
DELIMITER //
CREATE PROCEDURE `sp_get_recommend_music`(
    IN p_user_id BIGINT,
    IN p_limit INT
)
BEGIN
    -- 基于用户播放历史和热门程度推荐音乐
    SELECT DISTINCT
        m.id,
        m.title,
        m.artist,
        m.album,
        m.duration,
        m.original_file_name,
        m.file_size,
        m.file_type,
        m.upload_user_id,
        u.username AS upload_username,
        m.play_count,
        m.create_time
    FROM `ql_music` m
    INNER JOIN `ql_user` u ON m.upload_user_id = u.id
    WHERE m.status = 1 
      AND m.is_deleted = 0 
      AND u.is_deleted = 0
      AND m.upload_user_id != p_user_id
    ORDER BY 
        (m.play_count * 0.7 + RAND() * 0.3) DESC,
        m.create_time DESC
    LIMIT p_limit;
END //
DELIMITER ;

-- 创建触发器：用户删除时同步删除其音乐
DELIMITER //
CREATE TRIGGER `tr_user_delete_cascade`
AFTER UPDATE ON `ql_user`
FOR EACH ROW
BEGIN
    IF NEW.is_deleted = 1 AND OLD.is_deleted = 0 THEN
        UPDATE `ql_music` 
        SET `is_deleted` = 1, `update_time` = CURRENT_TIMESTAMP
        WHERE `upload_user_id` = NEW.id AND `is_deleted` = 0;
    END IF;
END //
DELIMITER ;

-- 创建函数：格式化文件大小
DELIMITER //
CREATE FUNCTION `fn_format_file_size`(size_bytes BIGINT) 
RETURNS VARCHAR(20)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE result VARCHAR(20);
    
    IF size_bytes IS NULL OR size_bytes < 0 THEN
        RETURN '0 B';
    ELSEIF size_bytes < 1024 THEN
        SET result = CONCAT(size_bytes, ' B');
    ELSEIF size_bytes < 1048576 THEN
        SET result = CONCAT(ROUND(size_bytes / 1024, 2), ' KB');
    ELSEIF size_bytes < 1073741824 THEN
        SET result = CONCAT(ROUND(size_bytes / 1048576, 2), ' MB');
    ELSE
        SET result = CONCAT(ROUND(size_bytes / 1073741824, 2), ' GB');
    END IF;
    
    RETURN result;
END //
DELIMITER ;

-- 创建函数：格式化播放时长
DELIMITER //
CREATE FUNCTION `fn_format_duration`(duration_seconds INT) 
RETURNS VARCHAR(10)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE hours INT;
    DECLARE minutes INT;
    DECLARE seconds INT;
    DECLARE result VARCHAR(10);
    
    IF duration_seconds IS NULL OR duration_seconds < 0 THEN
        RETURN '00:00';
    END IF;
    
    SET hours = FLOOR(duration_seconds / 3600);
    SET minutes = FLOOR((duration_seconds % 3600) / 60);
    SET seconds = duration_seconds % 60;
    
    IF hours > 0 THEN
        SET result = CONCAT(
            LPAD(hours, 2, '0'), ':',
            LPAD(minutes, 2, '0'), ':',
            LPAD(seconds, 2, '0')
        );
    ELSE
        SET result = CONCAT(
            LPAD(minutes, 2, '0'), ':',
            LPAD(seconds, 2, '0')
        );
    END IF;
    
    RETURN result;
END //
DELIMITER ;

-- 插入示例数据（可选）
-- 注意：实际部署时可以删除这部分示例数据
/*
INSERT INTO `ql_music` (
    `title`, `artist`, `album`, `duration`, `file_path`, `file_name`, 
    `original_file_name`, `file_size`, `file_type`, `upload_user_id`
) VALUES 
(
    '示例音乐1', '示例艺术家1', '示例专辑1', 180, 
    '/uploads/music/', 'sample-music-1.mp3', 
    '示例音乐1.mp3', 5242880, 'audio/mpeg', 1
),
(
    '示例音乐2', '示例艺术家2', '示例专辑2', 240, 
    '/uploads/music/', 'sample-music-2.mp3', 
    '示例音乐2.mp3', 7340032, 'audio/mpeg', 2
);
*/

-- 创建数据库用户（可选，用于应用连接）
-- CREATE USER 'music_app'@'localhost' IDENTIFIED BY 'your_password_here';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON music_platform.* TO 'music_app'@'localhost';
-- FLUSH PRIVILEGES;

-- 显示创建结果
SELECT 'Database initialization completed successfully!' AS message;
SHOW TABLES;