
# 处理运营记录关联站点id  电脑端
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_ub_qz_pc_migration`;
CREATE PROCEDURE pro_ub_qz_pc_migration ( )
BEGIN
	DECLARE qz_uuid INT ( 11 ) DEFAULT NULL;
	DECLARE c_uuid INT ( 11 ) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE ub_qz_pc_cursor CURSOR FOR (
			SELECT fUuid, fCustomerUuid
			FROM t_qz_setting qs
			WHERE EXISTS (
				SELECT 1
				FROM t_user_notebook un
				WHERE un.fCustomerUuid = qs.fCustomerUuid
				AND fTerminalType = 'PC'
			)
			AND qs.fPcGroup > ''
		);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
	OPEN ub_qz_pc_cursor;
	out_loop:
	LOOP
		FETCH ub_qz_pc_cursor INTO qz_uuid, c_uuid;
		IF done = 1 THEN
			LEAVE out_loop;
		END IF;
		BEGIN
				UPDATE t_user_notebook SET fQzUuid = qz_uuid WHERE fCustomerUuid = c_uuid AND fTerminalType = 'PC';
		END;
		SET done = 0;
	END LOOP out_loop;
	CLOSE ub_qz_pc_cursor;
	COMMIT;
END;
CALL pro_ub_qz_pc_migration();
DROP PROCEDURE pro_ub_qz_pc_migration;

# 处理运营记录关联站点id  移动端
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_ub_qz_phone_migration`;
CREATE PROCEDURE pro_ub_qz_phone_migration ( )
BEGIN
	DECLARE qz_uuid INT ( 11 ) DEFAULT NULL;
	DECLARE c_uuid INT ( 11 ) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE ub_qz_phone_cursor CURSOR FOR (
			SELECT fUuid, fCustomerUuid
			FROM t_qz_setting qs
			WHERE EXISTS (
				SELECT 1
				FROM t_user_notebook un
				WHERE un.fCustomerUuid = qs.fCustomerUuid
				AND fTerminalType = 'Phone'
			)
			AND qs.fPhoneGroup > ''
		);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
	OPEN ub_qz_phone_cursor;
	out_loop:
	LOOP
		FETCH ub_qz_phone_cursor INTO qz_uuid, c_uuid;
		IF done = 1 THEN
			LEAVE out_loop;
		END IF;
		BEGIN
				UPDATE t_user_notebook SET fQzUuid = qz_uuid WHERE fCustomerUuid = c_uuid AND fTerminalType = 'Phone';
		END;
		SET done = 0;
	END LOOP out_loop;
	CLOSE ub_qz_phone_cursor;
	COMMIT;
END;
CALL pro_ub_qz_phone_migration();
DROP PROCEDURE pro_ub_qz_phone_migration;

# 处理站点已删除的运营记录
DELETE FROM t_user_notebook WHERE fQzUuid IS NULL;

# 删除字段fCustomerUuid
ALTER TABLE t_user_notebook DROP fCustomerUuid;