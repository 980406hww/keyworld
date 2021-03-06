
# 新增网站管理的顶级菜单，子权限为网站列表（原网站管理），销售信息列表（原销售信息管理）
#添加一级菜单“网站管理”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)VALUES ('网站管理', '#', 'fi-thumbnails', 50, 0, 1, 0, NOW(), '2.0');

#将网站管理权限赋给SEO人员, 技术人员
insert into t_role_resource_new(fRoleID, fResourceId)
select tem_role.fUuid, tem_resource.fUuid
from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('SEO', 'Technical')) tem_role,
      (select tr.fUuid
       from t_resource_new tr
       where tr.fResourceName = '网站管理' AND tr.fVersion = '2.0') tem_resource);

DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_resource_data_migration`;
CREATE PROCEDURE pro_resource_data_migration ()
BEGIN
	START TRANSACTION;
			BEGIN
			SET @uuid = (
					SELECT fUuid FROM t_resource_new WHERE fVersion = '2.0' AND fResourceName = "网站管理" AND fUrl = '#' AND fParentID IS NULL
			);
			UPDATE t_resource_new SET fParentID = @uuid, fResourceName = "网站列表" WHERE fResourceName = "网站管理" AND fVersion = 2.0 AND fParentID > '';
			UPDATE t_resource_new SET fParentID = @uuid, fResourceName = "销售信息列表" WHERE fResourceName = "销售信息管理" AND fVersion = 2.0 AND fParentID > '';
			END;
	COMMIT;
END;
CALL pro_resource_data_migration();
DROP PROCEDURE pro_resource_data_migration;

# 菜单修改成按钮
UPDATE t_resource_new SET fResourceType = 1 WHERE fResourceName = '修改机器分组';
UPDATE t_resource_new SET fResourceType = 1 WHERE fResourceName = '客户关键字管理' AND fVersion = '2.0';
UPDATE t_resource_new SET fResourceType = 1 WHERE fResourceName = '修改优化组';

# 处理历史数据，所有的自动采集都设置为0，时间间隔设置为15天
UPDATE t_qz_setting SET fAutoCrawlKeywordFlag = 0, fUpdateInterval = 15 WHERE fAutoCrawlKeywordFlag IS TRUE OR fUpdateInterval != 15 OR fUpdateStatus > '';
