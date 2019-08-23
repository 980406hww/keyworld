
# 2019-8-13 t_daily_report 表
ALTER TABLE `db_keyword`.`t_daily_report` ADD COLUMN `fUserID` varchar(50) DEFAULT NULL COMMENT '所属人' AFTER `fUuid`;