
#添加关键字管理一级菜单下子项“客户关键字管理”权限
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, fOpenMode,  `fCreateTime`, `fVersion`)
VALUES ('客户关键字管理', '/internal/customerKeyword/toCustomerKeywords', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "客户关键字管理" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 1, 0, 1, 0, '无(用于上层菜单)', NOW(), '2.0');
# 新二级菜单赋予给原角色
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '客户关键字管理' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '客户关键字管理' AND fVersion = '2.0') tem_resource);

#添加客户管理一级菜单下子项“关键字统计”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('关键字统计', '/internal/customerKeyword/toKeywords', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "关键字统计" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 2, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角色
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '关键字统计' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '关键字统计' AND fVersion = '2.0') tem_resource);

#添加关键字管理一级菜单下子项“单词统计”权限
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('单词统计', '/internal/customerKeyword/toPTKeywordCount', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "单词统计" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 3, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '单词统计' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '单词统计' AND fVersion = '2.0') tem_resource);

# 添加关键字管理一级菜单下子项“优质清单”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('优质清单', '/internal/positivelists/toSearchPositiveLists', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "优质清单" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 4, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '优质清单' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '优质清单' AND fVersion = '2.0') tem_resource);

#新增抓排名任务管理列表页面
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('抓排名任务管理', '/internal/captureRanks/toCaptureRank', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "抓排名任务管理" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 5, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角色
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '抓排名任务管理' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '抓排名任务管理' AND fVersion = '2.0') tem_resource);

#新增客户提交词页面
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('客户提交词', '/internal/keywordsInfo/toKeywordsInfo', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "客户提交词" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 6, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '客户提交词' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '客户提交词' AND fVersion = '2.0') tem_resource);

# 添加关键字管理一级菜单下子项“负面清单”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('负面清单', '/internal/negativelists/toSearchNegativeLists', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "负面清单" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 7, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '负面清单' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '负面清单' AND fVersion = '2.0') tem_resource);

#添加关键字管理一级菜单下子项“负面排名”权限
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('负面排名', '/internal/negativeRank/toNegativeRanks', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "负面排名" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 8, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '负面排名' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '负面排名' AND fVersion = '2.0') tem_resource);

#添加关键字管理一级菜单下子项“预警清单”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('预警清单', '/internal/warnlists/toSearchWarnLists', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "预警清单" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 9, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '预警清单' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '预警清单' AND fVersion = '2.0') tem_resource);

# 添加关键字管理一级菜单下子项“机器分组工作统计”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('机器分组工作统计', '/internal/customerKeyword/toMachineGroupAndSize', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "机器分组工作统计" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 10, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '机器分组工作统计' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '机器分组工作统计' AND fVersion = '2.0') tem_resource);

# 添加关键字管理一级菜单下子项“简化模板下载”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('简化模板下载', '/SuperUserSimpleKeywordList.xls', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "简化模板下载" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 11, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '简化模板下载' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '简化模板下载' AND fVersion = '2.0') tem_resource);

# 添加关键字管理一级菜单下子项“完整模板下载”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('完整模板下载', '/SuperUserFullKeywordList.xls', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "完整模板下载" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理" AND fVersion = '2.0'), 12, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '完整模板下载' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '完整模板下载' AND fVersion = '2.0') tem_resource);

# 新增供应商管理下权限为2.0的叶子节点权限，并赋予给原角色
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
	SELECT fParentID, fResourceName, fUrl, fIconCls, fSequence, fStatus, fOpened, fResourceType, fOpenMode FROM t_resource_new WHERE fParentID IN (SELECT fUuid FROM t_resource_new WHERE fParentID IN (SELECT fUuid FROM t_resource_new WHERE fUrl = '#' AND fParentID IS NULL AND fResourceName = '关键字管理' AND fVersion = '1.0'))
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

