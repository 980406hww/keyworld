
#更新资源名称为“机器分组工作统计”的记录为“机器分组刷量统计”
-- UPDATE t_resource SET fResourceName = "机器分组刷量统计",fSequence= 2  WHERE fResourceName = "机器分组工作统计";

#添加机器分组统计标签栏
-- INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`,
-- 	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`)
-- VALUES ('终端机器分组统计', '/internal/machineInfo/machineInfoMachineGroupStat', NULL, NULL, 'fi-thumbnails',
-- 	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "终端管理"), 4, 0, 1, 0, NULL, NOW());

#添加机器分组查看权限
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`)
VALUES ('前往测试计划添加页面', '/internal/algorithmAutoTest/toAlgorithmTestPlanAdd', 'ajax', NULL,null,
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "前往测试计划列表"), 0, 0, 1, 1, NULL, NOW());


#增加终端管理下新的子权限赋给运营人员, 技术人员和部门经理权限
INSERT INTO t_role_resource(fRoleID, fResourceId) SELECT tem_role.fUuid, tem_resource.fUuid FROM
((SELECT r.fUuid FROM t_role r WHERE r.fRoleName = 'Technical')
tem_role, (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '前往测试计划添加页面') tem_resource);


#添加机器分组查看权限
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`)
VALUES ('测试计划添加数据', '/internal/algorithmAutoTest/postAlgorithmTestPlanAddData', 'ajax', NULL,null,
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "前往测试计划列表"), 0, 0, 1, 1, NULL, NOW());


#增加终端管理下新的子权限赋给运营人员, 技术人员和部门经理权限
INSERT INTO t_role_resource(fRoleID, fResourceId) SELECT tem_role.fUuid, tem_resource.fUuid FROM
((SELECT r.fUuid FROM t_role r WHERE r.fRoleName = 'Technical')
tem_role, (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '测试计划添加数据') tem_resource);

#添加机器分组查看权限
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`)
VALUES ('获得测试计划添加页面初始数据', '/internal/algorithmAutoTest/getAlgorithmTestPlanAddData', 'ajax', NULL,null,
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "前往测试计划列表"), 0, 0, 1, 1, NULL, NOW());

#增加终端管理下新的子权限赋给运营人员, 技术人员和部门经理权限
INSERT INTO t_role_resource(fRoleID, fResourceId) SELECT tem_role.fUuid, tem_resource.fUuid FROM
((SELECT r.fUuid FROM t_role r WHERE r.fRoleName = 'Technical')
tem_role, (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '获得测试计划添加页面初始数据') tem_resource);

