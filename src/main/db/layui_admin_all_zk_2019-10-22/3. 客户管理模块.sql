
# 原网站联系信息列表权限修改
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_resource_data_migration`;
CREATE PROCEDURE pro_resource_data_migration()
BEGIN
	DECLARE g_uuid bigint(19) DEFAULT NULL;
	DECLARE g_parentID bigint(19) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE resource_data_migration_cursor CURSOR FOR(
	SELECT fUuid, fParentID FROM t_resource_new WHERE fResourceName = '网站联系信息列表' AND fVersion = '1.0'
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
		OPEN resource_data_migration_cursor;
			out_loop:
			LOOP
			FETCH resource_data_migration_cursor INTO g_uuid, g_parentID;
			IF done = 1 THEN
			LEAVE out_loop;
			END IF;
			BEGIN
			UPDATE t_resource_new SET fParentID = g_parentID WHERE fParentID = g_uuid;
			UPDATE t_resource_new SET fOpenMode = 'ajax' WHERE fUuid = g_uuid;
			END;
			SET done = 0;
			END LOOP out_loop;
		CLOSE resource_data_migration_cursor;
	COMMIT;
END;
CALL pro_resource_data_migration();
DROP PROCEDURE pro_resource_data_migration;
