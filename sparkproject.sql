
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


DROP TABLE IF EXISTS `communicate`;
CREATE TABLE `communicate`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `toid` int NOT NULL,
  `addtime` mediumtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `type` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;



DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `k` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `v` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  PRIMARY KEY (`k`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;


INSERT INTO `config` VALUES ('user', 'admin');
INSERT INTO `config` VALUES ('pwd', '123456');
INSERT INTO `config` VALUES ('gonggao', '欢迎使用本平台有问题请联系QQ485412');
INSERT INTO `config` VALUES ('sitename', '零工经济在线协作平台');
INSERT INTO `config` VALUES ('title', 'Java程序设计');
INSERT INTO `config` VALUES ('keywords', '零工经济在线协作平台');
INSERT INTO `config` VALUES ('description', '构建一个服务于零工经济的在线协作平台后端，连接雇主和各类自由职业者（如程序员、设计师、文案撰写人、翻译等）。雇主可以发布项目需求，包括任务描述、技能要求、时间期限和预算等；自由职业者能够展示个人技能、工作经验和作品集，搜索并申请合适的项目。平台提供项目进度跟踪、即时通讯、报酬支付保障以及双方互评等功能，以促进零工经济的高效、诚信协作。');
INSERT INTO `config` VALUES ('api', 'https://sinocheck2.market.alicloudapi.com');
INSERT INTO `config` VALUES ('smtp', 'smtp.163.com');
INSERT INTO `config` VALUES ('port', '465');
INSERT INTO `config` VALUES ('mail', '15632198267@163.com');
INSERT INTO `config` VALUES ('key', 'GGipj9KBubcSzSB2');
INSERT INTO `config` VALUES ('appcode', 'ff7a018725e1433c9d7ec588eeebf4f0');



DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `toid` varchar(88) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `num` varchar(88) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS `introduce`;
CREATE TABLE `introduce`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `title` varchar(88) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` varchar(88) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `amount` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `orderid` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `twoid` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `addtime` datetime NULL DEFAULT NULL,
  `status` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;



DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `money` float NOT NULL,
  `title` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `acid` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `addtime` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `endtime` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `uevalute` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `acevalute` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `state` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;



DROP TABLE IF EXISTS `projectrequire`;
CREATE TABLE `projectrequire`  (
  `proid` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `state` int NOT NULL DEFAULT 0,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `pwd` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `qq` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `mail` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `card` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `roleid` int NOT NULL DEFAULT 0,
  `tips` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  `BZMoney` double UNSIGNED NULL DEFAULT NULL,
  `xinyu` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '50',
  `addtime` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `state` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;


SET FOREIGN_KEY_CHECKS = 1;
