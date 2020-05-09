/*
用户刷量统计表
*/
-- ----------------------------
-- Table structure for t_user_refresh_statistic_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_refresh_statistic_info`;
CREATE TABLE `t_user_refresh_statistic_info`  (
  `fUuid` int(11) NOT NULL,
  `fUserName` varchar(25) NULL DEFAULT NULL COMMENT '用户的登录名',
  `fType` varchar(50) NULL DEFAULT NULL COMMENT '链接类型',
  `fTerminalType` varchar(50)  NULL DEFAULT NULL COMMENT '终端类型',
  `fTotalKeywordCount` int(11) NULL DEFAULT NULL COMMENT '机器分组下关键词总数',
  `fNeedOptimizeKeywordCount` int(11) NULL DEFAULT NULL COMMENT '需要优化的关键词数',
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
) ENGINE = InnoDB CHARACTER SET = utf8;



#赋予用户刷量统计权限
insert into t_resourec_new (fResourceName,fUrl,fVersion) values("用户刷量统计","/internal/userRefreshStatistic/toUserRefreshStatistic","2.0");
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical', 'Operation', 'SEOSales', 'SEO')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '用户刷量统计' AND fVersion = '2.0') tem_resource);