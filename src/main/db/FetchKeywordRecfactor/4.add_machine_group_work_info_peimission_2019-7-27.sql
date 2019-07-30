#添加机器分组工作统计标签栏
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`)
VALUES ('机器分组统计', '/internal/machinegroupworkinfo/searchMachineGroupWorkInfos', NULL, NULL, 'fi-thumbnails',
	(SELECT r.fUuid from t_resource r where r.fUrl = '#' AND r.fResourceName = "终端管理"), 0, 0, 1, 0, NULL, NOW());


#添加查看权限
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`)
VALUES ('查询统计信息', '/internal/machinegroupworkinfo/searchMachineGroupWorkInfos', 'ajax', NULL,null,
	(SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "机器分组统计"), 0, 0, 1, 1, NULL, NOW());








#将所有分组设置信息子权限赋给运营人员, 技术人员和部门经理权限
insert into t_role_resource(fRoleID, fResourceId) select tem_role.fUuid, tem_resource.fUuid from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('DepartmentManager','Operation','Technical')) tem_role, (select t.fUuid from t_resource tr JOIN t_resource t on tr.fUUid = t.fParentID where tr.fResourceName = '终端管理') tem_resource);
