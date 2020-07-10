# 增加无效天数的字段
ALTER TABLE t_customer_keyword ADD COLUMN `fInvalidDays` int(4) NOT NULL DEFAULT 0 COMMENT '无效天数' AFTER fFailedCause;

# 设置暂停操作的无效天数最大值
INSERT INTO t_config (fConfigType, fKey, fValue) VALUES ("InvalidDays", "MaxDays", 3);

# 删除收录状态相关字段和索引
ALTER TABLE t_customer_keyword DROP COLUMN `fIncludeStatus`, DROP COLUMN `fIncludeCheckTime`, DROP INDEX CheckKeywordIncludeStatus;