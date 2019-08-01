
#抓排名任务管理新增字段 fRankJobCity
ALTER TABLE `db_keyword`.`t_capture_rank_job`
		ADD COLUMN `fRankJobCity` varchar(50) NULL COMMENT '抓排名任务城市(广州，上海，深圳等等)'after `fRankJobArea` ;

#向t_config表插入RankJobCity数据，以逗号`,`分隔，添加城市时使用

INSERT INTO `db_keyword`.`t_config` VALUES('RankJobCity','RankJobCity','广州,深圳,上海,杭州,北京,贵阳,哈尔滨');
