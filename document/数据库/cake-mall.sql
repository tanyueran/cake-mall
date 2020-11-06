/*
 Navicat Premium Data Transfer

 Source Server         : 本地虚拟机mysql
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 192.168.150.128:3306
 Source Schema         : cake-mall

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 05/11/2020 17:26:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cake_news
-- ----------------------------
DROP TABLE IF EXISTS `cake_news`;
CREATE TABLE `cake_news`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sender_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '发送方id',
  `receiver_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '接受方id',
  `title` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '消息简介',
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '消息内容',
  `is_read` int(11) NULL DEFAULT NULL COMMENT '是否已读（0未读、1已读）',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '消息发送时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '消息已读时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_Reference_6`(`receiver_id`) USING BTREE,
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`receiver_id`) REFERENCES `cake_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '订单消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cake_order
-- ----------------------------
DROP TABLE IF EXISTS `cake_order`;
CREATE TABLE `cake_order`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `cake_product_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '蛋糕主键',
  `create_user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '下单人主键',
  `action_user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '接单拒单操作人主键',
  `status` int(11) NULL DEFAULT NULL COMMENT '订单状态(\r\n            0、已下单，未付款；\r\n            5、未付款，订单取消；\r\n            10、已付款，待发货；\r\n            15、已拒单，订单取消；\r\n            20、已接单，待配货；\r\n            30、已配送，待收货；\r\n            40、已收货，完成订单)下单后30分未付款，则取消订单；付款后30分钟未接单，则订单取消',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '订单创建时间',
  `number` int(11) NULL DEFAULT NULL COMMENT '蛋糕个数',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `total_price` double NULL DEFAULT NULL COMMENT '总价',
  `status5_time` datetime(0) NULL DEFAULT NULL COMMENT '未付款订单取消时间',
  `status10_time` datetime(0) NULL DEFAULT NULL COMMENT '付款时间',
  `status15_time` datetime(0) NULL DEFAULT NULL COMMENT '订单被拒时间',
  `status20_time` datetime(0) NULL DEFAULT NULL COMMENT '接单时间',
  `status30_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `status40_time` datetime(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_Reference_2`(`cake_product_id`) USING BTREE,
  INDEX `FK_Reference_3`(`create_user_id`) USING BTREE,
  INDEX `FK_Reference_4`(`action_user_id`) USING BTREE,
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`cake_product_id`) REFERENCES `cake_product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`create_user_id`) REFERENCES `cake_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`action_user_id`) REFERENCES `cake_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '蛋糕订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cake_product
-- ----------------------------
DROP TABLE IF EXISTS `cake_product`;
CREATE TABLE `cake_product`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `cake_product_categories_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '分类id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '蛋糕名称',
  `cake_imgs` varchar(2550) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '蛋糕图片英文逗号分割，最多五个',
  `detail` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '蛋糕详情',
  `delete_status` int(11) NULL DEFAULT NULL COMMENT '删除状态(0未删除，1删除)',
  `recommend_status` int(11) NULL DEFAULT NULL COMMENT '是否推荐状态（0不推荐，1推荐）',
  `price` double NULL DEFAULT NULL COMMENT '蛋糕价格',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_Reference_1`(`cake_product_categories_id`) USING BTREE,
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`cake_product_categories_id`) REFERENCES `cake_product_categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '蛋糕表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cake_product
-- ----------------------------
INSERT INTO `cake_product` VALUES ('1323193790281945088', '1323095111910952960', '阿尔卑斯', '2020-11-03/1323439059405770752.jpeg', '阿尔卑斯，爱情纯洁的象征，白雪皑皑！~~~~~', 0, 0, 120, '', '2020-11-02 17:23:02', '2020-11-03 09:37:16');
INSERT INTO `cake_product` VALUES ('1323260068052996096', '1323093980837187584', '友谊之花', '2020-11-03/1323439020482629632.jpeg', '友谊天长地久', 0, 1, 88, '', '2020-11-02 21:46:33', '2020-11-03 09:37:07');
INSERT INTO `cake_product` VALUES ('1323260384504844288', '1323095111910952960', '百年好合', '2020-11-03/1323438958159466496.jpeg', '百年好合', 0, 1, 100, '', '2020-11-02 21:49:06', '2020-11-03 09:36:52');
INSERT INTO `cake_product` VALUES ('1323440960956403712', '1323095710618488832', '好吃蛋糕', '2020-11-03/1323441395138170880.jpeg', '好吃蛋糕，好吃你就多吃点儿', 0, 1, 99, '', '2020-11-03 09:47:02', '2020-11-03 09:47:08');
INSERT INTO `cake_product` VALUES ('1323443240325746688', '1323093980837187584', '开心蛋糕', '2020-11-03/1323448731340771328.jpeg', '开心蛋糕，天天开心哦', 0, 0, 123, '', '2020-11-03 10:16:14', '2020-11-03 10:16:14');
INSERT INTO `cake_product` VALUES ('1323448874454618112', '1323095403545104384', '猫咪蛋糕', '2020-11-03/1323448898949353472.jpg', '猫咪蛋糕，喵~~~~一般的感觉', 0, 0, 60, '', '2020-11-03 10:16:50', '2020-11-03 10:16:50');
INSERT INTO `cake_product` VALUES ('1323449024593924096', '1323095403545104384', '双层蛋糕', '2020-11-03/1323449065442250752.jpeg', '双层蛋糕，享受不一样的感觉，哦耶~~~~', 0, 1, 200, '', '2020-11-03 10:17:57', '2020-11-03 10:17:57');
INSERT INTO `cake_product` VALUES ('1323449699209973760', '1323093980837187584', '三明治', '2020-11-03/1323450415169277952.jpeg', '三明治蛋糕，友谊天长地久。~~~~', 0, 1, 20, '', '2020-11-03 10:22:54', '2020-11-03 10:22:54');
INSERT INTO `cake_product` VALUES ('1323450550896955392', '1323095111910952960', '红果果蛋糕', '2020-11-03/1323450591871111168.jpeg', '红果果蛋糕，真的很好吃哦', 0, 0, 30, '', '2020-11-03 10:23:53', '2020-11-03 10:23:53');
INSERT INTO `cake_product` VALUES ('1323450796108550144', '1323095111910952960', '芝士蛋糕', '2020-11-03/1323450853063004160.jpeg', '芝士蛋糕，不止一点点的好吃', 0, 0, 300, '', '2020-11-03 10:24:28', '2020-11-03 10:24:28');
INSERT INTO `cake_product` VALUES ('1323450942749806592', '1323095111910952960', '卡哇伊蛋糕', '2020-11-03/1323450977600278528.jpeg', '卡哇伊蛋糕，就是那么可爱', 0, 1, 40, '', '2020-11-03 10:24:57', '2020-11-03 10:24:57');

-- ----------------------------
-- Table structure for cake_product_categories
-- ----------------------------
DROP TABLE IF EXISTS `cake_product_categories`;
CREATE TABLE `cake_product_categories`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类型名称',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类型编码',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `AK_Key_2`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '蛋糕类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cake_product_categories
-- ----------------------------
INSERT INTO `cake_product_categories` VALUES ('1323093980837187584', '友情', 'friend', '关于友情类的', '2020-11-02 10:48:35', '2020-11-02 10:51:31');
INSERT INTO `cake_product_categories` VALUES ('1323095111910952960', '爱情', 'love', '关于爱情的', '2020-11-02 10:51:13', '2020-11-02 10:51:40');
INSERT INTO `cake_product_categories` VALUES ('1323095403545104384', '家庭', 'family', '关于家庭的', '2020-11-02 10:52:53', '2020-11-02 10:52:53');
INSERT INTO `cake_product_categories` VALUES ('1323095710618488832', '生日', 'birthday', '', '2020-11-02 10:53:25', '2020-11-02 10:53:25');

-- ----------------------------
-- Table structure for cake_user
-- ----------------------------
DROP TABLE IF EXISTS `cake_user`;
CREATE TABLE `cake_user`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `cake_user_role_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '角色主键',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '昵称',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户姓名',
  `head_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '头像',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户账号',
  `user_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户密码',
  `money` double NULL DEFAULT NULL COMMENT '账户金额',
  `status` int(11) NULL DEFAULT NULL COMMENT '激活状态（0激活，1冻结）',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `AK_Key_2`(`user_code`) USING BTREE,
  INDEX `FK_Reference_5`(`cake_user_role_id`) USING BTREE,
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`cake_user_role_id`) REFERENCES `cake_user_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cake_user
-- ----------------------------
INSERT INTO `cake_user` VALUES ('', '1320927463940952065', '测试2号', '测试账号2', '2020-11-05/1324279236185427968.jpeg', 'test02', '670b14728ad9902aecba32e22fa4f6bd', 0, 0, NULL, '2020-11-05 17:16:09', '2020-11-05 17:18:47');
INSERT INTO `cake_user` VALUES ('1320927463940952064', '1320927463940952065', '测试用户1', '测试1号', '2020-10-29/1321625932607393792.jpg', 'test01', '670b14728ad9902aecba32e22fa4f6bd', 0, 0, NULL, '2020-10-28 14:49:21', '2020-11-03 16:19:37');
INSERT INTO `cake_user` VALUES ('1322067257660149760', '1320927463940952064', '测试的abc', '阿播次', '2020-11-02/1323074808904290304.jpeg', 'abc', '670b14728ad9902aecba32e22fa4f6bd', NULL, 0, '备注', '2020-10-30 14:46:42', '2020-11-05 14:08:25');
INSERT INTO `cake_user` VALUES ('1323076098170425344', '1320927463940952064', '超级管理员', '超级管理员', '2020-11-02/1323076298289057792.jpg', 'admin', '5f4dcc3b5aa765d61d8327deb882cf99', NULL, 0, '超级管理员-勿删', '2020-11-02 09:35:20', '2020-11-02 09:35:59');

-- ----------------------------
-- Table structure for cake_user_role
-- ----------------------------
DROP TABLE IF EXISTS `cake_user_role`;
CREATE TABLE `cake_user_role`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `role_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '角色code',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `AK_Key_2`(`role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cake_user_role
-- ----------------------------
INSERT INTO `cake_user_role` VALUES ('1320927463940952064', 'manager', '管理员', '管理员角色', '2020-10-28 14:48:26', '2020-10-28 14:48:26');
INSERT INTO `cake_user_role` VALUES ('1320927463940952065', 'user', '用户', '普通用户角色', '2020-10-28 14:49:16', '2020-10-28 14:49:16');

SET FOREIGN_KEY_CHECKS = 1;
