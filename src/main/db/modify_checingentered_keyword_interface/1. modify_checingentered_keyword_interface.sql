# 修改fEnteredKeywordRemarks字段名
alter table t_customer_keyword change fEnteredKeywordRemarks fFailedCause VARCHAR(50) COMMENT '失败原因';

# 原fEnteredKeywordRemarks字段的值置空
UPDATE t_customer_keyword SET fFailedCause = NULL WHERE fFailedCause > '';


# 开启无效关键字检查定时任务
UPDATE t_config SET fValue = 1 WHERE fConfigType = 'NoEnteredKeywordScheduleSwitch' AND fKey = 'SwitchNumber';

# 如果不存在
#INSERT INTO t_config VALUES('NoEnteredKeywordScheduleSwitch', 'SwitchNumber', 1);