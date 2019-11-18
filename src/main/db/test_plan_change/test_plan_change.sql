ALTER TABLE `t_algorithm_test_plan`
CHANGE COLUMN `fOperationCombineName` `fOperationCombineId`  int(11) NULL DEFAULT NULL COMMENT '操作组合id' AFTER `fAlgorithmTestPlanName`