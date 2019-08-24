# 关键字状态新增
ALTER TABLE `db_keyword`.`t_customer_keyword` MODIFY COLUMN `fStatus` int(11) NULL DEFAULT 0 COMMENT '关键字状态 0-暂不操作，1-激活，2-新增  3-下架';


# 机器分组为Pause的关键字设置状态
UPDATE t_customer_keyword SET fStatus = 0 WHERE fMachineGroup ='Pause';

# 分组为 暂停(zanting)的关键字设置状态为暂停 0
UPDATE t_customer_keyword SET fStatus = 0 WHERE fOptimizeGroupName IN ('zanting', 'ZANTING');


# 分组为 下架(stop)的关键字设置状态为下架 3
UPDATE t_customer_keyword SET fStatus = 3 WHERE fOptimizeGroupName IN ('stop', 'sotp');


