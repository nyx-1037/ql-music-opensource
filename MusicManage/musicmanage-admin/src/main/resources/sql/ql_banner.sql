-- ----------------------------
-- Table structure for ql_banner
-- ----------------------------
DROP TABLE IF EXISTS `ql_banner`;
CREATE TABLE `ql_banner`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Banner ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Banner标题',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Banner描述',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片URL',
  `button_text` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮文本',
  `action_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '动作类型（0链接 1弹窗 2下载）',
  `action_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '动作URL',
  `sort_order` int(4) NOT NULL DEFAULT 0 COMMENT '排序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status_sort`(`status`, `sort_order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Banner表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ql_banner
-- ----------------------------
INSERT INTO `ql_banner` VALUES (1, '欢迎来到音乐平台', '发现好音乐，享有一首打动你', '/profile/upload/2024/01/01/banner1.jpg', '立即探索', '0', '/music/discover', 1, '0', 'admin', '2024-01-01 12:00:00', '', NULL, '首页轮播图1');
INSERT INTO `ql_banner` VALUES (2, '热门音乐推荐', '最新最热的音乐等你来听', '/profile/upload/2024/01/01/banner2.jpg', '查看更多', '0', '/music/hot', 2, '0', 'admin', '2024-01-01 12:00:00', '', NULL, '首页轮播图2');
INSERT INTO `ql_banner` VALUES (3, '个性化推荐', '根据你的喜好为你推荐音乐', '/profile/upload/2024/01/01/banner3.jpg', '个人中心', '0', '/user/profile', 3, '0', 'admin', '2024-01-01 12:00:00', '', NULL, '首页轮播图3');

-- ----------------------------
-- 菜单 SQL
-- ----------------------------
-- 父菜单ID
SELECT @parentId := menu_id FROM sys_menu WHERE menu_name = '系统管理';

-- 插入Banner管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Banner管理', @parentId, '1', 'banner', 'system/qlmanage/bannar/index', 1, 0, 'C', '0', '0', 'system:banner:list', 'image', 'admin', sysdate(), '', null, 'Banner管理菜单');

-- 获取Banner管理菜单ID
SELECT @menuId := LAST_INSERT_ID();

-- 按钮父菜单ID
SELECT @parentId := @menuId;

-- 插入Banner管理按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Banner查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:banner:query',        '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Banner新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:banner:add',          '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Banner修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:banner:edit',         '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Banner删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:banner:remove',       '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES('Banner导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:banner:export',       '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 字典类型 SQL
-- ----------------------------
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('Banner动作类型', 'banner_action_type', '0', 'admin', sysdate(), '', null, 'Banner动作类型列表');

-- ----------------------------
-- 字典数据 SQL
-- ----------------------------
SELECT @dictTypeId := dict_id FROM sys_dict_type WHERE dict_type = 'banner_action_type';

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '链接', '0', 'banner_action_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '链接跳转');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '弹窗', '1', 'banner_action_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '弹窗显示');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (3, '下载', '2', 'banner_action_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '文件下载');