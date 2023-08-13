/*
 Source Server         : geekplus
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : 127.0.0.1:3306
 Source Schema         : admin

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 11/08/2023 12:23:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `class_name` varchar(100) DEFAULT '' COMMENT 'Java类名称',
  `model_name` varchar(100) DEFAULT NULL COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名，也就是功能名',
  `base_package_name` varchar(120) DEFAULT NULL COMMENT '基础包名称',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `other` varchar(100) DEFAULT '0' COMMENT '其他',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代码生成业务表';

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `column_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '列表ID',
  `column_name` varchar(100) DEFAULT NULL COMMENT '列表名称',
  `is_pk` int(2) DEFAULT NULL COMMENT '是否主键',
  `is_increment` int(2) DEFAULT NULL COMMENT '是否自增',
  `is_required` int(2) DEFAULT NULL COMMENT '是否必须',
  `column_comment` varchar(100) DEFAULT NULL COMMENT '列表注释',
  `column_type` varchar(50) DEFAULT NULL COMMENT '列表类型',
  `sort` int(2) DEFAULT NULL COMMENT '排序',
  `table_id` int(11) DEFAULT NULL COMMENT '关联表的ID',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='生成表数据列表';

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8 DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8 DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8 COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (100, 0, '0', '麦壳畅想', 0, '麦客', '13465898832', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:10', 'admin', '2023-07-25 10:47:14');
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '畅想核心', 1, '麦客', '13465898832', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:14', 'admin', '2023-07-25 10:47:14');
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '南京分公司', 2, '麦壳', '15888888888', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:19', '', '2023-07-24 05:11:46');
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '软件开发部门', 1, '麦客', '13465898832', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:24', 'admin', '2023-07-25 10:47:14');
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '麦客', '13465898832', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:32', 'admin', '2023-07-25 10:47:14');
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '麦客', '13465898832', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:37', 'admin', '2023-07-25 10:47:14');
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '麦客', '13465898832', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:41', 'admin', '2023-07-25 10:47:14');
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '麦客', '13465898832', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:46', 'admin', '2023-07-25 10:47:14');
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '极客普拉斯', '15888888888', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:50', '', '2023-07-24 05:11:41');
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '极客普拉斯', '15888888888', 'xxx@geekplus.xyz', '0', '0', 'admin', '2023-07-20 02:49:53', '', '2023-07-24 05:11:46');
INSERT INTO `sys_dept` VALUES (110, 101, '0,100,101', '网络安全部门', 6, '极客普拉斯', '13333333333', 'xxx@geekplus.xyz', '0', '0', '', '2023-07-20 03:44:03', 'admin', '2023-07-25 10:47:14');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(11) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8 DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8 DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dict_name` char(44) DEFAULT NULL COMMENT '字典名称',
  `dict_type` varchar(30) DEFAULT NULL COMMENT '字典类型',
  `status` char(2) DEFAULT NULL COMMENT '状态，0为正常，1为停用',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '登录日志ID',
  `log_user_id` bigint(20) DEFAULT NULL COMMENT '登录用户ID',
  `log_user_name` varchar(40) DEFAULT NULL COMMENT '登录用户名',
  `log_login_ip` varchar(100) DEFAULT NULL COMMENT '登录IP地址',
  `login_location` varchar(50) DEFAULT NULL COMMENT '登录的地点',
  `log_browser` varchar(100) DEFAULT NULL COMMENT '登录浏览器',
  `log_system` varchar(30) DEFAULT NULL COMMENT '登录系统',
  `log_time` datetime DEFAULT NULL COMMENT '登录事件',
  `log_type` tinyint(4) DEFAULT NULL COMMENT '登录状态，0为失败，1为成功',
  `log_msg` varchar(100) DEFAULT NULL COMMENT '登录信息（备注）',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统登录日志';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8 DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '组件路径',
  `is_frame` int(11) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int(11) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8 DEFAULT '' COMMENT '菜单类型（C目录 M菜单 B按钮）',
  `visible` char(1) CHARACTER SET utf8 DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8 DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8 DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单权限';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 0, 'system', '', 1, 0, 'C', '0', '0', NULL, 'system', '', '2023-07-04 00:58:30', '', '2023-07-03 10:26:41', '');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 8, 'monitor', NULL, 1, 0, 'C', '0', '0', '', 'monitor', 'admin', '2023-07-04 00:58:19', '', '2023-08-06 22:10:40', '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 9, 'tool', NULL, 1, 0, 'C', '0', '0', '', 'tool', 'admin', '2023-07-04 00:58:25', '', '2023-08-06 22:10:47', '系统工具目录');
INSERT INTO `sys_menu` VALUES (101, '用户管理', 1, 1, 'userManage', 'system/user/index', 1, 0, 'M', '0', '0', 'system:user:list', 'el-icon-user', '', '2023-07-04 00:58:35', '', '2023-08-03 05:51:44', '');
INSERT INTO `sys_menu` VALUES (102, '角色管理', 1, 2, 'roleManage', 'system/role/index', 1, 0, 'M', '0', '0', 'system:role:list', 'el-icon-info', '', '2023-07-04 00:58:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (103, '菜单管理', 1, 3, 'menuManage', 'system/menu/index', 1, 0, 'M', '0', '0', 'system:menu:list', 'el-icon-menu', '', '2023-07-04 00:58:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (104, '系统通知', 1, 4, 'noticeManage', 'system/notice/index', 1, 0, 'M', '0', '0', 'system:notice:list', 'el-icon-s-claim', '', '2023-06-29 03:04:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (105, '部门管理', 1, 4, 'dept', 'system/dept/index', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2020-11-24 13:49:41', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', 'system/log/index', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2023-07-04 00:59:11', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2020-11-24 13:49:41', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2023-07-04 00:59:16', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2023-07-04 00:59:22', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (121, '代码生成', 3, 1, 'generator', 'tool/generator/index.vue', 1, 0, 'M', '0', '0', 'tool:generator', 'table', '', '2023-07-29 03:21:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (123, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2023-07-04 00:59:27', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (124, '密码重置', 101, 1, '', NULL, 1, 0, 'B', '0', '0', 'system:user:resetPwd', '#', '', '2023-08-11 12:14:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (125, '用户添加', 101, 2, '', NULL, 1, 0, 'B', '0', '0', 'system:user:add', '#', '', '2023-08-03 06:18:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (126, '用户删除', 101, 3, '', NULL, 1, 0, 'B', '0', '0', 'system:user:delete', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (127, '用户更新', 101, 4, '', NULL, 1, 0, 'B', '0', '0', 'system:user:update', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (128, '用户信息', 101, 5, '', NULL, 1, 0, 'B', '0', '0', 'system:user:detail', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (129, '个人信息', 101, 6, '', NULL, 1, 0, 'B', '0', '0', 'system:user:profile', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (130, '用户头像', 101, 7, '', NULL, 1, 0, 'B', '0', '0', 'system:user:avatar', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (131, '用户上传头像', 101, 8, '', NULL, 1, 0, 'B', '0', '0', 'system:user:upAvatar', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (132, '用户头像列表', 101, 9, '', NULL, 1, 0, 'B', '0', '0', 'system:user:avatarList', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (133, '所有用户列表', 101, 10, '', NULL, 1, 0, 'B', '0', '0', 'system:user:listAll', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (134, '用户信息导出', 101, 11, '', NULL, 1, 0, 'B', '0', '0', 'system:user:export', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (135, '角色添加', 102, 1, '', NULL, 1, 0, 'B', '0', '0', 'system:role:add', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (136, '角色删除', 102, 2, '', NULL, 1, 0, 'B', '0', '0', 'system:role:delete', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (137, '角色更新', 102, 3, '', NULL, 1, 0, 'B', '0', '0', 'system:role:update', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (138, '角色信息', 102, 4, '', NULL, 1, 0, 'B', '0', '0', 'system:role:detail', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (139, '所有角色列表', 102, 5, '', NULL, 1, 0, 'B', '0', '0', 'system:role:listAll', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (140, '角色列表导出', 102, 6, '', NULL, 1, 0, 'B', '0', '0', 'system:role:export', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (141, '添加菜单', 103, 1, '', NULL, 1, 0, 'B', '0', '0', 'system:menu:add', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (142, '删除菜单', 103, 2, '', NULL, 1, 0, 'B', '0', '0', 'system:menu:delete', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (143, '更新菜单', 103, 3, '', NULL, 1, 0, 'B', '0', '0', 'system:menu:update', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (144, '查询菜单', 103, 4, '', NULL, 1, 0, 'B', '0', '0', 'system:menu:detail', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (145, '所有菜单', 103, 5, '', NULL, 1, 0, 'B', '0', '0', 'system:menu:listAll', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (146, '菜单导出', 103, 6, '', NULL, 1, 0, 'B', '0', '0', 'system:menu:export', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (147, '通知添加', 104, 1, '', NULL, 1, 0, 'B', '0', '0', 'system:notice:add', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (148, '通知删除', 104, 2, '', NULL, 1, 0, 'B', '0', '0', 'system:notice:delete', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (149, '通知更新', 104, 3, '', NULL, 1, 0, 'B', '0', '0', 'system:notice:update', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (150, '通知信息', 104, 4, '', NULL, 1, 0, 'B', '0', '0', 'system:notice:detail', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (151, '所有通知', 104, 5, '', NULL, 1, 0, 'B', '0', '0', 'system:notice:listAll', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (152, '通知导出', 104, 6, '', NULL, 1, 0, 'B', '0', '0', 'system:notice:export', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (153, '部门添加', 105, 1, '', NULL, 1, 0, 'B', '0', '0', 'system:dept:add', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (154, '部门删除', 105, 2, '', NULL, 1, 0, 'B', '0', '0', 'system:dept:delete', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (155, '部门更新', 105, 3, '', NULL, 1, 0, 'B', '0', '0', 'system:dept:update', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (156, '部门信息', 105, 4, '', NULL, 1, 0, 'B', '0', '0', 'system:dept:detail', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (157, '所有部门', 105, 5, '', NULL, 1, 0, 'B', '0', '0', 'system:dept:listAll', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (158, '部门批量添加', 105, 6, '', NULL, 1, 0, 'B', '0', '0', 'system:dept:batchAdd', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operLog', 'system/log/operlog/index', 1, 0, 'M', '0', '0', 'system:operLog:list', 'form', 'admin', '2023-07-04 01:00:50', '', '2023-08-03 05:52:40', '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'loginLog', 'system/log/loginlog/index', 1, 0, 'M', '0', '0', 'system:loginLog:list', 'logininfor', 'admin', '2023-07-04 01:00:55', '', '2023-08-03 05:52:26', '登录日志菜单');
INSERT INTO `sys_menu` VALUES (502, '删除操作日志', 500, 1, '', NULL, 1, 0, 'B', '0', '0', 'system:operLog:delete', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (503, '清空操作日志', 500, 2, '', NULL, 1, 0, 'B', '0', '0', 'system:operLog:clean', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (504, '操作日志信息', 500, 3, '', NULL, 1, 0, 'B', '0', '0', 'system:operLog:detail', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (505, '所有操作日志', 500, 4, '', NULL, 1, 0, 'B', '0', '0', 'system:operLog:listAll', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (506, '操作日志导出', 500, 5, '', NULL, 1, 0, 'B', '0', '0', 'system:operLog:export', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (507, '删除登录日志', 501, 1, '', NULL, 1, 0, 'B', '0', '0', 'system:loginLog:delete', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (508, '清空登录日志', 501, 2, '', NULL, 1, 0, 'B', '0', '0', 'system:loginLog:clean', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (509, '登录日志信息', 501, 3, '', NULL, 1, 0, 'B', '0', '0', 'system:loginLog:detail', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (510, '所有登录日志', 501, 4, '', NULL, 1, 0, 'B', '0', '0', 'system:loginLog:listAll', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (511, '登录日志导出', 501, 5, '', NULL, 1, 0, 'B', '0', '0', 'system:loginLog:export', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (512, '清空生成表', 121, 1, '', NULL, 1, 0, 'B', '0', '0', 'tool:genTable:clean', '#', '', '2023-08-03 06:20:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (513, '用户强退', 109, 1, '', '', 1, 0, 'B', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2023-08-12 02:14:29', '', '2023-08-12 02:15:15', '');
COMMIT;

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `notice_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notice_title` varchar(60) DEFAULT NULL COMMENT '标题',
  `notice_content` text COMMENT '内容',
  `notifier` varchar(50) DEFAULT NULL COMMENT '发布公告的人，一般是管理员',
  `notice_type` varchar(255) DEFAULT NULL COMMENT '公告类型',
  `notice_url` varchar(100) DEFAULT NULL COMMENT '公告携带的url',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(22) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(22) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
BEGIN;
INSERT INTO `sys_notice` VALUES (1, '这是第一条公告', '<h3>你好，我是geekplus admin系统，<span style=\"color: rgb(230, 0, 0);\">这是一个基于springboot+vue的开源管理系统</span></h3>', 'admin', '1', NULL, '2022-05-08 23:44:17', 'admin', NULL, NULL);
INSERT INTO `sys_notice` VALUES (2, 'Hello World！', '你好，世界！', 'admin', '1', NULL, '2022-05-11 09:24:18', 'admin', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8 DEFAULT '' COMMENT '模块标题',
  `business_type` int(11) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8 DEFAULT '' COMMENT '请求方式',
  `operator_type` int(11) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8 DEFAULT '' COMMENT '操作人员',
  `group_name` varchar(50) CHARACTER SET utf8 DEFAULT '' COMMENT '群组名称',
  `oper_url` varchar(255) CHARACTER SET utf8 DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(50) CHARACTER SET utf8 DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8 DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8 DEFAULT '' COMMENT '请求参数',
  `json_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '返回参数',
  `status` int(11) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8 DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `oper_browser` varchar(80) DEFAULT NULL COMMENT '操作浏览器',
  `oper_os` varchar(80) DEFAULT NULL COMMENT '操作系统',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志';

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `permission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_name` varchar(56) DEFAULT NULL COMMENT '权限名称',
  `remark` varchar(200) DEFAULT NULL COMMENT '权限列表',
  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统权限表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8 NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(11) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8 DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本群组数据权限 4：本群组及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8 NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '系统管理员', 'admin', 1, '1', 1, 1, '0', '0', '', '2022-05-03 22:42:20', '', '2023-07-03 08:09:03', '系统管理员，最高级角色，可以控制所有权限');
INSERT INTO `sys_role` VALUES (2, '普通用户', 'common', 2, '1', 1, 1, '0', '0', '', '2022-05-03 22:42:25', '', '2023-07-01 12:29:57', '普通用户，没有系统管理的权限');
INSERT INTO `sys_role` VALUES (3, '网站管理员', 'webManage', 3, '1', 1, 1, '0', '0', '', '2023-07-01 12:36:17', '', '2023-07-05 18:32:00', '网站管理员，负责管理网站信息');
INSERT INTO `sys_role` VALUES (4, '开发者', 'development', 4, '1', 1, 1, '0', '0', '', '2023-07-02 06:51:14', '', '2023-08-11 12:22:17', '开发者，为网站系统提供功能开发服务');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '角色菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 101);
INSERT INTO `sys_role_menu` VALUES (1, 102);
INSERT INTO `sys_role_menu` VALUES (1, 103);
INSERT INTO `sys_role_menu` VALUES (1, 104);
INSERT INTO `sys_role_menu` VALUES (1, 105);
INSERT INTO `sys_role_menu` VALUES (1, 108);
INSERT INTO `sys_role_menu` VALUES (1, 109);
INSERT INTO `sys_role_menu` VALUES (1, 111);
INSERT INTO `sys_role_menu` VALUES (1, 112);
INSERT INTO `sys_role_menu` VALUES (1, 121);
INSERT INTO `sys_role_menu` VALUES (1, 123);
INSERT INTO `sys_role_menu` VALUES (1, 124);
INSERT INTO `sys_role_menu` VALUES (1, 125);
INSERT INTO `sys_role_menu` VALUES (1, 126);
INSERT INTO `sys_role_menu` VALUES (1, 127);
INSERT INTO `sys_role_menu` VALUES (1, 128);
INSERT INTO `sys_role_menu` VALUES (1, 129);
INSERT INTO `sys_role_menu` VALUES (1, 130);
INSERT INTO `sys_role_menu` VALUES (1, 131);
INSERT INTO `sys_role_menu` VALUES (1, 132);
INSERT INTO `sys_role_menu` VALUES (1, 133);
INSERT INTO `sys_role_menu` VALUES (1, 134);
INSERT INTO `sys_role_menu` VALUES (1, 135);
INSERT INTO `sys_role_menu` VALUES (1, 136);
INSERT INTO `sys_role_menu` VALUES (1, 137);
INSERT INTO `sys_role_menu` VALUES (1, 138);
INSERT INTO `sys_role_menu` VALUES (1, 139);
INSERT INTO `sys_role_menu` VALUES (1, 140);
INSERT INTO `sys_role_menu` VALUES (1, 141);
INSERT INTO `sys_role_menu` VALUES (1, 142);
INSERT INTO `sys_role_menu` VALUES (1, 143);
INSERT INTO `sys_role_menu` VALUES (1, 144);
INSERT INTO `sys_role_menu` VALUES (1, 145);
INSERT INTO `sys_role_menu` VALUES (1, 146);
INSERT INTO `sys_role_menu` VALUES (1, 147);
INSERT INTO `sys_role_menu` VALUES (1, 148);
INSERT INTO `sys_role_menu` VALUES (1, 149);
INSERT INTO `sys_role_menu` VALUES (1, 150);
INSERT INTO `sys_role_menu` VALUES (1, 151);
INSERT INTO `sys_role_menu` VALUES (1, 152);
INSERT INTO `sys_role_menu` VALUES (1, 153);
INSERT INTO `sys_role_menu` VALUES (1, 154);
INSERT INTO `sys_role_menu` VALUES (1, 155);
INSERT INTO `sys_role_menu` VALUES (1, 156);
INSERT INTO `sys_role_menu` VALUES (1, 157);
INSERT INTO `sys_role_menu` VALUES (1, 158);
INSERT INTO `sys_role_menu` VALUES (1, 500);
INSERT INTO `sys_role_menu` VALUES (1, 501);
INSERT INTO `sys_role_menu` VALUES (1, 502);
INSERT INTO `sys_role_menu` VALUES (1, 503);
INSERT INTO `sys_role_menu` VALUES (1, 504);
INSERT INTO `sys_role_menu` VALUES (1, 505);
INSERT INTO `sys_role_menu` VALUES (1, 506);
INSERT INTO `sys_role_menu` VALUES (1, 507);
INSERT INTO `sys_role_menu` VALUES (1, 508);
INSERT INTO `sys_role_menu` VALUES (1, 509);
INSERT INTO `sys_role_menu` VALUES (1, 510);
INSERT INTO `sys_role_menu` VALUES (1, 511);
INSERT INTO `sys_role_menu` VALUES (1, 512);
INSERT INTO `sys_role_menu` VALUES (1, 513);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 123);
INSERT INTO `sys_role_menu` VALUES (2, 2000);
INSERT INTO `sys_role_menu` VALUES (2, 2001);
INSERT INTO `sys_role_menu` VALUES (2, 2002);
INSERT INTO `sys_role_menu` VALUES (2, 2003);
INSERT INTO `sys_role_menu` VALUES (2, 2014);
INSERT INTO `sys_role_menu` VALUES (3, 1);
INSERT INTO `sys_role_menu` VALUES (3, 104);
INSERT INTO `sys_role_menu` VALUES (3, 147);
INSERT INTO `sys_role_menu` VALUES (3, 148);
INSERT INTO `sys_role_menu` VALUES (3, 149);
INSERT INTO `sys_role_menu` VALUES (3, 150);
INSERT INTO `sys_role_menu` VALUES (4, 1);
INSERT INTO `sys_role_menu` VALUES (4, 2);
INSERT INTO `sys_role_menu` VALUES (4, 3);
INSERT INTO `sys_role_menu` VALUES (4, 101);
INSERT INTO `sys_role_menu` VALUES (4, 102);
INSERT INTO `sys_role_menu` VALUES (4, 103);
INSERT INTO `sys_role_menu` VALUES (4, 104);
INSERT INTO `sys_role_menu` VALUES (4, 108);
INSERT INTO `sys_role_menu` VALUES (4, 109);
INSERT INTO `sys_role_menu` VALUES (4, 111);
INSERT INTO `sys_role_menu` VALUES (4, 112);
INSERT INTO `sys_role_menu` VALUES (4, 121);
INSERT INTO `sys_role_menu` VALUES (4, 123);
INSERT INTO `sys_role_menu` VALUES (4, 500);
INSERT INTO `sys_role_menu` VALUES (4, 501);
INSERT INTO `sys_role_menu` VALUES (4, 512);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_id`,`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色权限关系表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门组织ID',
  `user_name` varchar(33) NOT NULL COMMENT '用户账户名',
  `nick_name` varchar(33) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT NULL COMMENT '用户类型，是否为管理员',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮件',
  `phoneunmber` varchar(13) DEFAULT NULL COMMENT '手机号',
  `gender` char(1) DEFAULT NULL COMMENT '性别（0为女，1为男，2为未知）',
  `avatar` varchar(101) DEFAULT NULL COMMENT '头像地址',
  `password` varchar(101) DEFAULT NULL COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常，1停禁）',
  `del_flag` char(1) DEFAULT '0' COMMENT '存在状态（0为存在，1删除）',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注信息，可以是个人简介等',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '系统管理员', '1', 'geekplus@mk.com', '13245357547', '1', '/profile/avatar/2022/07/04/e36aa247-a795-4e70-a7f6-d1f7c4b2e299.jpeg', 'c0ac167302e2bbea4137501572a115a5', '0', '0', '127.0.0.1', '2022-08-10 04:29:19', NULL, '2022-04-26 22:12:10', NULL, '2022-08-11 12:22:39', '系统管理员，拥有最高的操作权限');
INSERT INTO `sys_user` VALUES (2, 103, 'commonuser', '普通用户', '2', 'geekplus@mk.com', '11111222224', '1', '/profile/avatar/2022/02/17/68589fb7-ba50-4da2-bcd9-151b805940e8.jpeg', 'c0ac167302e2bbea4137501572a115a5', '0', '0', NULL, '2022-07-06 02:37:00', NULL, '2022-04-26 22:12:10', NULL, '2022-07-09 12:58:01', '普通用户');
INSERT INTO `sys_user` VALUES (3, 103, 'geekplus', '极客普拉斯', '1', 'geekcjj@geekplus.xyz', '12233556789', '1', '/profile/avatar/2022/08/01/cfd6ea74-b706-42e7-a582-792e378bad63.jpeg', 'c0ac167302e2bbea4137501572a115a5', '0', '0', '117.89.1.32', '2022-08-11 12:06:44', NULL, '2022-07-02 06:36:44', NULL, '2022-08-11 12:06:44', '极客普拉斯');
INSERT INTO `sys_user` VALUES (4, 103, 'developer', '开发者', '2', 'geekplus@dev.com', '12324544654', '1', '/profile/avatar/2022/07/04/ddc9f62f-b304-4e67-adc5-c3d54ead3239.jpeg', 'c0ac167302e2bbea4137501572a115a5', '0', '0', '117.89.1.15', '2022-07-20 11:32:27', NULL, '2022-07-02 06:42:05', NULL, '2022-07-20 11:32:26', '开发者人员');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户角色关系表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (1, 2);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (3, 1);
INSERT INTO `sys_user_role` VALUES (3, 2);
INSERT INTO `sys_user_role` VALUES (3, 3);
INSERT INTO `sys_user_role` VALUES (3, 4);
INSERT INTO `sys_user_role` VALUES (4, 4);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
