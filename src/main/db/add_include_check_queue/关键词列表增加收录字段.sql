# 关键词表增加收录字段
ALTER TABLE `t_customer_keyword` ADD COLUMN `fIncludeStatus` INT(11) DEFAULT 1 COMMENT '关键词收录状态' AFTER `fFailedCause`;
ALTER TABLE `t_customer_keyword` ADD COLUMN `fIncludeCheckTime` datetime DEFAULT NULL COMMENT '收录查询时间' AFTER `fIncludeStatus`;

# 查询所需的索引
ALTER TABLE `t_customer_keyword` ADD INDEX fIncludeCheckTime (fIncludeCheckTime,fFailedCause);