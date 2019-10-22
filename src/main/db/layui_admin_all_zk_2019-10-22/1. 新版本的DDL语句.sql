# 修改不是整站销售的人的站点状态为其他状态
UPDATE t_qz_setting SET fRenewalStatus = 4 WHERE fCustomerUuid IN (
SELECT DISTINCT c.fUuid
FROM t_customer c
LEFT JOIN t_userinfo u 
ON c.fUserID = u.fLoginName
LEFT JOIN t_organization o 
ON o.fUuid = u.fOrganizationID
WHERE u.fstatus = 0 
AND o.fOrganizationName != '整站销售部');

# 修改销售部门的人的现有暂停续费站点状态为下架状态
UPDATE t_qz_setting SET fRenewalStatus = 3 WHERE fRenewalStatus = 0;

alter table t_qz_setting add fCaptureTerminalType varchar(40) null comment '抓取终端类型';

CREATE TABLE `t_qz_charge_status` (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fQZSettingUuid` int(11) NOT NULL COMMENT '全站对应表',
  `fChargeStatus` int(2) NOT NULL COMMENT '收费状态 1：续费中 0：暂停续费 2：首次收费 3：下架 4：其他',
  `fChargeMoney` decimal(10,0) DEFAULT '0' COMMENT '收费金额',
  `fCustomerSatisfaction` int(2) DEFAULT '5' COMMENT '客户满意度 5：特别满意 4：满意 3：中等合适 2：不满意 1：特别不满意',
  `fChargeStatusMsg` text COMMENT '状态备注',
  `fCreateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fLoginName` varchar(64) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`fUuid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='收费状态表';
# 全站关联收费状态表
alter table t_qz_setting add fChargeStatusUuid int null comment '最新一次收费状态Uuid';

#往config表插入数据
# 所有搜索引擎
INSERT INTO `db_keyword`.`t_config`(`fConfigType`, `fKey`, `fValue`) VALUES ('SearchEngine', 'All', '谷歌,百度,搜狗,必应中国,百度下拉,360,必应日本');

# 重点词的预设刷量
INSERT INTO `db_keyword`.`t_config`(`fConfigType`, `fKey`, `fValue`) VALUES ('KeywordEffectOptimizePlanCount', 'ImportantKeyword', '100');

# 复制一份t_resource, t_role_resource命名为t_resource_new, t_role_resource_new 来进行权限操作
# 项目里用的是新的表t_resource_new, t_role_resource_new
-- CREATE TABLE t_resource_new SELECT * FROM t_resource;
-- CREATE TABLE t_role_resource_new SELECT * FROM t_role_resource;

# 删除部门管理员角色关联的资源记录
DELETE FROM t_role_resource_new WHERE fRoleID = (SELECT fUuid FROM t_role WHERE fRoleName = 'DepartmentManager');

# 资源表增加版本控制，原资源默认版本为1.0
ALTER TABLE t_resource_new ADD fVersion varchar(10) DEFAULT NULL COMMENT '版本号' AFTER `fIconCls`;
UPDATE t_resource_new SET fVersion = 1.0;

# 新增除原资源一级菜单应用程序管理，日志监控，权限管理外的其他一级菜单，版本号为2.0
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_resource_data_migration`;
CREATE PROCEDURE pro_resource_data_migration()
BEGIN
	DECLARE g_resourceName VARCHAR(64) DEFAULT 0;
	DECLARE g_url VARCHAR(100) DEFAULT NULL;
	DECLARE g_sequence TINYINT(2) DEFAULT NULL;
	DECLARE g_status TINYINT(2) DEFAULT NULL;
	DECLARE g_opened TINYINT(2) DEFAULT NULL;
	DECLARE g_resource_type TINYINT(2) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE resource_data_migration_cursor CURSOR FOR(
	SELECT fResourceName, fUrl, fSequence, fStatus, fOpened, fResourceType FROM t_resource_new WHERE fUrl = '#' AND fParentID IS NULL AND fResourceName NOT IN("应用程序管理", "日志监控","权限管理")
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
		OPEN resource_data_migration_cursor;
			out_loop:
			LOOP
			FETCH resource_data_migration_cursor INTO g_resourceName, g_url, g_sequence, g_status, g_opened, g_resource_type;
			IF done = 1 THEN
			LEAVE out_loop;
			END IF;
			BEGIN
			INSERT INTO t_resource_new SET fResourceName = g_resourceName, fUrl = g_url, fIconCls = 'fi-thumbnails', fVersion='2.0', fSequence = g_sequence, fStatus = g_status, fOpened = g_opened, fResourceType = g_resource_type;
			
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = g_resourceName and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = g_resourceName AND fVersion = '2.0') tem_resource);
			END;
			SET done = 0;
			END LOOP out_loop;
		CLOSE resource_data_migration_cursor;
	COMMIT;
END;
CALL pro_resource_data_migration();
DROP PROCEDURE pro_resource_data_migration;




