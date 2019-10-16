 #添加其他一级菜单下子项“前往销售信息管理”
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('前往销售信息管理', '/internal/salesInfo/toSalesInfo', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "其他"), 1, 0, 1, 0, NOW());


# 将前往关键字列表权限分配给技术角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
		(SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
		(SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '前往销售信息管理') tem_resource);