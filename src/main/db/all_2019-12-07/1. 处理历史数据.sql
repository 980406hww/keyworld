
# 处理操作-排名曲线的fFullDate的值
UPDATE t_qz_keyword_rank_info SET fFullDate = fBaiduRecordFullDate WHERE fWebsiteType = 'xt';

# 删除历史排名表的数据，由于bug导致数据有误，尝试修复，但是运行时间太长不能接受
DELETE FROM t_ck_position_summary WHERE fCustomerUuid IS NULL;;

# 处理任务城市空串 已处理，请忽略
UPDATE t_capture_rank_job SET fRankJobCity = NULL WHERE fRankJobCity = '';