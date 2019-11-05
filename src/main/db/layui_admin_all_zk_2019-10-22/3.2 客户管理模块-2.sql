
# 新增客户管理下权限为2.0的叶子节点权限，并赋予给原角色
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_resource_data_migration`;
CREATE PROCEDURE pro_resource_data_migration()
BEGIN
	DECLARE g_parentID bigint(19) DEFAULT NULL;
	DECLARE g_resourceName VARCHAR(64) DEFAULT 0;
	DECLARE g_url VARCHAR(100) DEFAULT NULL;
	DECLARE g_iconCls VARCHAR(32) DEFAULT NULL;
	DECLARE g_sequence TINYINT(2) DEFAULT 0;
	DECLARE g_status TINYINT(2) DEFAULT 0;
	DECLARE g_opened TINYINT(2) DEFAULT 1;
	DECLARE g_resource_type TINYINT(2) DEFAULT 0;
	DECLARE g_openMode VARCHAR(32) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE resource_data_migration_cursor CURSOR FOR(
	SELECT fParentID, fResourceName, fUrl, fIconCls, fSequence, fStatus, fOpened, fResourceType, fOpenMode FROM t_resource_new WHERE fParentID IN (SELECT fUuid FROM t_resource_new WHERE fParentID IN (SELECT fUuid FROM t_resource_new WHERE fUrl = '#' AND fParentID IS NULL AND fResourceName = '客户管理' AND fVersion = '1.0') AND fResourceName NOT IN ('客户收费', '负面达标设置'))
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
		OPEN resource_data_migration_cursor;
			out_loop:
			LOOP
			FETCH resource_data_migration_cursor INTO g_parentID, g_resourceName, g_url, g_iconCls, g_sequence, g_status, g_opened, g_resource_type, g_openMode;
			IF done = 1 THEN
			LEAVE out_loop;
			END IF;
			BEGIN
			SET @new_pid = (
				SELECT fUuid FROM t_resource_new WHERE fVersion = '2.0' AND fResourceName = (SELECT fResourceName FROM t_resource_new WHERE fUuid = g_parentID)
			);
			INSERT INTO t_resource_new SET fResourceName = g_resourceName, fUrl = g_url, fIconCls = g_iconCls, fVersion='2.0', fParentID=@new_pid, fSequence = g_sequence, fStatus = g_status, fOpened = g_opened, fResourceType = g_resource_type, fOpenMode = g_openMode;
			
			INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (SELECT fRoleName from t_role WHERE fUuid IN (SELECT fRoleID FROM t_role_resource_new WHERE fResourceID IN (SELECT fUuid from t_resource_new WHERE fResourceName = g_resourceName AND fVersion = '1.0' AND fUrl = g_url)))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = g_resourceName AND fVersion = '2.0' AND fUrl = g_url) tem_resource);
			END;
			SET done = 0;
			END LOOP out_loop;
		CLOSE resource_data_migration_cursor;
	COMMIT;
END;
CALL pro_resource_data_migration();
DROP PROCEDURE pro_resource_data_migration;


