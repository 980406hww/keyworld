
# 2020-6-28 t_pt_customer_keyword表 fMark
ALTER TABLE `db_keyword`.`t_pt_customer_keyword` ADD COLUMN `fMark` TINYINT(4) DEFAULT 0 COMMENT '同步标识' AFTER `fCapturePositionCity`;