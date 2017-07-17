/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : lunchdb

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-07-17 16:36:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '菜单名',
  `type` int(1) DEFAULT '1' COMMENT '类型:1盖浇饭，2小吃，3饮品',
  `price` double(11,2) DEFAULT '0.00' COMMENT '价格',
  `enable` int(1) DEFAULT '1' COMMENT '是否可用: 1可用，0不可用',
  `shop` int(1) DEFAULT '1' COMMENT '店名,1表示放心午餐，2表示其他',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '豉汁排骨饭', '1', '16.00', '1', '1');
INSERT INTO `menu` VALUES ('2', '农家小炒肉', '1', '16.00', '1', '1');
INSERT INTO `menu` VALUES ('3', '猪手饭', '1', '16.00', '1', '1');
INSERT INTO `menu` VALUES ('4', '杏鲍菇培根饭', '1', '18.00', '1', '1');
INSERT INTO `menu` VALUES ('5', '咖喱鸡肉饭', '1', '16.00', '1', '1');
INSERT INTO `menu` VALUES ('8', '鱼香肉丝', '1', '16.00', '1', '1');
INSERT INTO `menu` VALUES ('9', '香草鸡排饭', '1', '16.00', '1', '1');
INSERT INTO `menu` VALUES ('10', '素什锦饭', '1', '14.00', '1', '1');
INSERT INTO `menu` VALUES ('11', '台湾卤肉饭', '1', '16.00', '1', '1');
INSERT INTO `menu` VALUES ('12', '木瓜莲子银耳羹', '3', '4.00', '1', '1');
INSERT INTO `menu` VALUES ('13', '红豆蕙米芊实汤', '3', '4.00', '1', '1');
INSERT INTO `menu` VALUES ('14', '紫菜蛋花汤', '3', '4.00', '1', '1');
INSERT INTO `menu` VALUES ('15', '醪糟', '3', '4.00', '1', '1');
INSERT INTO `menu` VALUES ('16', '酸梅汤', '3', '4.00', '1', '1');
INSERT INTO `menu` VALUES ('17', '可乐', '3', '4.00', '1', '1');
INSERT INTO `menu` VALUES ('18', '雪碧', '3', '4.00', '1', '1');
INSERT INTO `menu` VALUES ('19', '特色卤面', '2', '14.00', '1', '1');
INSERT INTO `menu` VALUES ('20', '港式香饭', '2', '14.00', '1', '1');
INSERT INTO `menu` VALUES ('21', '米饭', '2', '2.00', '1', '1');

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '人名',
  `enable` int(1) DEFAULT '1' COMMENT '是否可用,1可用，0不可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES ('1', '周湘林', '1');
INSERT INTO `person` VALUES ('2', '李思宇', '1');
INSERT INTO `person` VALUES ('3', '郭琦', '1');
INSERT INTO `person` VALUES ('4', '张鑫钰', '1');
INSERT INTO `person` VALUES ('5', '杨君岳', '1');
INSERT INTO `person` VALUES ('6', '毛智斌', '1');
INSERT INTO `person` VALUES ('7', '李辉', '1');
INSERT INTO `person` VALUES ('8', '王娟', '1');
INSERT INTO `person` VALUES ('9', '雅倩', '1');
INSERT INTO `person` VALUES ('10', '崔雨轩', '1');
INSERT INTO `person` VALUES ('11', '王攀', '1');
INSERT INTO `person` VALUES ('12', '愈卓', '1');
INSERT INTO `person` VALUES ('13', '靳毅', '1');

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) DEFAULT NULL COMMENT '菜id',
  `person_id` int(11) DEFAULT NULL COMMENT '人名id',
  `time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '订餐时间',
  `price_end` double(11,2) DEFAULT NULL COMMENT '折扣之后的价格',
  `enable` int(1) DEFAULT '1' COMMENT '是否可用，1可用，0不可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of relation
-- ----------------------------
INSERT INTO `relation` VALUES ('1', '1', '1', '2017-07-07 17:52:21', '10.00', '1');
INSERT INTO `relation` VALUES ('2', '2', '2', '2017-07-07 17:54:08', '12.00', '1');
