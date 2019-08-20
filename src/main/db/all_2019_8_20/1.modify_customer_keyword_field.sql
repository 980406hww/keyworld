
#修改fCapturePositionQueryTime默认时间
ALTER TABLE t_customer_keyword MODIFY COLUMN fCapturePositionQueryTime datetime  DEFAULT '2019-07-18 00:00:00' COMMENT '抓排名取词查询时间'

#给旧数据fCapturePositionQueryTime字段值为空的记录赋值
UPDATE t_customer_keyword SET fCapturePositionQueryTime = '2019-07-18 00:00:00' WHERE fCapturePositionQueryTime IS NULL