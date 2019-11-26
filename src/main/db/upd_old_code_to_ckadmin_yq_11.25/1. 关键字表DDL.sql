
# 关键字表加字段fCapturePositionFailIdentify
ALTER TABLE `db_keyword`.`t_customer_keyword` ADD fCapturePositionFailIdentify INT DEFAULT NULL COMMENT ''抓排名失败标识'' AFTER `fCapturePositionQueryTime` ;