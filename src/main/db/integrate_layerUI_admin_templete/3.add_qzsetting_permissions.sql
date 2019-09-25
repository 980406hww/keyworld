
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('前往全站信息设置', '/internal/qzsetting/toQZSetttings', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "其他"), 1, 0, 1, 0, NOW());


# 将前往关键字列表权限分配给运营, 技术, 整站销售, 优化角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
		(SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Operation', 'Technical', 'SEOSales', 'SEO')) tem_role,
		(SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '前往全站信息设置') tem_resource);


# 全站设置信息子资源移植
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_resource_data_migration`;
CREATE PROCEDURE pro_resource_data_migration()
BEGIN
	START TRANSACTION;
			BEGIN
			SET @old_uuid = (SELECT fUuid FROM t_resource WHERE fResourceName = "全站设置信息");
			SET @new_uuid = (SELECT fUuid FROM t_resource WHERE fResourceName = "前往全站信息设置");
			UPDATE t_resource SET fParentID = @new_uuid WHERE fParentID = @old_uuid;
			END;
	COMMIT;
END;

CALL pro_resource_data_migration();

DROP PROCEDURE pro_resource_data_migration;

# 删除原全站设置信息 资源
DELETE FROM t_resource WHERE fResourceName = '全站设置信息';


# 修改所选站点操作组合
INSERT INTO t_resource
VALUES (null, "修改所选站点操作组合", "/internal/group/updateGroupOperationCombineUuid", 'ajax', null, null,
        (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "前往全站信息设置"), 0, 0,
        1, 1, null, NOW());

#将全站设置信息下的修改所选站点操作组合子权限赋给运营人员, 技术人员
insert into t_role_resource(fRoleID, fResourceId)
select tem_role.fUuid, tem_resource.fUuid
from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Operation', 'Technical')) tem_role,
      (select t.fUuid
       from t_resource tr
                JOIN t_resource t on tr.fUUid = t.fParentID
       where tr.fResourceName = '前往全站信息设置'
         AND t.fResourceName = '修改所选站点操作组合') tem_resource);


# 修改续费状态
INSERT INTO t_resource
VALUES (null, "修改续费状态", "/internal/qzsetting/updateRenewalStatus", 'ajax', null, null,
        (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "前往全站信息设置"), 0, 0,
        1, 1, null, NOW());
#将全站设置信息下的修改续费状态子权限赋给运营人员, 技术人员
insert into t_role_resource(fRoleID, fResourceId)
select tem_role.fUuid, tem_resource.fUuid
from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Operation', 'Technical')) tem_role,
      (select t.fUuid
       from t_resource tr
                JOIN t_resource t on tr.fUUid = t.fParentID
       where tr.fResourceName = '前往全站信息设置'
         AND t.fResourceName = '修改续费状态') tem_resource);