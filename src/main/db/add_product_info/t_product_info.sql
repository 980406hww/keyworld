/*
 Navicat MySQL Data Transfer

 Source Server         : server
 Source Server Type    : MySQL
 Source Server Version : 50517
 Source Host           : server:33208
 Source Schema         : db_keyword

 Target Server Type    : MySQL
 Target Server Version : 50517
 File Encoding         : 65001

 Date: 21/05/2020 10:59:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_product_info
-- ----------------------------
DROP TABLE IF EXISTS `t_product_info`;
CREATE TABLE `t_product_info`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `fProductName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名',
  `fProductPrice` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品价格',
  `fCreateDate` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `fAlterDate` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`fUuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
