
#新增关键字抓排名日期、查询时间、抓取状态三个字段
ALTER TABLE `db_keyword`.`t_customer_keyword` ADD COLUMN `fRankDate` date  COMMENT '抓取日期' ;

ALTER TABLE `db_keyword`.`t_customer_keyword` ADD COLUMN `fRankQueryTime` datetime  COMMENT '抓取时间';

ALTER TABLE `db_keyword`.`t_customer_keyword` ADD COLUMN `fRankStatus` TINYINT(1) DEFAULT 0 COMMENT '0：未爬取、1：爬取中' ;

#初始化值
update t_customer_keyword set fRankDate = DATE_SUB( CURDATE(), INTERVAL 1 DAY ) , fRankQueryTime = DATE_SUB( NOW(), INTERVAL 1 DAY ),fRankSatus = 0;