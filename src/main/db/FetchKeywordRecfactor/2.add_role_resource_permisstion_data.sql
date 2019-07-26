#关键字管理权限
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, 
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) 
VALUES ('机器分组工作统计', '/internal/customerKeyword/showMachineGroupAndSize', NULL, NULL, 'fi-thumbnails', 
	(SELECT r.fUuid from t_resource r where r.fUrl = '#' AND r.fResourceName = "关键字管理"), 0, 0, 1, 0, NULL, NOW());


INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, 
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) 
VALUES ('修改机器分组', '/internal/customerKeyword/updateCustomerKeywordMachineGroup', 'ajax', NULL,null, 
	(SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "客户关键字管理"), 0, 0, 1, 0, NULL, NOW());

#机器管理权限

INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, 
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) 
VALUES ('修改机器分组', '/internal/machineInfo/updateMachineGroup', 'ajax', NULL,null, 
	(SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "机器管理"), 0, 0, 1, 0, NULL, NOW());

INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, 
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) 
VALUES ('修改优化组', '/internal/machineInfo/batchUpdateGroup', 'ajax', NULL,null, 
	(SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "机器管理"), 0, 0, 1, 0, NULL, NOW());




#将所有分组设置信息子权限赋给运营人员, 技术人员和部门经理权限
insert into t_role_resource(fRoleID, fResourceId) select tem_role.fUuid, tem_resource.fUuid from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('DepartmentManager','Operation','Technical')) tem_role, (select t.fUuid from t_resource tr JOIN t_resource t on tr.fUUid = t.fParentID where tr.fResourceName = '机器管理') tem_resource);
insert into t_role_resource(fRoleID, fResourceId) select tem_role.fUuid, tem_resource.fUuid from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('DepartmentManager','Operation','Technical')) tem_role, (select t.fUuid from t_resource tr JOIN t_resource t on tr.fUUid = t.fParentID where tr.fResourceName = '客户关键字管理') tem_resource);

insert into t_role_resource(fRoleID, fResourceId) select tem_role.fUuid, tem_resource.fUuid from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('DepartmentManager','Operation','Technical')) tem_role, (select t.fUuid from t_resource tr JOIN t_resource t on tr.fUUid = t.fParentID where tr.fResourceName = '关键字管理') tem_resource);