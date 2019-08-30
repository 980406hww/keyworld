
# 创建自动化测试数据统计表
DROP TABLE IF EXISTS `t_algorithm_test_data_statistics`;
CREATE TABLE `t_algorithm_test_data_statistics`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '算法执行统计信息表ID',
  `fAlgorithmTestPlanUuid` int(11) NOT NULL COMMENT '算法测试计划ID',
  `fAlgorithmTestTaskUuid` int(11) NOT NULL COMMENT '算法测试任务ID',
  `fContactPerson` varchar(255) NULL COMMENT '用户联系人名称',
  `fTopTenCount` int(11) NULL DEFAULT 0 COMMENT '首页个数',
  `fZeroOptimizedCount` int(11)  NULL DEFAULT 0 COMMENT '没刷量个数',
  `fRankChangeRate` VARCHAR(20) NULL DEFAULT null COMMENT '排名首页率',
  `fRecordDate` date NULL DEFAULT NULL COMMENT '数据记录日期',
  `fCreateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fUpdateTime` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`fUuid`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '算法执行统计信息表' ;