
ALTER TABLE `db_keyword`.`t_qz_setting` ADD COLUMN `fRenewalStatus` TINYINT(1) DEFAULT 1 COMMENT '续费状态 1: 续费 0: 暂停续费' AFTER `fStatus`;

# 原采集状态为 暂停状态(3)的修改为 续费状态为暂停续费(0) 采集状态为暂停(0)
UPDATE t_qz_setting SET fRenewalStatus = 0, fStatus = 0 WHERE fStatus = 3;

# 暴涨暴跌不仅需要涨幅指数大于小于指定值，还需要一周增长词数大于小于指定值
ALTER TABLE `db_keyword`.`t_qz_keyword_rank_info` ADD COLUMN `fOneWeekDifference` INT(11) DEFAULT 0 COMMENT '一周top10差值' AFTER `fTodayDifference`;

# 一周涨幅词数，配置在t_config 表
INSERT INTO t_config VALUES('QZSettingKeywordRank', 'OneWeekDiff', '10');
