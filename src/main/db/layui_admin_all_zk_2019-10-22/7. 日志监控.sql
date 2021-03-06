
#添加其他一级菜单下子项“登录日志”权限
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('登录日志', '/internal/systemLog/toSystemLog', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "登录日志" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "日志监控" AND fVersion = '2.0'), 5, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角色
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '登录日志' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '登录日志' AND fVersion = '2.0') tem_resource);

#添加其他一级菜单下子项“Druid监控”权限
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('Druid监控', '/druid', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "Druid监控" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "日志监控" AND fVersion = '2.0'), 5, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角色
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = 'Druid监控' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = 'Druid监控' AND fVersion = '2.0') tem_resource);

# 新增日志监控下权限为2.0的叶子节点权限，并赋予给原角色
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
	SELECT fParentID, fResourceName, fUrl, fIconCls, fSequence, fStatus, fOpened, fResourceType, fOpenMode FROM t_resource_new WHERE fParentID IN (SELECT fUuid FROM t_resource_new WHERE fParentID IN (SELECT fUuid FROM t_resource_new WHERE fUrl = '#' AND fParentID IS NULL AND fResourceName = '日志监控' AND fVersion = '1.0') AND fResourceName NOT IN ('系统图标'))
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

