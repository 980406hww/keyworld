# 删除旧索引
ALTER TABLE `t_customer_keyword` DROP INDEX fIncludeCheckTime;

# 创建新索引
ALTER TABLE `t_customer_keyword` ADD INDEX CheckKeywordIncludeStatus(fFailedCause, fIncludeCheckTime);
