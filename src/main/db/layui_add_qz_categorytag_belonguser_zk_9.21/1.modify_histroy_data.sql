# 给全站分组标签表新增所属用户字段
ALTER TABLE `db_keyword`.`t_qz_category_tag` ADD COLUMN `fBelongUser` VARCHAR(50) DEFAULT NULL COMMENT '所属用户' AFTER `fTagName`;

# 利用存储过程和游标进行数据迁移
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_qs_categorytag_data_migration`;
CREATE PROCEDURE pro_qs_categorytag_data_migration()
BEGIN
DECLARE gct_uuid INT DEFAULT 0;
DECLARE logine_name VARCHAR(50);
DECLARE done INT DEFAULT 0;
DECLARE qs_categorytag_data_migration_cursor CURSOR FOR (
	SELECT
		qct.fUuid,
		c.fUserID
	FROM t_customer c
	LEFT JOIN t_qz_setting qs ON c.fUuid = qs.fCustomerUuid
	JOIN t_qz_category_tag qct ON qct.fQZSettingUuid = qs.fUuid
	GROUP BY qct.fUuid
) ;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
		OPEN qs_categorytag_data_migration_cursor;
			out_loop:
			LOOP
			FETCH qs_categorytag_data_migration_cursor INTO gct_uuid, logine_name;
			IF done = 1 THEN
				LEAVE out_loop;
			END IF;
			BEGIN
			UPDATE t_qz_category_tag SET fBelongUser = logine_name WHERE fUuid = gct_uuid;
			END;
			SET done = 0;
			END LOOP out_loop;
		CLOSE qs_categorytag_data_migration_cursor;
	COMMIT;
END;

CALL pro_qs_categorytag_data_migration();

# 数据迁移成功后，删除没有关联的分组标签
DELETE FROM t_qz_category_tag WHERE fBelongUser IS NULL;