SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_machine_group_work_info
-- ----------------------------
DROP TABLE IF EXISTS `t_machine_group_work_info`;
CREATE TABLE `t_machine_group_work_info`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT,
  `fMachineGroup` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '机器分组',
  `fType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '链接类型',
  `fTerminalType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '终端类型',
  `fTotalKeywordCount` int(11) NULL DEFAULT NULL COMMENT '机器分组下关键词总数',
  `fNeedOptimizeKeywordCount` int(11) NULL DEFAULT NULL COMMENT '需要优化的关键词数',
  `fInvalidKeywordCount` int(11) NULL DEFAULT NULL COMMENT '无效的关键词数',
  `fZeroOptimizedCount` int(11) NULL DEFAULT NULL COMMENT '零优化数',
  `fReachStandardKeywordCount` int(11) NULL DEFAULT NULL COMMENT '达标关键字数',
  `fTodaySubTotal` double NULL DEFAULT NULL COMMENT '今天小计',
  `fTotalOptimizeCount` int(11) NULL DEFAULT NULL COMMENT '总计优化数',
  `fTotalOptimizedCount` int(11) NULL DEFAULT NULL COMMENT '总优化数',
  `fNeedOptimizeCount` int(11) NULL DEFAULT NULL COMMENT '需要优化数',
  `fQueryCount` int(11) NULL DEFAULT NULL COMMENT '查询数',
  `fTotalMachineCount` int(11) NULL DEFAULT NULL COMMENT '总机器数',
  `fUnworkMachineCount` int(11) NULL DEFAULT NULL COMMENT '空闲机器数',
  `fMaxInvalidCount` int(11) NULL DEFAULT NULL COMMENT '最大无效数',
  `fIdleTotalMinutes` int(11) NULL DEFAULT NULL COMMENT '空闲总分钟数',
  `fCreateDate` date NULL DEFAULT NULL COMMENT '创建的日期',
  PRIMARY KEY (`fUuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
