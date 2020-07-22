
-- ----------------------------
-- Table structure for cms_sync_manage
-- ----------------------------
DROP TABLE IF EXISTS `cms_sync_manage`;
CREATE TABLE `cms_sync_manage`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` int(11) NOT NULL COMMENT '用户ID',
  `COMPANY_CODE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型 pt,qz',
  `SEARCH_ENGINE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '搜索引擎',
  `SYNC_OPERA_STATUS_TIME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步修改操作状态的时间',
  `SYNC_STATUS_TIME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步状态的时间',
  `SYNC_POSITION_TIME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步排名的时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `USER_ID`(`USER_ID`, `COMPANY_CODE`, `TYPE`) USING BTREE,
  INDEX `COMPANY_CODE`(`COMPANY_CODE`, `TYPE`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '同步管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cms_sync_manage
-- ----------------------------
INSERT INTO `cms_sync_manage` VALUES (10, 101, 'liebiao360', 'pt', '360', '2020-07-12 23:34', '2020-07-10 16:22', '2020-07-22 15:46');
INSERT INTO `cms_sync_manage` VALUES (11, 101, 'liebiaobaidu', 'pt', '百度', '2020-07-12 23:31', '2020-07-21 18:41', '2020-07-22 15:36');
INSERT INTO `cms_sync_manage` VALUES (12, 101, 'liebiaosougou', 'pt', '搜狗', '2020-07-12 23:33', '2020-07-21 18:42', '2020-07-22 15:46');
INSERT INTO `cms_sync_manage` VALUES (13, 104, 'guoren360', 'pt', '360', NULL, NULL, '2020-07-09 08:42');
INSERT INTO `cms_sync_manage` VALUES (14, 104, 'guorenbaidu', 'pt', '百度', '2020-07-12 23:34', '2020-07-10 16:22', '2020-07-22 15:30');
INSERT INTO `cms_sync_manage` VALUES (15, 104, 'guorensougou', 'pt', '搜狗', NULL, NULL, '2020-07-09 08:42');
INSERT INTO `cms_sync_manage` VALUES (16, 104, 'guorenuc', 'pt', '神马', NULL, NULL, '2020-07-09 08:42');
INSERT INTO `cms_sync_manage` VALUES (17, 74, '168', 'qz', NULL, NULL, '2020-07-10 16:41', '2020-07-22 12:29');
INSERT INTO `cms_sync_manage` VALUES (18, 101, 'liebiaouc', 'pt', '神马', '2020-07-12 23:33', '2020-07-10 16:22', '2020-07-13 16:16');
INSERT INTO `cms_sync_manage` VALUES (19, 34, '188', 'qz', NULL, NULL, '2020-07-10 16:41', '2020-07-22 12:28');

