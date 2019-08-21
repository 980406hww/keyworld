

ALTER TABLE `db_keyword`.`t_qz_setting` MODIFY COLUMN `fAutoCrawlKeywordFlag` TINYINT DEFAULT 0 NULL COMMENT '是否自动运营 0：否、1是';

# 字段数据置为默认值
UPDATE t_qz_setting SET fAutoCrawlKeywordFlag = 0, fPcKeywordExceedMaxCount = 0, fPhoneKeywordExceedMaxCount = 0;