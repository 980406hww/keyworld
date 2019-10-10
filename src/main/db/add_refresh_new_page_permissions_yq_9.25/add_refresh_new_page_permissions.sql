#新增前往刷量统计页面
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('前往刷量统计', '/internal/refreshstatistics/toRefreshStatistics', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "终端管理"), 1, 0, 1, 0, NOW());

#新增前往机器分组统计页面
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('前往机器分组统计', '/internal/machinegroupstatistics/toMachineGroupStatistics', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "终端管理"), 1, 0, 1, 0, NOW());

#新增前往终端统计页面
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('前往终端统计', '/internal/machineManage/toMachineInfo', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "终端管理"), 1, 0, 1, 0, NOW());

#新增前往供应商列表页面
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('前往供应商列表', '/internal/suppliers/toSuppliers', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "供应商管理"), 1, 0, 1, 0, NOW());
# 将前往关键字统计权限分配给技术角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
      (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '前往供应商列表') tem_resource);

#新增前往抓排名任务管理列表页面
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('前往抓排名任务管理', '/internal/captureRanks/toCaptureRank', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理"), 1, 0, 1, 0, NOW());
# 将前往关键字统计权限分配给技术角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
      (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '前往抓排名任务管理') tem_resource);
