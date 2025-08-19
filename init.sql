/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80036
 Source Host           : localhost:3306
 Source Schema         : music_platform

 Target Server Type    : MySQL
 Target Server Version : 80036
 File Encoding         : 65001

 Date: 19/08/2025 22:28:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (1, 'ql_music', '音乐表', NULL, NULL, 'QlMusic', 'crud', '', 'com.musicmanager.system', 'system', 'music', '音乐', 'ruoyi', '0', '/', NULL, 'admin', '2025-06-26 12:45:11', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (2, 'ql_music_category', '音乐分类表', NULL, NULL, 'QlMusicCategory', 'crud', '', 'com.musicmanager.system', 'system', 'category', '音乐分类', 'ruoyi', '0', '/', NULL, 'admin', '2025-06-26 12:45:11', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (3, 'ql_music_category_relation', '音乐分类关联表', NULL, NULL, 'QlMusicCategoryRelation', 'crud', '', 'com.musicmanager.system', 'system', 'relation', '音乐分类关联', 'ruoyi', '0', '/', NULL, 'admin', '2025-06-26 12:45:11', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (4, 'ql_music_favorite', '音乐收藏表', NULL, NULL, 'QlMusicFavorite', 'crud', '', 'com.musicmanager.system', 'system', 'favorite', '音乐收藏', 'ruoyi', '0', '/', NULL, 'admin', '2025-06-26 12:45:11', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (5, 'ql_music_play_history', '音乐播放历史表', NULL, NULL, 'QlMusicPlayHistory', 'crud', '', 'com.musicmanager.system', 'system', 'history', '音乐播放历史', 'ruoyi', '0', '/', NULL, 'admin', '2025-06-26 12:45:11', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (6, 'ql_playlist', '歌单表', NULL, NULL, 'QlPlaylist', 'crud', '', 'com.musicmanager.system', 'system', 'playlist', '歌单', 'ruoyi', '0', '/', NULL, 'admin', '2025-06-26 12:45:11', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (7, 'ql_playlist_music', '歌单音乐关联表', NULL, NULL, 'QlPlaylistMusic', 'crud', '', 'com.musicmanager.system', 'system', 'music', '歌单音乐关联', 'ruoyi', '0', '/', NULL, 'admin', '2025-06-26 12:45:12', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (8, 'ql_user', '用户表', NULL, NULL, 'QlUser', 'crud', '', 'com.musicmanager.system', 'system', 'user', '用户', 'ruoyi', '0', '/', NULL, 'admin', '2025-06-26 12:45:12', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (9, 'ql_user_follow', '用户关注关系表', NULL, NULL, 'QlUserFollow', 'crud', '', 'com.musicmanager.system', 'system', 'follow', '用户关注关系', 'ruoyi', '0', '/', NULL, 'admin', '2025-06-26 12:45:12', '', NULL, NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (1, 1, 'id', '音乐ID', 'bigint', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (2, 1, 'title', '标题', 'varchar(200)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (3, 1, 'artist', '艺术家', 'varchar(200)', 'String', 'artist', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (4, 1, 'album', '专辑', 'varchar(200)', 'String', 'album', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (5, 1, 'duration', '时长（秒）', 'int', 'Long', 'duration', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (6, 1, 'file_path', '文件路径', 'varchar(500)', 'String', 'filePath', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'textarea', '', 6, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (7, 1, 'file_name', '文件名（UUID）', 'varchar(255)', 'String', 'fileName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 7, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (8, 1, 'original_file_name', '原始文件名', 'varchar(255)', 'String', 'originalFileName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 8, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (9, 1, 'file_size', '文件大小（字节）', 'bigint', 'Long', 'fileSize', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (10, 1, 'file_type', '文件类型', 'varchar(50)', 'String', 'fileType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 10, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (11, 1, 'upload_user_id', '上传用户ID', 'bigint', 'Long', 'uploadUserId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (12, 1, 'play_count', '播放次数', 'bigint', 'Long', 'playCount', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (13, 1, 'status', '状态（0-禁用，1-启用）', 'tinyint', 'Long', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 13, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (14, 1, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '1', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 14, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (15, 1, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '1', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 15, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (16, 1, 'is_deleted', '逻辑删除标志（0-未删除，1-已删除）', 'tinyint', 'Long', 'isDeleted', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 16, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (17, 1, 'genre', '音乐流派', 'varchar(100)', 'String', 'genre', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 17, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (18, 1, 'release_year', '发布年份', 'int', 'Long', 'releaseYear', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 18, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (19, 1, 'description', '音乐描述', 'text', 'String', 'description', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 19, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (20, 1, 'tags', '标签(逗号分隔)', 'varchar(500)', 'String', 'tags', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 20, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (21, 1, 'is_public', '是否公开(0:私有 1:公开)', 'tinyint', 'Long', 'isPublic', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 21, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (22, 1, 'allow_download', '是否允许下载(0:不允许 1:允许)', 'tinyint', 'Long', 'allowDownload', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 22, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (23, 1, 'allow_comment', '是否允许评论(0:不允许 1:允许)', 'tinyint', 'Long', 'allowComment', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 23, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (24, 1, 'cover_url', '封面图片URL', 'varchar(500)', 'String', 'coverUrl', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 24, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (25, 2, 'id', '分类ID', 'bigint', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (26, 2, 'name', '分类名称', 'varchar(50)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (27, 2, 'description', '分类描述', 'varchar(200)', 'String', 'description', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (28, 2, 'sort_order', '排序', 'int', 'Long', 'sortOrder', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (29, 2, 'status', '状态(0:禁用 1:启用)', 'tinyint', 'Long', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 5, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (30, 2, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (31, 2, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (32, 3, 'id', '关联ID', 'bigint', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (33, 3, 'music_id', '音乐ID', 'bigint', 'Long', 'musicId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (34, 3, 'category_id', '分类ID', 'bigint', 'Long', 'categoryId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (35, 3, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 4, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (36, 4, 'id', '收藏ID', 'bigint', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (37, 4, 'user_id', '用户ID', 'bigint', 'Long', 'userId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (38, 4, 'music_id', '音乐ID', 'bigint', 'Long', 'musicId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (39, 4, 'playlist_id', '歌单ID', 'bigint', 'Long', 'playlistId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (40, 4, 'create_time', '收藏时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (41, 5, 'id', '播放历史ID', 'bigint', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (42, 5, 'user_id', '用户ID', 'bigint', 'Long', 'userId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (43, 5, 'music_id', '音乐ID', 'bigint', 'Long', 'musicId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (44, 5, 'play_time', '播放时间', 'datetime', 'Date', 'playTime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (45, 5, 'play_duration', '播放时长(秒)', 'int', 'Long', 'playDuration', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (46, 5, 'play_position', '播放位置(秒)', 'int', 'Long', 'playPosition', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-06-26 12:45:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (47, 6, 'id', '歌单ID', 'bigint', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (48, 6, 'name', '歌单名称', 'varchar(100)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (49, 6, 'description', '歌单描述', 'text', 'String', 'description', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 3, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (50, 6, 'cover_url', '封面图片URL', 'varchar(500)', 'String', 'coverUrl', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 4, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (51, 6, 'user_id', '创建用户ID', 'bigint', 'Long', 'userId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (52, 6, 'is_public', '是否公开（0-私有，1-公开）', 'tinyint', 'Long', 'isPublic', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (53, 6, 'play_count', '播放次数', 'bigint', 'Long', 'playCount', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (54, 6, 'music_count', '歌曲数量', 'int', 'Long', 'musicCount', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (55, 6, 'status', '状态（0-禁用，1-启用）', 'tinyint', 'Long', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 9, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (56, 6, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '1', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (57, 6, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '1', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (58, 6, 'is_deleted', '逻辑删除标志（0-未删除，1-已删除）', 'tinyint', 'Long', 'isDeleted', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (59, 7, 'id', '关联ID', 'bigint', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (60, 7, 'playlist_id', '歌单ID', 'bigint', 'Long', 'playlistId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (61, 7, 'music_id', '音乐ID', 'bigint', 'Long', 'musicId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (62, 7, 'sort_order', '排序顺序', 'int', 'Long', 'sortOrder', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (63, 7, 'create_time', '添加时间', 'datetime', 'Date', 'createTime', '0', '0', '1', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (64, 8, 'id', '用户ID', 'bigint', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (65, 8, 'username', '用户名', 'varchar(50)', 'String', 'username', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (66, 8, 'password', '密码（加密）', 'varchar(255)', 'String', 'password', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (67, 8, 'email', '邮箱', 'varchar(100)', 'String', 'email', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (68, 8, 'nickname', '昵称', 'varchar(100)', 'String', 'nickname', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 5, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (69, 8, 'avatar_url', '头像URL', 'varchar(500)', 'String', 'avatarUrl', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 6, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (70, 8, 'status', '账号状态（0-禁用，1-启用）', 'tinyint', 'Long', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 7, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (71, 8, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '1', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (72, 8, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '1', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (73, 8, 'is_deleted', '逻辑删除标志（0-未删除，1-已删除）', 'tinyint', 'Long', 'isDeleted', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (74, 9, 'id', NULL, 'bigint', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (75, 9, 'follower_id', '关注者ID', 'bigint', 'Long', 'followerId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (76, 9, 'following_id', '被关注者ID', 'bigint', 'Long', 'followingId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-06-26 12:45:12', '', NULL);
INSERT INTO `gen_table_column` VALUES (77, 9, 'created_at', '关注时间', 'timestamp', 'Date', 'createdAt', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2025-06-26 12:45:12', '', NULL);

-- ----------------------------
-- Table structure for ql_banner
-- ----------------------------
DROP TABLE IF EXISTS `ql_banner`;
CREATE TABLE `ql_banner`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Banner ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Banner标题',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Banner描述',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Banner图片OSS地址',
  `button_text` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '按钮文字',
  `action_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'explore' COMMENT '点击动作类型(explore/create/share)',
  `action_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序顺序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0:禁用 1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status_sort`(`status` ASC, `sort_order` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Banner管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_banner
-- ----------------------------
INSERT INTO `ql_banner` VALUES (1, '发现好音乐', '海量音乐库，总有一首打动你', '', '立即探索', 'explore', '/music', 1, 1, '2025-07-18 22:34:04', '2025-08-19 22:25:23', 1, '', '', NULL);
INSERT INTO `ql_banner` VALUES (2, '创建播放列表', '收藏你喜欢的音乐，随时随地享受', '', '开始创建', 'create', '/upload', 2, 1, '2025-07-18 22:34:04', '2025-08-19 22:25:20', 1, '', '', NULL);
INSERT INTO `ql_banner` VALUES (3, '分享音乐', '与朋友分享你的音乐品味', '', '了解更多', 'share', '/music', 3, 1, '2025-07-18 22:34:04', '2025-08-19 22:25:18', 1, '', '', NULL);
INSERT INTO `ql_banner` VALUES (4, 'test', 'test', '', 'test', 'explore', '/', 0, 1, '2025-07-18 22:53:55', '2025-08-19 22:25:16', 1, '', '', NULL);

-- ----------------------------
-- Table structure for ql_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `ql_comment_like`;
CREATE TABLE `ql_comment_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `comment_id` bigint NOT NULL COMMENT '评论ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_comment`(`user_id` ASC, `comment_id` ASC) USING BTREE COMMENT '用户评论唯一索引',
  INDEX `idx_comment_id`(`comment_id` ASC) USING BTREE COMMENT '评论ID索引',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评论点赞表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_comment_like
-- ----------------------------
INSERT INTO `ql_comment_like` VALUES (1, 3, 2, '2025-07-19 18:20:18');

-- ----------------------------
-- Table structure for ql_music
-- ----------------------------
DROP TABLE IF EXISTS `ql_music`;
CREATE TABLE `ql_music`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '音乐ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `artist` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '艺术家',
  `album` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专辑',
  `duration` int NULL DEFAULT NULL COMMENT '时长（秒）',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件路径',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名（UUID）',
  `original_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原始文件名',
  `file_size` bigint NOT NULL COMMENT '文件大小（字节）',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件类型',
  `upload_user_id` bigint NOT NULL COMMENT '上传用户ID',
  `play_count` bigint NOT NULL DEFAULT 0 COMMENT '播放次数',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0-未删除，1-已删除）',
  `genre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '音乐流派',
  `release_year` int NULL DEFAULT NULL COMMENT '发布年份',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '音乐描述',
  `tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签(逗号分隔)',
  `is_public` tinyint NULL DEFAULT 1 COMMENT '是否公开(0:私有 1:公开)',
  `allow_download` tinyint NULL DEFAULT 1 COMMENT '是否允许下载(0:不允许 1:允许)',
  `allow_comment` tinyint NULL DEFAULT 1 COMMENT '是否允许评论(0:不允许 1:允许)',
  `cover_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_file_name`(`file_name` ASC) USING BTREE,
  INDEX `idx_title`(`title` ASC) USING BTREE,
  INDEX `idx_artist`(`artist` ASC) USING BTREE,
  INDEX `idx_album`(`album` ASC) USING BTREE,
  INDEX `idx_upload_user_id`(`upload_user_id` ASC) USING BTREE,
  INDEX `idx_play_count`(`play_count` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE,
  INDEX `idx_music_search`(`title` ASC, `artist` ASC, `album` ASC, `status` ASC, `is_deleted` ASC) USING BTREE,
  INDEX `idx_user_music_list`(`upload_user_id` ASC, `status` ASC, `is_deleted` ASC, `create_time` ASC) USING BTREE,
  INDEX `idx_hot_music`(`play_count` ASC, `status` ASC, `is_deleted` ASC) USING BTREE,
  INDEX `idx_latest_music`(`create_time` ASC, `status` ASC, `is_deleted` ASC) USING BTREE,
  CONSTRAINT `fk_music_upload_user_id` FOREIGN KEY (`upload_user_id`) REFERENCES `ql_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 444 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '音乐表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_music
-- ----------------------------

-- ----------------------------
-- Table structure for ql_music_category
-- ----------------------------
DROP TABLE IF EXISTS `ql_music_category`;
CREATE TABLE `ql_music_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0:禁用 1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '音乐分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_music_category
-- ----------------------------
INSERT INTO `ql_music_category` VALUES (1, '流行', '流行音乐', 1, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');
INSERT INTO `ql_music_category` VALUES (2, '摇滚', '摇滚音乐', 2, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');
INSERT INTO `ql_music_category` VALUES (3, '古典', '古典音乐', 3, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');
INSERT INTO `ql_music_category` VALUES (4, '爵士', '爵士音乐', 4, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');
INSERT INTO `ql_music_category` VALUES (5, '电子', '电子音乐', 5, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');
INSERT INTO `ql_music_category` VALUES (6, '民谣', '民谣音乐', 6, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');
INSERT INTO `ql_music_category` VALUES (7, '说唱', '说唱音乐', 7, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');
INSERT INTO `ql_music_category` VALUES (8, '乡村', '乡村音乐', 8, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');
INSERT INTO `ql_music_category` VALUES (9, '蓝调', '蓝调音乐', 9, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');
INSERT INTO `ql_music_category` VALUES (10, '其他', '其他类型', 10, 1, '2025-06-07 20:57:41', '2025-06-07 20:57:41');

-- ----------------------------
-- Table structure for ql_music_category_relation
-- ----------------------------
DROP TABLE IF EXISTS `ql_music_category_relation`;
CREATE TABLE `ql_music_category_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `music_id` bigint NOT NULL COMMENT '音乐ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_music_category`(`music_id` ASC, `category_id` ASC) USING BTREE,
  INDEX `idx_music_id`(`music_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '音乐分类关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_music_category_relation
-- ----------------------------

-- ----------------------------
-- Table structure for ql_music_comment
-- ----------------------------
DROP TABLE IF EXISTS `ql_music_comment`;
CREATE TABLE `ql_music_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `music_id` bigint NOT NULL COMMENT '音乐ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父评论ID，用于回复功能',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-删除，1-正常',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_music_id`(`music_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '音乐评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_music_comment
-- ----------------------------

-- ----------------------------
-- Table structure for ql_music_favorite
-- ----------------------------
DROP TABLE IF EXISTS `ql_music_favorite`;
CREATE TABLE `ql_music_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `music_id` bigint NOT NULL COMMENT '音乐ID',
  `playlist_id` bigint NULL DEFAULT NULL COMMENT '歌单ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_music`(`user_id` ASC, `music_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_music_id`(`music_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_playlist_id`(`playlist_id` ASC) USING BTREE,
  CONSTRAINT `fk_music_favorite_playlist` FOREIGN KEY (`playlist_id`) REFERENCES `ql_playlist` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '音乐收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_music_favorite
-- ----------------------------

-- ----------------------------
-- Table structure for ql_music_play_history
-- ----------------------------
DROP TABLE IF EXISTS `ql_music_play_history`;
CREATE TABLE `ql_music_play_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '播放历史ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `music_id` bigint NOT NULL COMMENT '音乐ID',
  `play_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '播放时间',
  `play_duration` int NULL DEFAULT 0 COMMENT '播放时长(秒)',
  `play_position` int NULL DEFAULT 0 COMMENT '播放位置(秒)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_music_id`(`music_id` ASC) USING BTREE,
  INDEX `idx_play_time`(`play_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 259 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '音乐播放历史表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_music_play_history
-- ----------------------------

-- ----------------------------
-- Table structure for ql_playlist
-- ----------------------------
DROP TABLE IF EXISTS `ql_playlist`;
CREATE TABLE `ql_playlist`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '歌单ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '歌单名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '歌单描述',
  `cover_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `user_id` bigint NOT NULL COMMENT '创建用户ID',
  `is_public` tinyint NOT NULL DEFAULT 1 COMMENT '是否公开（0-私有，1-公开）',
  `play_count` bigint NOT NULL DEFAULT 0 COMMENT '播放次数',
  `music_count` int NOT NULL DEFAULT 0 COMMENT '歌曲数量',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0-未删除，1-已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_name`(`name` ASC) USING BTREE,
  INDEX `idx_is_public`(`is_public` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE,
  CONSTRAINT `fk_playlist_user_id` FOREIGN KEY (`user_id`) REFERENCES `ql_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_playlist
-- ----------------------------

-- ----------------------------
-- Table structure for ql_playlist_music
-- ----------------------------
DROP TABLE IF EXISTS `ql_playlist_music`;
CREATE TABLE `ql_playlist_music`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `playlist_id` bigint NOT NULL COMMENT '歌单ID',
  `music_id` bigint NOT NULL COMMENT '音乐ID',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_playlist_music`(`playlist_id` ASC, `music_id` ASC) USING BTREE,
  INDEX `idx_playlist_id`(`playlist_id` ASC) USING BTREE,
  INDEX `idx_music_id`(`music_id` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE,
  CONSTRAINT `fk_playlist_music_music_id` FOREIGN KEY (`music_id`) REFERENCES `ql_music` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_playlist_music_playlist_id` FOREIGN KEY (`playlist_id`) REFERENCES `ql_playlist` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌单音乐关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_playlist_music
-- ----------------------------

-- ----------------------------
-- Table structure for ql_user
-- ----------------------------
DROP TABLE IF EXISTS `ql_user`;
CREATE TABLE `ql_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密）',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '账号状态（0-禁用，1-启用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0-未删除，1-已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_user
-- ----------------------------
INSERT INTO `ql_user` VALUES (1, 'admin', '$2a$10$/wLG8FfQCdgvrYkyedfMCOR556HvgeoSfcm.52oXNdAubVK37RvRu', 'admin@musicplatform.com', '系统管理员', NULL, 1, '2025-06-06 21:37:39', '2025-08-19 22:22:01', 0);
INSERT INTO `ql_user` VALUES (2, 'testuser', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'test@musicplatform.com', '测试用户', NULL, 1, '2025-06-06 21:37:46', '2025-06-06 21:37:46', 0);

-- ----------------------------
-- Table structure for ql_user_follow
-- ----------------------------
DROP TABLE IF EXISTS `ql_user_follow`;
CREATE TABLE `ql_user_follow`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `follower_id` bigint NOT NULL COMMENT '关注者ID',
  `following_id` bigint NOT NULL COMMENT '被关注者ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_follower_following`(`follower_id` ASC, `following_id` ASC) USING BTREE,
  INDEX `idx_follower_id`(`follower_id` ASC) USING BTREE,
  INDEX `idx_following_id`(`following_id` ASC) USING BTREE,
  CONSTRAINT `ql_user_follow_ibfk_1` FOREIGN KEY (`follower_id`) REFERENCES `ql_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ql_user_follow_ibfk_2` FOREIGN KEY (`following_id`) REFERENCES `ql_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关注关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ql_user_follow
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Blob类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '日历信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Cron类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '已触发的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储的悲观锁信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '暂停的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '调度器状态表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '简单触发器的信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '同步机制的行锁表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name` ASC, `job_name` ASC, `job_group` ASC) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '触发器详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2025-06-26 12:39:53', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-06-26 12:39:53', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2025-06-26 12:39:53', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2025-06-26 12:39:53', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2025-06-26 12:39:53', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2025-06-26 12:39:53', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO `sys_config` VALUES (7, '用户管理-初始密码修改策略', 'sys.account.initPasswordModify', '1', 'Y', 'admin', '2025-06-26 12:39:53', '', NULL, '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
INSERT INTO `sys_config` VALUES (8, '用户管理-账号密码更新周期', 'sys.account.passwordValidateDays', '0', 'Y', 'admin', '2025-06-26 12:39:53', '', NULL, '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '禁用', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '启用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (100, 1, '启用', '1', 'banner_status', '', 'success', 'Y', '0', 'admin', '2025-07-19 13:49:33', '', NULL, 'Banner启用状态');
INSERT INTO `sys_dict_data` VALUES (101, 2, '禁用', '0', 'banner_status', '', 'danger', 'N', '0', 'admin', '2025-07-19 13:49:33', '', NULL, 'Banner禁用状态');
INSERT INTO `sys_dict_data` VALUES (102, 1, '探索', 'explore', 'banner_action_type', '', 'primary', 'Y', '0', 'admin', '2025-07-19 13:49:46', '', NULL, 'Banner探索动作');
INSERT INTO `sys_dict_data` VALUES (103, 2, '创建', 'create', 'banner_action_type', '', 'success', 'N', '0', 'admin', '2025-07-19 13:49:46', '', NULL, 'Banner创建动作');
INSERT INTO `sys_dict_data` VALUES (104, 3, '分享', 'share', 'banner_action_type', '', 'info', 'N', '0', 'admin', '2025-07-19 13:49:46', '', NULL, 'Banner分享动作');
INSERT INTO `sys_dict_data` VALUES (105, 4, '下载', 'download', 'banner_action_type', '', 'warning', 'N', '0', 'admin', '2025-07-19 13:49:46', '', NULL, 'Banner下载动作');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (100, 'Banner状态', 'banner_status', '0', 'admin', '2025-07-19 13:49:26', '', NULL, 'Banner状态列表');
INSERT INTO `sys_dict_type` VALUES (101, 'Banner动作类型', 'banner_action_type', '0', 'admin', '2025-07-19 13:49:39', '', NULL, 'Banner动作类型列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2025-06-26 12:39:54', '', NULL, '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2025-06-26 12:39:54', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2025-06-26 12:39:54', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (100, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-26 12:44:21');
INSERT INTO `sys_logininfor` VALUES (101, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-26 13:13:05');
INSERT INTO `sys_logininfor` VALUES (102, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-26 14:25:21');
INSERT INTO `sys_logininfor` VALUES (103, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-26 17:11:19');
INSERT INTO `sys_logininfor` VALUES (104, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '1', '验证码错误', '2025-06-26 21:35:48');
INSERT INTO `sys_logininfor` VALUES (105, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '1', '验证码错误', '2025-06-26 21:35:58');
INSERT INTO `sys_logininfor` VALUES (106, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-26 21:36:03');
INSERT INTO `sys_logininfor` VALUES (107, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-26 21:59:10');
INSERT INTO `sys_logininfor` VALUES (108, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-26 22:50:18');
INSERT INTO `sys_logininfor` VALUES (109, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '退出成功', '2025-06-26 23:05:48');
INSERT INTO `sys_logininfor` VALUES (110, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '1', '验证码错误', '2025-06-26 23:05:58');
INSERT INTO `sys_logininfor` VALUES (111, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-26 23:06:02');
INSERT INTO `sys_logininfor` VALUES (112, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-27 11:04:18');
INSERT INTO `sys_logininfor` VALUES (113, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-27 11:48:35');
INSERT INTO `sys_logininfor` VALUES (114, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-27 13:30:59');
INSERT INTO `sys_logininfor` VALUES (115, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-27 15:31:09');
INSERT INTO `sys_logininfor` VALUES (116, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-27 15:31:51');
INSERT INTO `sys_logininfor` VALUES (117, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '1', '验证码错误', '2025-06-27 15:39:32');
INSERT INTO `sys_logininfor` VALUES (118, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-06-27 15:39:40');
INSERT INTO `sys_logininfor` VALUES (119, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', '0', '登录成功', '2025-07-19 14:17:27');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2068 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2025-06-26 12:39:53', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2025-06-26 12:39:53', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2025-06-26 12:39:53', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (4, '若依官网', 0, 4, 'http://ruoyi.vip', NULL, '', '', 0, 0, 'M', '1', '0', '', 'guide', 'admin', '2025-06-26 12:39:53', 'admin', '2025-06-26 13:24:42', '若依官网地址');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2025-06-26 12:39:53', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2025-06-26 12:39:53', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2025-06-26 12:39:53', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2025-06-26 12:39:53', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2025-06-26 12:39:53', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2025-06-26 12:39:53', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2025-06-26 12:39:53', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2025-06-26 12:39:53', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2025-06-26 12:39:53', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2025-06-26 12:39:53', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2025-06-26 12:39:53', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2025-06-26 12:39:53', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2025-06-26 12:39:53', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2025-06-26 12:39:53', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2025-06-26 12:39:53', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2025-06-26 12:39:53', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2025-06-26 12:39:53', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2025-06-26 12:39:53', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2025-06-26 12:39:53', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2025-06-26 12:39:53', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2000, '音乐分类', 2054, 2, 'category', 'system/category/index', NULL, '', 1, 0, 'C', '0', '0', 'system:category:list', 'build', 'admin', '2025-06-26 12:52:03', 'admin', '2025-06-27 17:32:20', '音乐分类菜单');
INSERT INTO `sys_menu` VALUES (2001, '音乐分类查询', 2000, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:category:query', '#', 'admin', '2025-06-26 12:52:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2002, '音乐分类新增', 2000, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:category:add', '#', 'admin', '2025-06-26 12:52:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2003, '音乐分类修改', 2000, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:category:edit', '#', 'admin', '2025-06-26 12:52:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2004, '音乐分类删除', 2000, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:category:remove', '#', 'admin', '2025-06-26 12:52:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2005, '音乐分类导出', 2000, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:category:export', '#', 'admin', '2025-06-26 12:52:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2006, '音乐收藏', 2054, 1, 'favorite', 'system/favorite/index', NULL, '', 1, 0, 'C', '0', '0', 'system:favorite:list', 'star', 'admin', '2025-06-26 12:52:15', 'admin', '2025-06-26 13:22:14', '音乐收藏菜单');
INSERT INTO `sys_menu` VALUES (2007, '音乐收藏查询', 2006, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:favorite:query', '#', 'admin', '2025-06-26 12:52:15', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2008, '音乐收藏新增', 2006, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:favorite:add', '#', 'admin', '2025-06-26 12:52:15', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2009, '音乐收藏修改', 2006, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:favorite:edit', '#', 'admin', '2025-06-26 12:52:15', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2010, '音乐收藏删除', 2006, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:favorite:remove', '#', 'admin', '2025-06-26 12:52:15', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2011, '音乐收藏导出', 2006, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:favorite:export', '#', 'admin', '2025-06-26 12:52:15', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '用户关注关系', 2054, 3, 'follow', 'system/follow/index', NULL, '', 1, 0, 'C', '0', '0', 'system:follow:list', 'people', 'admin', '2025-06-26 12:52:24', 'admin', '2025-06-27 17:32:46', '用户关注关系菜单');
INSERT INTO `sys_menu` VALUES (2013, '用户关注关系查询', 2012, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:follow:query', '#', 'admin', '2025-06-26 12:52:24', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2014, '用户关注关系新增', 2012, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:follow:add', '#', 'admin', '2025-06-26 12:52:24', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2015, '用户关注关系修改', 2012, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:follow:edit', '#', 'admin', '2025-06-26 12:52:24', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2016, '用户关注关系删除', 2012, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:follow:remove', '#', 'admin', '2025-06-26 12:52:24', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '用户关注关系导出', 2012, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:follow:export', '#', 'admin', '2025-06-26 12:52:24', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '音乐播放历史', 2054, 4, 'history', 'system/history/index', NULL, '', 1, 0, 'C', '0', '0', 'system:history:list', 'dict', 'admin', '2025-06-26 12:52:46', 'admin', '2025-06-27 17:32:51', '音乐播放历史菜单');
INSERT INTO `sys_menu` VALUES (2025, '音乐播放历史查询', 2024, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:history:query', '#', 'admin', '2025-06-26 12:52:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2026, '音乐播放历史新增', 2024, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:history:add', '#', 'admin', '2025-06-26 12:52:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2027, '音乐播放历史修改', 2024, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:history:edit', '#', 'admin', '2025-06-26 12:52:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2028, '音乐播放历史删除', 2024, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:history:remove', '#', 'admin', '2025-06-26 12:52:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2029, '音乐播放历史导出', 2024, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:history:export', '#', 'admin', '2025-06-26 12:52:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2030, '音乐列表', 2054, 0, 'music', 'system/music/index', NULL, '', 1, 0, 'C', '0', '0', 'system:music:list', 'date-range', 'admin', '2025-06-26 12:52:54', 'admin', '2025-06-27 17:32:00', '音乐菜单');
INSERT INTO `sys_menu` VALUES (2031, '音乐查询', 2030, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:music:query', '#', 'admin', '2025-06-26 12:52:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2032, '音乐新增', 2030, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:music:add', '#', 'admin', '2025-06-26 12:52:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2033, '音乐修改', 2030, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:music:edit', '#', 'admin', '2025-06-26 12:52:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2034, '音乐删除', 2030, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:music:remove', '#', 'admin', '2025-06-26 12:52:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2035, '音乐导出', 2030, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:music:export', '#', 'admin', '2025-06-26 12:52:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2036, '歌单列表', 2054, 1, 'playlist', 'system/playlist/index', NULL, '', 1, 0, 'C', '0', '0', 'system:playlist:list', 'list', 'admin', '2025-06-26 12:53:14', 'admin', '2025-06-27 17:32:08', '歌单菜单');
INSERT INTO `sys_menu` VALUES (2037, '歌单查询', 2036, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:playlist:query', '#', 'admin', '2025-06-26 12:53:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2038, '歌单新增', 2036, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:playlist:add', '#', 'admin', '2025-06-26 12:53:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2039, '歌单修改', 2036, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:playlist:edit', '#', 'admin', '2025-06-26 12:53:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2040, '歌单删除', 2036, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:playlist:remove', '#', 'admin', '2025-06-26 12:53:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2041, '歌单导出', 2036, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:playlist:export', '#', 'admin', '2025-06-26 12:53:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '音乐分类关联', 2054, 5, 'relation', 'system/relation/index', NULL, '', 1, 0, 'C', '0', '0', 'system:relation:list', 'clipboard', 'admin', '2025-06-26 12:53:25', 'admin', '2025-06-27 17:32:59', '音乐分类关联菜单');
INSERT INTO `sys_menu` VALUES (2043, '音乐分类关联查询', 2042, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:relation:query', '#', 'admin', '2025-06-26 12:53:25', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2044, '音乐分类关联新增', 2042, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:relation:add', '#', 'admin', '2025-06-26 12:53:25', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2045, '音乐分类关联修改', 2042, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:relation:edit', '#', 'admin', '2025-06-26 12:53:25', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2046, '音乐分类关联删除', 2042, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:relation:remove', '#', 'admin', '2025-06-26 12:53:25', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2047, '音乐分类关联导出', 2042, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:relation:export', '#', 'admin', '2025-06-26 12:53:25', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2048, '音乐用户管理', 2054, 6, 'user', 'system/qluser/index', NULL, '', 1, 0, 'C', '0', '0', 'system:user:list', 'peoples', 'admin', '2025-06-26 12:53:32', 'admin', '2025-06-27 17:33:06', '用户菜单');
INSERT INTO `sys_menu` VALUES (2049, '用户查询', 2048, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-06-26 12:53:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2050, '用户新增', 2048, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-06-26 12:53:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2051, '用户修改', 2048, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-06-26 12:53:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2052, '用户删除', 2048, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-06-26 12:53:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2053, '用户导出', 2048, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-06-26 12:53:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2054, '音乐管理', 0, 0, '/ql_music', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'component', 'admin', '2025-06-26 13:20:38', 'admin', '2025-06-26 13:21:41', '');
INSERT INTO `sys_menu` VALUES (2055, '音乐上传', 0, 0, '/musicUpload', 'system/musicUpload/index', NULL, '', 1, 0, 'C', '0', '0', 'system:music:upload', 'upload', 'admin', '2025-06-26 13:28:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2056, 'Banner管理', 2067, 6, 'banner', 'system/qlmanage/bannar/index', '', '', 1, 0, 'C', '0', '0', 'system:banner:list', 'cascader', 'admin', '2025-07-19 13:48:34', 'admin', '2025-07-19 14:19:50', 'Banner管理菜单');
INSERT INTO `sys_menu` VALUES (2062, 'Banner查询', 2056, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:banner:query', '#', 'admin', '2025-07-19 13:48:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2063, 'Banner新增', 2056, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:banner:add', '#', 'admin', '2025-07-19 13:49:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2064, 'Banner修改', 2056, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:banner:edit', '#', 'admin', '2025-07-19 13:49:09', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2065, 'Banner删除', 2056, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:banner:remove', '#', 'admin', '2025-07-19 13:49:15', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2066, 'Banner导出', 2056, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:banner:export', '#', 'admin', '2025-07-19 13:49:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2067, '网站管理', 0, 0, 'qlmanage', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'build', 'admin', '2025-07-19 14:18:17', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2025-06-26 12:39:54', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2025-06-26 12:39:54', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (100, '代码生成', 6, 'com.musicmanager.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', '研发部门', '/tool/gen/importTable', '127.0.0.1', '内网IP', '{\"tables\":\"ql_music_favorite,ql_playlist_music,ql_playlist,ql_user_follow,ql_music,ql_music_category_relation,ql_music_category,ql_music_play_history,ql_user\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 12:45:12', 190);
INSERT INTO `sys_oper_log` VALUES (101, '代码生成', 8, 'com.musicmanager.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '研发部门', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"ql_music,ql_music_category,ql_music_category_relation,ql_music_favorite,ql_music_play_history,ql_playlist,ql_playlist_music,ql_user,ql_user_follow\"}', NULL, 0, NULL, '2025-06-26 12:48:00', 208);
INSERT INTO `sys_oper_log` VALUES (102, '菜单管理', 1, 'com.musicmanager.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createBy\":\"admin\",\"icon\":\"component\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"音乐管理\",\"menuType\":\"M\",\"orderNum\":2,\"params\":{},\"parentId\":0,\"path\":\"/ql_music\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:20:38', 27);
INSERT INTO `sys_oper_log` VALUES (103, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/category/index\",\"createTime\":\"2025-06-26 12:52:03\",\"icon\":\"build\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2000,\"menuName\":\"音乐分类\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"category\",\"perms\":\"system:category:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:21:24', 10);
INSERT INTO `sys_oper_log` VALUES (104, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createTime\":\"2025-06-26 13:20:38\",\"icon\":\"component\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2054,\"menuName\":\"音乐管理\",\"menuType\":\"M\",\"orderNum\":0,\"params\":{},\"parentId\":0,\"path\":\"/ql_music\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:21:41', 10);
INSERT INTO `sys_oper_log` VALUES (105, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/favorite/index\",\"createTime\":\"2025-06-26 12:52:15\",\"icon\":\"star\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2006,\"menuName\":\"音乐收藏\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"favorite\",\"perms\":\"system:favorite:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:22:14', 10);
INSERT INTO `sys_oper_log` VALUES (106, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/follow/index\",\"createTime\":\"2025-06-26 12:52:24\",\"icon\":\"people\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2012,\"menuName\":\"用户关注关系\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"follow\",\"perms\":\"system:follow:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:22:39', 8);
INSERT INTO `sys_oper_log` VALUES (107, '菜单管理', 3, 'com.musicmanager.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2018', '127.0.0.1', '内网IP', '2018', '{\"msg\":\"存在子菜单,不允许删除\",\"code\":601}', 0, NULL, '2025-06-26 13:22:52', 5);
INSERT INTO `sys_oper_log` VALUES (108, '菜单管理', 3, 'com.musicmanager.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2023', '127.0.0.1', '内网IP', '2023', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:23:07', 10);
INSERT INTO `sys_oper_log` VALUES (109, '菜单管理', 3, 'com.musicmanager.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2022', '127.0.0.1', '内网IP', '2022', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:23:10', 9);
INSERT INTO `sys_oper_log` VALUES (110, '菜单管理', 3, 'com.musicmanager.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2021', '127.0.0.1', '内网IP', '2021', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:23:14', 10);
INSERT INTO `sys_oper_log` VALUES (111, '菜单管理', 3, 'com.musicmanager.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2020', '127.0.0.1', '内网IP', '2020', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:23:17', 9);
INSERT INTO `sys_oper_log` VALUES (112, '菜单管理', 3, 'com.musicmanager.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2019', '127.0.0.1', '内网IP', '2019', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:23:20', 9);
INSERT INTO `sys_oper_log` VALUES (113, '菜单管理', 3, 'com.musicmanager.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2018', '127.0.0.1', '内网IP', '2018', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:23:24', 7);
INSERT INTO `sys_oper_log` VALUES (114, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/history/index\",\"createTime\":\"2025-06-26 12:52:46\",\"icon\":\"dict\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2024,\"menuName\":\"音乐播放历史\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"history\",\"perms\":\"system:history:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:24:07', 19);
INSERT INTO `sys_oper_log` VALUES (115, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/music/index\",\"createTime\":\"2025-06-26 12:52:54\",\"icon\":\"date-range\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2030,\"menuName\":\"音乐列表\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"music\",\"perms\":\"system:music:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:24:38', 10);
INSERT INTO `sys_oper_log` VALUES (116, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createTime\":\"2025-06-26 12:39:53\",\"icon\":\"guide\",\"isCache\":\"0\",\"isFrame\":\"0\",\"menuId\":4,\"menuName\":\"若依官网\",\"menuType\":\"M\",\"orderNum\":4,\"params\":{},\"parentId\":0,\"path\":\"http://ruoyi.vip\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:24:42', 7);
INSERT INTO `sys_oper_log` VALUES (117, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/user/index\",\"createTime\":\"2025-06-26 12:53:32\",\"icon\":\"peoples\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2048,\"menuName\":\"七洛音乐用户管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"user\",\"perms\":\"system:user:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:25:29', 8);
INSERT INTO `sys_oper_log` VALUES (118, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/playlist/index\",\"createTime\":\"2025-06-26 12:53:14\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2036,\"menuName\":\"歌单列表\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"playlist\",\"perms\":\"system:playlist:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:25:52', 10);
INSERT INTO `sys_oper_log` VALUES (119, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/relation/index\",\"createTime\":\"2025-06-26 12:53:25\",\"icon\":\"clipboard\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2042,\"menuName\":\"音乐分类关联\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"relation\",\"perms\":\"system:relation:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:26:10', 8);
INSERT INTO `sys_oper_log` VALUES (120, '菜单管理', 1, 'com.musicmanager.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createBy\":\"admin\",\"icon\":\"upload\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"音乐上传\",\"menuType\":\"C\",\"orderNum\":0,\"params\":{},\"parentId\":0,\"path\":\"/musicUpload\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:28:03', 10);
INSERT INTO `sys_oper_log` VALUES (121, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/user/index\",\"createTime\":\"2025-06-26 12:53:32\",\"icon\":\"peoples\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2048,\"menuName\":\"音乐用户管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"user\",\"perms\":\"system:user:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 13:30:44', 10);
INSERT INTO `sys_oper_log` VALUES (122, '在线用户', 7, 'com.musicmanager.web.controller.monitor.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', '研发部门', '/monitor/online/939dfea2-b617-4430-9473-3bd143eba22c', '127.0.0.1', '内网IP', '\"939dfea2-b617-4430-9473-3bd143eba22c\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-26 22:02:39', 9);
INSERT INTO `sys_oper_log` VALUES (123, '音乐上传', 1, 'com.musicmanager.system.controller.QlMusicController.uploadMusic()', 'POST', 1, 'admin', '研发部门', '/system/music/upload', '127.0.0.1', '内网IP', '{\"artist\":\"test\",\"album\":\"test\",\"genre\":\"流行\",\"isPublic\":\"1\",\"allowComment\":\"1\",\"title\":\"test\",\"releaseYear\":\"2025\",\"allowDownload\":\"1\"}', '{\"msg\":\"音乐上传失败: 音乐上传失败: \\r\\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`music_db`.`ql_music`, CONSTRAINT `fk_music_upload_user_id` FOREIGN KEY (`upload_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE)\\r\\n### The error may exist in file [E:\\\\Java_code2\\\\music-platform\\\\MusicManage\\\\musicmanage-admin\\\\target\\\\classes\\\\mapper\\\\system\\\\QlMusicMapper.xml]\\r\\n### The error may involve com.musicmanager.system.mapper.QlMusicMapper.insertQlMusic-Inline\\r\\n### The error occurred while setting parameters\\r\\n### SQL: insert into ql_music          ( title,             artist,             album,             duration,             file_path,             file_name,             original_file_name,             file_size,             file_type,             upload_user_id,             play_count,             status,             create_time,                          is_deleted,             genre,             release_year,                                       is_public,             allow_download,             allow_comment,             cover_url )           values ( ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,                          ?,             ?,             ?,                                       ?,             ?,             ?,             ? )\\r\\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`music_db`.`ql_music`, CONSTRAINT `fk_music_upload_user_id` FOREIGN KEY (`upload_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE)\\n; Cannot add or update a child row: a foreign key constraint fails (`music_db`.`ql_music`, CONSTRAINT `fk_music_upload_user_id` FOREIGN KEY (`upload_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE); nested exception is java', 0, NULL, '2025-06-26 22:26:02', 4854);
INSERT INTO `sys_oper_log` VALUES (124, '音乐上传', 1, 'com.musicmanager.system.controller.QlMusicController.uploadMusic()', 'POST', 1, 'admin', '研发部门', '/system/music/upload', '127.0.0.1', '内网IP', '{\"artist\":\"test\",\"album\":\"test\",\"genre\":\"流行\",\"description\":\"test\",\"isPublic\":\"1\",\"allowComment\":\"1\",\"title\":\"test\",\"releaseYear\":\"2025\",\"allowDownload\":\"1\"}', '{\"msg\":\"音乐上传失败，请稍后重试\",\"code\":500}', 0, NULL, '2025-06-27 12:20:27', 5714);
INSERT INTO `sys_oper_log` VALUES (125, '音乐上传', 1, 'com.musicmanager.system.controller.QlMusicController.uploadMusic()', 'POST', 1, 'admin', '研发部门', '/system/music/upload', '127.0.0.1', '内网IP', '{\"artist\":\"test\",\"album\":\"tssss\",\"genre\":\"流行\",\"isPublic\":\"1\",\"allowComment\":\"1\",\"title\":\"test\",\"releaseYear\":\"2025\",\"allowDownload\":\"1\"}', '{\"msg\":\"音乐上传成功\",\"code\":200,\"data\":{\"album\":\"tssss\",\"allowComment\":1,\"allowDownload\":1,\"artist\":\"test\",\"createTime\":\"2025-06-27 12:41:44\",\"duration\":0,\"fileName\":\"1750999300789_c3edea6b.mp3\",\"filePath\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/music-data/2025/06/27/1750999300789_c3edea6b.mp3\",\"fileSize\":8516199,\"fileType\":\"mp3\",\"genre\":\"流行\",\"id\":440,\"isDeleted\":0,\"isPublic\":1,\"originalFileName\":\"周深 - Rubia.mp3\",\"params\":{},\"playCount\":0,\"releaseYear\":2025,\"status\":1,\"title\":\"test\",\"uploadUserId\":1}}', 0, NULL, '2025-06-27 12:41:45', 4359);
INSERT INTO `sys_oper_log` VALUES (126, '音乐上传', 1, 'com.musicmanager.system.controller.QlMusicController.uploadMusic()', 'POST', 1, 'admin', '研发部门', '/system/music/upload', '127.0.0.1', '内网IP', '{\"artist\":\"test\",\"album\":\"tssss\",\"genre\":\"流行\",\"isPublic\":\"1\",\"allowComment\":\"1\",\"title\":\"test\",\"releaseYear\":\"2025\",\"allowDownload\":\"1\"}', '{\"msg\":\"音乐上传成功\",\"code\":200,\"data\":{\"album\":\"tssss\",\"allowComment\":1,\"allowDownload\":1,\"artist\":\"test\",\"coverUrl\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/cover/7331f4d9-b6ba-4bc7-aee9-dfa646ff0b6f.png\",\"createTime\":\"2025-06-27 12:43:58\",\"duration\":0,\"fileName\":\"1750999435171_e4c0a938.mp3\",\"filePath\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/music-data/2025/06/27/1750999435171_e4c0a938.mp3\",\"fileSize\":8516199,\"fileType\":\"mp3\",\"genre\":\"流行\",\"id\":441,\"isDeleted\":0,\"isPublic\":1,\"originalFileName\":\"周深 - Rubia.mp3\",\"params\":{},\"playCount\":0,\"releaseYear\":2025,\"status\":1,\"title\":\"test\",\"uploadUserId\":1}}', 0, NULL, '2025-06-27 12:43:58', 3785);
INSERT INTO `sys_oper_log` VALUES (127, '个人信息', 2, 'com.musicmanager.web.controller.system.SysProfileController.updateProfile()', 'PUT', 1, 'admin', '研发部门', '/system/user/profile', '127.0.0.1', '内网IP', '{\"admin\":false,\"email\":\"ry@163.com\",\"nickName\":\"若依\",\"params\":{},\"phonenumber\":\"15888888888\",\"sex\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 12:46:56', 16);
INSERT INTO `sys_oper_log` VALUES (128, '用户头像', 2, 'com.musicmanager.web.controller.system.SysProfileController.avatar()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/avatar', '127.0.0.1', '内网IP', '', '{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2025/06/27/66a294db4eae464fb45522a8dfc6f479.png\",\"code\":200}', 0, NULL, '2025-06-27 13:31:12', 98);
INSERT INTO `sys_oper_log` VALUES (129, '用户头像', 2, 'com.musicmanager.web.controller.system.SysProfileController.avatar()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/avatar', '127.0.0.1', '内网IP', '', '{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2025/06/27/11ff5e1e0407410b960dab1a8ded7488.png\",\"code\":200}', 0, NULL, '2025-06-27 13:31:27', 12);
INSERT INTO `sys_oper_log` VALUES (130, '音乐上传', 1, 'com.musicmanager.system.controller.QlMusicController.uploadMusic()', 'POST', 1, 'admin', '研发部门', '/system/music/upload', '127.0.0.1', '内网IP', '{\"artist\":\"test3\",\"album\":\"test3\",\"genre\":\"流行\",\"isPublic\":\"1\",\"allowComment\":\"1\",\"title\":\"test3\",\"releaseYear\":\"2025\",\"allowDownload\":\"1\"}', '{\"msg\":\"音乐上传成功\",\"code\":200,\"data\":{\"album\":\"test3\",\"allowComment\":1,\"allowDownload\":1,\"artist\":\"test3\",\"createTime\":\"2025-06-27 13:33:54\",\"duration\":0,\"fileName\":\"1751002428687_d6396a55.mp3\",\"filePath\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/music-data/2025/06/27/1751002428687_d6396a55.mp3\",\"fileSize\":8516199,\"fileType\":\"mp3\",\"genre\":\"流行\",\"id\":442,\"isDeleted\":0,\"isPublic\":1,\"originalFileName\":\"周深 - Rubia.mp3\",\"params\":{},\"playCount\":0,\"releaseYear\":2025,\"status\":1,\"title\":\"test3\",\"uploadUserId\":1}}', 0, NULL, '2025-06-27 13:33:54', 5855);
INSERT INTO `sys_oper_log` VALUES (131, '音乐上传', 1, 'com.musicmanager.system.controller.QlMusicController.uploadMusic()', 'POST', 1, 'admin', '研发部门', '/system/music/upload', '127.0.0.1', '内网IP', '{\"artist\":\"test45\",\"album\":\"test4\",\"genre\":\"流行\",\"isPublic\":\"1\",\"allowComment\":\"1\",\"title\":\"test4\",\"releaseYear\":\"2025\",\"allowDownload\":\"1\"}', '{\"msg\":\"音乐上传成功\",\"code\":200,\"data\":{\"album\":\"test4\",\"allowComment\":1,\"allowDownload\":1,\"artist\":\"test45\",\"coverUrl\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/cover/2025/06/27/1751002891898_9660c83e.png\",\"createTime\":\"2025-06-27 13:41:32\",\"duration\":0,\"fileName\":\"1751002886481_f5ec2949.mp3\",\"filePath\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/music-data/2025/06/27/1751002886481_f5ec2949.mp3\",\"fileSize\":8516199,\"fileType\":\"mp3\",\"genre\":\"流行\",\"id\":443,\"isDeleted\":0,\"isPublic\":1,\"originalFileName\":\"周深 - Rubia.mp3\",\"params\":{},\"playCount\":0,\"releaseYear\":2025,\"status\":1,\"title\":\"test4\",\"uploadUserId\":1}}', 0, NULL, '2025-06-27 13:41:32', 5882);
INSERT INTO `sys_oper_log` VALUES (132, '音乐上传', 1, 'com.musicmanager.system.controller.QlMusicController.uploadMusic()', 'POST', 1, 'admin', '研发部门', '/system/music/upload', '127.0.0.1', '内网IP', '{\"artist\":\"test6\",\"album\":\"test6\",\"isPublic\":\"1\",\"allowComment\":\"1\",\"title\":\"test6\",\"allowDownload\":\"1\"}', '{\"msg\":\"音乐上传成功\",\"code\":200,\"data\":{\"album\":\"test6\",\"allowComment\":1,\"allowDownload\":1,\"artist\":\"test6\",\"coverUrl\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/cover/2025/06/27/1751003603866_84d94a0e.png\",\"createTime\":\"2025-06-27 13:53:24\",\"duration\":0,\"fileName\":\"1751003598896_595e4e64.mp3\",\"filePath\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/music-data/2025/06/27/1751003598896_595e4e64.mp3\",\"fileSize\":8516199,\"fileType\":\"mp3\",\"id\":444,\"isDeleted\":0,\"isPublic\":1,\"originalFileName\":\"周深 - Rubia.mp3\",\"params\":{},\"playCount\":0,\"status\":1,\"title\":\"test6\",\"uploadUserId\":1}}', 0, NULL, '2025-06-27 13:53:24', 5423);
INSERT INTO `sys_oper_log` VALUES (133, '用户头像', 2, 'com.musicmanager.web.controller.system.SysProfileController.avatar()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/avatar', '127.0.0.1', '内网IP', '', '{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2025/06/27/56eb45ca7b894946860da4028496cc2a.jpg\",\"code\":200}', 0, NULL, '2025-06-27 13:59:32', 104);
INSERT INTO `sys_oper_log` VALUES (134, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/qluser/index\",\"createTime\":\"2025-06-26 12:53:32\",\"icon\":\"peoples\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2048,\"menuName\":\"音乐用户管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"user\",\"perms\":\"system:user:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 16:13:30', 35);
INSERT INTO `sys_oper_log` VALUES (135, '用户', 5, 'com.musicmanager.system.controller.QlUserController.export()', 'POST', 1, 'admin', '研发部门', '/system/qluser/export', '127.0.0.1', '内网IP', '{\"pageSize\":\"10\",\"pageNum\":\"1\"}', NULL, 0, NULL, '2025-06-27 16:49:45', 1454);
INSERT INTO `sys_oper_log` VALUES (136, '音乐', 2, 'com.musicmanager.system.controller.QlMusicController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/music', '127.0.0.1', '内网IP', '{\"album\":\"music1\",\"allowComment\":1,\"allowDownload\":0,\"artist\":\"HOYO-MiX\",\"coverUrl\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/cover/2025/06/08/1749312082835_b331008d.png\",\"createTime\":\"2025-06-08 00:01:23\",\"description\":\"1232\",\"duration\":134,\"fileName\":\"1749312080174_d2229593.mp3\",\"filePath\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/music-data/2025/06/08/1749312080174_d2229593.mp3\",\"fileSize\":6218834,\"fileType\":\"mp3\",\"genre\":\"流行\",\"id\":5,\"isDeleted\":0,\"isPublic\":1,\"originalFileName\":\"HOYO-MiX - Da Capo.mp3\",\"params\":{},\"playCount\":1,\"releaseYear\":2023,\"status\":1,\"tags\":\"123\",\"title\":\"Da Capo\",\"updateTime\":\"2025-06-27 17:29:02\",\"uploadUserId\":3}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 17:29:02', 51);
INSERT INTO `sys_oper_log` VALUES (137, '音乐', 2, 'com.musicmanager.system.controller.QlMusicController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/music', '127.0.0.1', '内网IP', '{\"album\":\"music1\",\"allowComment\":1,\"allowDownload\":1,\"artist\":\"HOYO-MiX\",\"coverUrl\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/cover/2025/06/08/1749312082835_b331008d.png\",\"createTime\":\"2025-06-08 00:01:23\",\"description\":\"1232\",\"duration\":134,\"fileName\":\"1749312080174_d2229593.mp3\",\"filePath\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/music-data/2025/06/08/1749312080174_d2229593.mp3\",\"fileSize\":6218834,\"fileType\":\"mp3\",\"genre\":\"流行\",\"id\":5,\"isDeleted\":0,\"isPublic\":1,\"originalFileName\":\"HOYO-MiX - Da Capo.mp3\",\"params\":{},\"playCount\":1,\"releaseYear\":2023,\"status\":1,\"tags\":\"123\",\"title\":\"Da Capo\",\"updateTime\":\"2025-06-27 17:29:05\",\"uploadUserId\":3}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 17:29:05', 7);
INSERT INTO `sys_oper_log` VALUES (138, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/music/index\",\"createTime\":\"2025-06-26 12:52:54\",\"icon\":\"date-range\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2030,\"menuName\":\"音乐列表\",\"menuType\":\"C\",\"orderNum\":0,\"params\":{},\"parentId\":2054,\"path\":\"music\",\"perms\":\"system:music:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 17:32:00', 12);
INSERT INTO `sys_oper_log` VALUES (139, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/playlist/index\",\"createTime\":\"2025-06-26 12:53:14\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2036,\"menuName\":\"歌单列表\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2054,\"path\":\"playlist\",\"perms\":\"system:playlist:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 17:32:08', 12);
INSERT INTO `sys_oper_log` VALUES (140, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/category/index\",\"createTime\":\"2025-06-26 12:52:03\",\"icon\":\"build\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2000,\"menuName\":\"音乐分类\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2054,\"path\":\"category\",\"perms\":\"system:category:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 17:32:21', 9);
INSERT INTO `sys_oper_log` VALUES (141, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/follow/index\",\"createTime\":\"2025-06-26 12:52:24\",\"icon\":\"people\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2012,\"menuName\":\"用户关注关系\",\"menuType\":\"C\",\"orderNum\":3,\"params\":{},\"parentId\":2054,\"path\":\"follow\",\"perms\":\"system:follow:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 17:32:46', 13);
INSERT INTO `sys_oper_log` VALUES (142, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/history/index\",\"createTime\":\"2025-06-26 12:52:46\",\"icon\":\"dict\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2024,\"menuName\":\"音乐播放历史\",\"menuType\":\"C\",\"orderNum\":4,\"params\":{},\"parentId\":2054,\"path\":\"history\",\"perms\":\"system:history:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 17:32:51', 8);
INSERT INTO `sys_oper_log` VALUES (143, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/relation/index\",\"createTime\":\"2025-06-26 12:53:25\",\"icon\":\"clipboard\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2042,\"menuName\":\"音乐分类关联\",\"menuType\":\"C\",\"orderNum\":5,\"params\":{},\"parentId\":2054,\"path\":\"relation\",\"perms\":\"system:relation:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 17:32:59', 10);
INSERT INTO `sys_oper_log` VALUES (144, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/qluser/index\",\"createTime\":\"2025-06-26 12:53:32\",\"icon\":\"peoples\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2048,\"menuName\":\"音乐用户管理\",\"menuType\":\"C\",\"orderNum\":6,\"params\":{},\"parentId\":2054,\"path\":\"user\",\"perms\":\"system:user:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-06-27 17:33:06', 10);
INSERT INTO `sys_oper_log` VALUES (145, '菜单管理', 1, 'com.musicmanager.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createBy\":\"admin\",\"icon\":\"build\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"网站管理\",\"menuType\":\"M\",\"orderNum\":0,\"params\":{},\"parentId\":0,\"path\":\"qlmanage\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:18:17', 26);
INSERT INTO `sys_oper_log` VALUES (146, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/qlmanage/bannar/index\",\"createTime\":\"2025-07-19 13:48:34\",\"icon\":\"banner\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2056,\"menuName\":\"Banner管理\",\"menuType\":\"C\",\"orderNum\":6,\"params\":{},\"parentId\":2067,\"path\":\"banner\",\"perms\":\"system:banner:list\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:18:43', 15);
INSERT INTO `sys_oper_log` VALUES (147, '菜单管理', 2, 'com.musicmanager.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/qlmanage/bannar/index\",\"createTime\":\"2025-07-19 13:48:34\",\"icon\":\"cascader\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2056,\"menuName\":\"Banner管理\",\"menuType\":\"C\",\"orderNum\":6,\"params\":{},\"parentId\":2067,\"path\":\"banner\",\"perms\":\"system:banner:list\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:19:50', 12);
INSERT INTO `sys_oper_log` VALUES (148, 'Banner', 1, 'com.musicmanager.system.controller.QlBannerController.add()', 'POST', 1, 'admin', '研发部门', '/system/banner', '127.0.0.1', '内网IP', '{\"actionType\":\"explore\",\"buttonText\":\"test2\",\"createTime\":\"2025-07-19 14:24:32\",\"description\":\"test2\",\"id\":5,\"imageUrl\":\"http://localhost:8090/profile/upload/2025/07/19/1693185585970ebcd9465eb94e74ec21d8231b8a255d0711200151e11b830bc4e87f45bfe699c.0_20250719142411A001.jpg\",\"params\":{},\"remark\":\"test2\",\"sortOrder\":0,\"status\":\"0\",\"title\":\"test2\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:24:32', 51);
INSERT INTO `sys_oper_log` VALUES (149, 'Banner', 2, 'com.musicmanager.system.controller.QlBannerController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/banner', '127.0.0.1', '内网IP', '{\"actionType\":\"explore\",\"buttonText\":\"test2\",\"createBy\":\"\",\"createTime\":\"2025-07-19 14:24:33\",\"description\":\"test2\",\"id\":5,\"imageUrl\":\"http://localhost:8090/profile/upload/2025/07/19/1693185585970ebcd9465eb94e74ec21d8231b8a255d0711200151e11b830bc4e87f45bfe699c.0_20250719143259A001.jpg\",\"params\":{},\"remark\":\"test2\",\"sortOrder\":0,\"status\":\"0\",\"title\":\"test2\",\"updateBy\":\"\",\"updateTime\":\"2025-07-19 14:33:02\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:33:02', 29);
INSERT INTO `sys_oper_log` VALUES (150, 'Banner', 3, 'com.musicmanager.system.controller.QlBannerController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/banner/5', '127.0.0.1', '内网IP', '[5]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:33:14', 10);
INSERT INTO `sys_oper_log` VALUES (151, 'Banner', 2, 'com.musicmanager.system.controller.QlBannerController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/banner', '127.0.0.1', '内网IP', '{\"actionType\":\"explore\",\"actionUrl\":\"/\",\"buttonText\":\"test\",\"createBy\":\"\",\"createTime\":\"2025-07-18 22:53:55\",\"description\":\"test\",\"id\":4,\"imageUrl\":\"http://localhost:8090/profile/upload/2025/07/19/1693185585970ebcd9465eb94e74ec21d8231b8a255d0711200151e11b830bc4e87f45bfe699c.0_20250719143339A002.jpg\",\"params\":{},\"sortOrder\":0,\"status\":\"1\",\"title\":\"test\",\"updateBy\":\"\",\"updateTime\":\"2025-07-19 14:33:41\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:33:41', 8);
INSERT INTO `sys_oper_log` VALUES (152, 'Banner', 2, 'com.musicmanager.system.controller.QlBannerController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/banner', '127.0.0.1', '内网IP', '{\"actionType\":\"explore\",\"actionUrl\":\"/\",\"buttonText\":\"test\",\"createBy\":\"\",\"createTime\":\"2025-07-18 22:53:55\",\"description\":\"test\",\"id\":4,\"imageUrl\":\"http://localhost:8090/profile/upload/2025/07/19/1693185585970ebcd9465eb94e74ec21d8231b8a255d0711200151e11b830bc4e87f45bfe699c.0_20250719143806A003.jpg\",\"params\":{},\"sortOrder\":0,\"status\":\"0\",\"title\":\"test\",\"updateBy\":\"\",\"updateTime\":\"2025-07-19 14:38:07\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:38:07', 12);
INSERT INTO `sys_oper_log` VALUES (153, 'Banner', 2, 'com.musicmanager.system.controller.QlBannerController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/banner', '127.0.0.1', '内网IP', '{\"actionType\":\"explore\",\"actionUrl\":\"/\",\"buttonText\":\"test\",\"createBy\":\"\",\"createTime\":\"2025-07-18 22:53:55\",\"description\":\"test\",\"id\":4,\"imageUrl\":\"http://localhost:8090/profile/upload/2025/07/19/微信图片_20231022150347_20250719143824A004.jpg\",\"params\":{},\"sortOrder\":0,\"status\":\"0\",\"title\":\"test\",\"updateBy\":\"\",\"updateTime\":\"2025-07-19 14:38:24\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:38:24', 9);
INSERT INTO `sys_oper_log` VALUES (154, 'Banner图片上传', 1, 'com.musicmanager.system.controller.QlBannerController.uploadBannerImage()', 'POST', 1, 'admin', '研发部门', '/system/qlbanner/upload', '127.0.0.1', '内网IP', '', '{\"msg\":\"Banner图片上传成功\",\"code\":200,\"data\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/banner/2025/07/19/1752907529066_bc5e456a.jpg\"}', 0, NULL, '2025-07-19 14:45:30', 1363);
INSERT INTO `sys_oper_log` VALUES (155, 'Banner图片上传', 1, 'com.musicmanager.system.controller.QlBannerController.uploadBannerImage()', 'POST', 1, 'admin', '研发部门', '/system/qlbanner/upload', '127.0.0.1', '内网IP', '', '{\"msg\":\"Banner图片上传成功\",\"code\":200,\"data\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/banner/2025/07/19/1752908317478_ca93bb32.jpg\"}', 0, NULL, '2025-07-19 14:58:38', 1175);
INSERT INTO `sys_oper_log` VALUES (156, 'Banner', 2, 'com.musicmanager.system.controller.QlBannerController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/qlbanner', '127.0.0.1', '内网IP', '{\"actionType\":\"explore\",\"actionUrl\":\"/\",\"buttonText\":\"test\",\"createBy\":\"\",\"createTime\":\"2025-07-18 22:53:55\",\"description\":\"test\",\"id\":4,\"imageUrl\":\"https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/music/banner/2025/07/19/1752908317478_ca93bb32.jpg\",\"params\":{},\"sortOrder\":0,\"status\":\"0\",\"title\":\"test\",\"updateBy\":\"\",\"updateTime\":\"2025-07-19 14:58:40\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-07-19 14:58:40', 18);
INSERT INTO `sys_oper_log` VALUES (157, 'Banner状态', 2, 'com.musicmanager.system.controller.QlBannerController.changeStatus()', 'PUT', 1, 'admin', '研发部门', '/system/qlbanner/changeStatus', '127.0.0.1', '内网IP', '{\"id\":4,\"params\":{},\"status\":\"0\"}', '{\"msg\":\"状态修改成功\",\"code\":200}', 0, NULL, '2025-07-19 15:36:00', 39);
INSERT INTO `sys_oper_log` VALUES (158, 'Banner状态', 2, 'com.musicmanager.system.controller.QlBannerController.changeStatus()', 'PUT', 1, 'admin', '研发部门', '/system/qlbanner/changeStatus', '127.0.0.1', '内网IP', '{\"id\":4,\"params\":{},\"status\":\"1\"}', '{\"msg\":\"状态修改成功\",\"code\":200}', 0, NULL, '2025-07-19 15:36:17', 14);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2025-06-26 12:39:53', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2025-06-26 12:39:53', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2025-06-26 12:39:53', '', NULL, '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 2055);
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime NULL DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '/profile/avatar/2025/06/27/56eb45ca7b894946860da4028496cc2a.jpg', '$2a$10$/wLG8FfQCdgvrYkyedfMCOR556HvgeoSfcm.52oXNdAubVK37RvRu', '0', '0', '127.0.0.1', '2025-07-19 14:17:27', '2025-06-26 12:39:53', 'admin', '2025-06-26 12:39:53', '', '2025-07-19 14:17:27', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$/wLG8FfQCdgvrYkyedfMCOR556HvgeoSfcm.52oXNdAubVK37RvRu', '0', '0', '127.0.0.1', '2025-06-26 12:39:53', '2025-06-26 12:39:53', 'admin', '2025-06-26 12:39:53', '', NULL, '测试员');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

SET FOREIGN_KEY_CHECKS = 1;
