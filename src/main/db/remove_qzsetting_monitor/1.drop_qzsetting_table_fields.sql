# 移除达标监控，达标计划字段
ALTER TABLE `db_keyword`.`t_qz_setting` DROP COLUMN fIsMonitor;
ALTER TABLE `db_keyword`.`t_qz_setting` DROP COLUMN fIsReady;