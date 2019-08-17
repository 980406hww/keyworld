
USE db_keyword;

#计划表
DROP TABLE IF EXISTS `t_algorithm_test_plan`;
CREATE TABLE `t_algorithm_test_plan`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '算法测试计划id',
  `fAlgorithmTestPlanName` varchar(255) NOT NULL COMMENT '算法测试计划名称',
  `fOperationCombineName` varchar(255) NULL DEFAULT NULL COMMENT '操作组合名称',
  `fMachineGroup` varchar(255) NULL DEFAULT NULL COMMENT '机器分组',
  `fTestIntervalDay` int(11) NULL DEFAULT NULL COMMENT '测试间隔日期（每隔多少天生成一批测试数据）',
  `fTestKeywordCount` int(11) NULL DEFAULT NULL COMMENT '测试词数',
  `fTestkeywordRankBegin` int(11) NULL DEFAULT NULL COMMENT '测试词排名区间头',
  `fTestkeywordRankEnd` int(11) NULL DEFAULT NULL COMMENT '测试词排名区间尾',
  `fOptimizePlanCount` int(11) NULL DEFAULT NULL COMMENT '刷量',
  `fStatus` tinyint(4) NULL DEFAULT NULL COMMENT '状态 0：暂停 1：激活',
  `fExecuteQueryTime` datetime NULL DEFAULT NULL COMMENT '计划取出查询时间',
  `fCreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fUpdateTime` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`fUuid`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '算法测试任务表' ;

#任务表
DROP TABLE IF EXISTS `t_algorithm_test_task`;
CREATE TABLE `t_algorithm_test_task`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '算法测试任务ID',
  `fAlgorithmTestPlanUuid` int(11) NOT NULL COMMENT '算法测试计划表id',
  `fKeywordGroup` varchar(255) NULL DEFAULT NULL COMMENT '关键字分组（由算法测试计划名称+日期组合而成）',
  `fCustomerName` varchar(255)  NULL DEFAULT NULL COMMENT '客户名称（由算法测试计划名称+日期组成）',
  `fActualKeywordCount` int(11) NULL DEFAULT NULL COMMENT '实际测试词数',
  `fStartDate` date NULL DEFAULT NULL COMMENT '开始测试日期',
  `fCreateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fUpdateTime` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`fUuid`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '算法测试任务表' ;