
#刷量统计记录新增失败关键字数 字段
ALTER TABLE `db_keyword`.`t_ck_refresh_stat_record`
		ADD COLUMN `fFailedKeywordCount` int(11) NULL COMMENT '失败关键字数'after `fInvalidKeywordCount` ;