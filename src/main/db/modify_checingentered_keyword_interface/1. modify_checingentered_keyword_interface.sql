# 修改fEnteredKeywordRemarks字段名
alter table t_customer_keyword change fEnteredKeywordRemarks fFailedCause VARCHAR(50) COMMENT '失败原因';

# 原fEnteredKeywordRemarks字段的值置空
UPDATE t_customer_keyword SET fFailedCause = NULL WHERE fFailedCause > '';