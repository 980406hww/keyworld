/*
 Navicat Premium Data Transfer

 Source Server         : 公司
 Source Server Type    : MySQL
 Source Server Version : 50517
 Source Host           : server:3306
 Source Schema         : db_keyword

 Target Server Type    : MySQL
 Target Server Version : 50517
 File Encoding         : 65001

 Date: 29/10/2019 15:51:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_customer_keyword_mon
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_keyword_mon`;
CREATE TABLE `t_customer_keyword_mon`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT,
  `fCustomerUuid` int(11) NOT NULL COMMENT '关键字所属',
  `fKeywordUuid` int(11) NOT NULL COMMENT '关键字ID',
  `fKeyword` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关键字',
  `fPosition` int(11) NOT NULL COMMENT '排名',
  `fRecordDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`fUuid`) USING BTREE,
  INDEX `t_customer_keyword_mon_fRecordDate_fCustomerUuid_index`(`fRecordDate`, `fCustomerUuid`) USING BTREE,
  INDEX `t_customer_keyword_mon_fKeywordUuid_index`(`fKeywordUuid`) USING BTREE,
  INDEX `t_customer_keyword_mon_fRecordDate_index`(`fRecordDate`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '关键字历史排名记录' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;