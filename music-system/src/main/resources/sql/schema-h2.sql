-- H2数据库测试环境初始化脚本

-- 创建用户表
CREATE TABLE IF NOT EXISTS `ql_user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `email` VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar_url` VARCHAR(500) COMMENT '头像URL',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '账号状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除'
) COMMENT='用户表';

-- 创建音乐表
CREATE TABLE IF NOT EXISTS `ql_music` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '音乐ID',
  `title` VARCHAR(200) NOT NULL COMMENT '音乐标题',
  `artist` VARCHAR(200) NOT NULL COMMENT '艺术家',
  `album` VARCHAR(200) COMMENT '专辑',
  `duration` INT COMMENT '音乐时长（秒）',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
  `file_name` VARCHAR(255) NOT NULL UNIQUE COMMENT '文件名（UUID）',
  `original_file_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
  `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
  `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型',
  `upload_user_id` BIGINT NOT NULL COMMENT '上传用户ID',
  `play_count` BIGINT NOT NULL DEFAULT 0 COMMENT '播放次数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '音乐状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
  FOREIGN KEY (`upload_user_id`) REFERENCES `ql_user`(`id`)
) COMMENT='音乐表';

-- 创建索引
CREATE INDEX IF NOT EXISTS `idx_music_upload_user_id` ON `ql_music`(`upload_user_id`);
CREATE INDEX IF NOT EXISTS `idx_music_title` ON `ql_music`(`title`);
CREATE INDEX IF NOT EXISTS `idx_music_artist` ON `ql_music`(`artist`);
CREATE INDEX IF NOT EXISTS `idx_music_create_time` ON `ql_music`(`create_time`);
CREATE INDEX IF NOT EXISTS `idx_user_username` ON `ql_user`(`username`);
CREATE INDEX IF NOT EXISTS `idx_user_email` ON `ql_user`(`email`);