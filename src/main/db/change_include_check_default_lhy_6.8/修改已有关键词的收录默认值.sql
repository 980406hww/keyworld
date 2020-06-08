# 修改收录状态
UPDATE `t_customer_keyword` SET fIncludeCheckTime = DATE_SUB( CURRENT_DATE ( ), INTERVAL 1 DAY ), fIncludeStatus = 1;