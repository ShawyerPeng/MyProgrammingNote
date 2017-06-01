/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50519
Source Host           : localhost:3306
Source Database       : ssm

Target Server Type    : MYSQL
Target Server Version : 50519
File Encoding         : 65001

Date: 2017-01-26 19:38:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL COMMENT 'id',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `age` char(2) DEFAULT NULL COMMENT '年龄',
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '张三', '19', '2016-11-10 11:06:59');
INSERT INTO `user` VALUES ('2', '方法', '99', '2016-11-10 11:07:05');
INSERT INTO `user` VALUES ('3', '吖吖', '45', '2016-11-10 11:07:02');
INSERT INTO `user` VALUES ('4', '啊啊', '34', '2016-11-10 11:07:09');
INSERT INTO `user` VALUES ('5', '等等', '45', '2016-11-10 11:07:14');
INSERT INTO `user` VALUES ('6', '试试', '89', '2016-11-10 11:07:11');
INSERT INTO `user` VALUES ('7', '王五', '67', '2016-11-10 11:06:56');
INSERT INTO `user` VALUES ('8', '张三', '19', '2016-11-10 11:05:53');
INSERT INTO `user` VALUES ('9', '王五', '67', '2016-11-10 11:06:56');
INSERT INTO `user` VALUES ('10', '王五', '67', '2016-11-10 11:06:56');
INSERT INTO `user` VALUES ('11', '张三', '19', '2016-11-10 11:05:53');
INSERT INTO `user` VALUES ('12', '123', '12', '2017-11-24 15:00:28');