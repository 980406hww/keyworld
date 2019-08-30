
# 关键字添加 今日实际刷量字段
ALTER TABLE `db_keyword`.`t_customer_keyword` ADD COLUMN `fOptimizeTodayCount` INT(11) NULL DEFAULT NULL COMMENT '当天实际刷量' AFTER `fOptimizedCount`;
