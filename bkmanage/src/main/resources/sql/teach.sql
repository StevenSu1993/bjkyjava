SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for icon
-- ----------------------------
DROP TABLE IF EXISTS `icon`;
CREATE TABLE `icon`  (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of icon
-- ----------------------------

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int(11) NOT NULL,
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `isChild` int(1) NULL DEFAULT NULL,
  `pid` int(11) NULL DEFAULT NULL COMMENT '不能为空，默认就是0',
  `role_id` int(11) NULL DEFAULT NULL,
  `creat_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT NULL COMMENT '0 代表是父级菜单 1 2 ... 代表的是子菜单',
  `creater_id` int(11) NULL DEFAULT NULL,
  `create_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'vue 中xxx.vue 存放的路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '课程管理', '/index', 0, 0, NULL, '2022-03-11 17:44:54', '2022-03-11 17:45:16', 0, 3, 'admin', 'Index');
INSERT INTO `menu` VALUES (2, '素材中心', '/index', 0, 0, NULL, '2022-03-11 17:44:57', '2022-03-11 17:45:18', 0, 3, 'admin', 'Index');
INSERT INTO `menu` VALUES (3, '系统学员', '/index', 0, 0, NULL, '2022-03-11 17:45:01', '2022-03-11 17:45:20', 0, 3, 'admin', 'Index');
INSERT INTO `menu` VALUES (4, '作业课程', '/work', 1, 1, NULL, '2022-03-11 17:45:04', '2022-03-11 17:45:23', 1, 3, 'admin', 'Work');
INSERT INTO `menu` VALUES (5, '图文素材', '/imageM', 1, 2, NULL, '2022-03-11 17:45:07', '2022-03-11 17:45:26', 1, 3, 'admin', 'ImageM');
INSERT INTO `menu` VALUES (6, '模板素材', '/templateM', 1, 2, NULL, '2022-03-11 17:45:09', '2022-03-11 17:45:27', 1, 3, 'admin', 'TemplateM');
INSERT INTO `menu` VALUES (7, '学员总览', '/student', 1, 3, NULL, '2022-03-11 17:45:11', '2022-03-11 17:45:30', 1, 3, 'admin', '../views/systemStudents/Student');
INSERT INTO `menu` VALUES (8, '班级管理', '/classManage', 1, 3, NULL, '2022-03-11 17:45:13', '2022-03-11 17:45:33', 1, 3, 'admin', '../views/systemStudents/ClassManage');
INSERT INTO `menu` VALUES (9, '菜单管理', '/index', 0, 0, NULL, '2022-03-12 16:51:33', '2022-03-12 16:51:36', 0, 3, 'admin', NULL);
INSERT INTO `menu` VALUES (10, '添加菜单', '/addMenu', 1, 9, NULL, '2022-03-12 16:54:09', '2022-03-12 16:54:12', 1, 3, 'admin', NULL);
INSERT INTO `menu` VALUES (11, '查看所有菜单', '/showMenu', 1, 9, NULL, '2022-03-12 19:40:22', '2022-03-12 19:40:25', 1, 3, 'admin', NULL);
INSERT INTO `menu` VALUES (12, '服务监控', '/index', 0, 0, NULL, '2022-03-13 16:51:15', '2022-03-13 16:51:17', 0, 3, 'admin', NULL);
INSERT INTO `menu` VALUES (13, '查看系统信息', '/monitor', 1, 12, NULL, '2022-03-13 16:52:26', '2022-03-13 16:52:29', 1, 3, 'admin', NULL);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` tinyint(4) NOT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, 'superadmin');
INSERT INTO `permission` VALUES (2, 'admin');
INSERT INTO `permission` VALUES (3, 'normal');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'admin');
INSERT INTO `role` VALUES (2, 'teach');
INSERT INTO `role` VALUES (3, 'student');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` int(11) NOT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  `menu_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 1, 1);
INSERT INTO `role_menu` VALUES (2, 1, 2);
INSERT INTO `role_menu` VALUES (3, 1, 3);
INSERT INTO `role_menu` VALUES (4, 1, 4);
INSERT INTO `role_menu` VALUES (5, 1, 5);
INSERT INTO `role_menu` VALUES (6, 1, 6);
INSERT INTO `role_menu` VALUES (7, 1, 7);
INSERT INTO `role_menu` VALUES (8, 1, 8);
INSERT INTO `role_menu` VALUES (9, 1, 9);
INSERT INTO `role_menu` VALUES (10, 1, 10);
INSERT INTO `role_menu` VALUES (11, 1, 11);
INSERT INTO `role_menu` VALUES (12, 1, 12);
INSERT INTO `role_menu` VALUES (13, 1, 13);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `id` int(11) NULL DEFAULT NULL,
  `name` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  `role_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('zhangsan', 1, 'zhangsan', 1, 'admin');
INSERT INTO `user` VALUES ('lisi', 2, 'lisi', 1, 'admin');
INSERT INTO `user` VALUES ('MTIzNDU2', 3, 'admin', 1, 'admin');

-- ----------------------------
-- Table structure for user_permission
-- ----------------------------
DROP TABLE IF EXISTS `user_permission`;
CREATE TABLE `user_permission`  (
  `id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission_id` int(11) NULL DEFAULT NULL,
  `permission_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_permission
-- ----------------------------
INSERT INTO `user_permission` VALUES (1, 1, 'zhangsan', 3, 'normal');
INSERT INTO `user_permission` VALUES (2, 1, 'zhangsan', 2, 'admin');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(11) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 'zhangsan', 3, 'student');
INSERT INTO `user_role` VALUES (2, 1, 'zhangsan', 1, 'admin');

SET FOREIGN_KEY_CHECKS = 1;
