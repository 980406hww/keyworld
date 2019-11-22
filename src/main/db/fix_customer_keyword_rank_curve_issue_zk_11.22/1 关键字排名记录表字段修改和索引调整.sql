# 删除字段 
ALTER TABLE `db_keyword`.`t_ck_position_summary` DROP fPCPosition;
ALTER TABLE `db_keyword`.`t_ck_position_summary` DROP fJisuPosition;
ALTER TABLE `db_keyword`.`t_ck_position_summary` DROP fChupingPosition;
# 新增字段
ALTER TABLE `db_keyword`.`t_ck_position_summary` ADD fSearchEngine VARCHAR(100) DEFAULT NULL COMMENT '搜索引擎' AFTER `fCustomerKeywordUuid` ;
ALTER TABLE `db_keyword`.`t_ck_position_summary` ADD fTerminalType VARCHAR(10) DEFAULT NULL COMMENT '终端' AFTER `fSearchEngine` ;
ALTER TABLE `db_keyword`.`t_ck_position_summary` ADD fCustomerUuid INT DEFAULT NULL COMMENT '客户id' AFTER `fTerminalType` ;
ALTER TABLE `db_keyword`.`t_ck_position_summary` ADD fType VARCHAR(10) DEFAULT NULL COMMENT '类型' AFTER `fCustomerUuid` ;

# 拼接删除索引的语法 
# 执行完，运行结果SQL
SELECT i.TABLE_NAME, i.COLUMN_NAME, i.INDEX_NAME, 
CONCAT('ALTER TABLE ', 't_ck_position_summary', ' DROP INDEX ',i.INDEX_NAME,' ;') 
FROM INFORMATION_SCHEMA.STATISTICS i
WHERE TABLE_SCHEMA = 'db_keyword' AND TABLE_NAME = 't_ck_position_summary' AND i.INDEX_NAME <> 'PRIMARY'
GROUP BY i.INDEX_NAME;

# 建立索引
CREATE INDEX NewIndex1 ON `db_keyword`.`t_ck_position_summary`(fCustomerKeywordUuid, fCreateDate);
CREATE INDEX NewIndex2 ON `db_keyword`.`t_ck_position_summary`(fSearchEngine, fTerminalType, fPosition, fCustomerUuid, fType, fCreateDate);
CREATE INDEX NewIndex3 ON `db_keyword`.`t_ck_position_summary`(fCustomerUuid, fPosition, fCreateDate);