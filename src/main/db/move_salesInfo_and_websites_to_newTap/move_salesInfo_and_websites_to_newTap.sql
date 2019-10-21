#添加一级菜单“网站管理”
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('网站管理', '#', 45, 0, 1, 0, NOW());

 #添加网站管理一级菜单下子项“网站列表”
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('网站列表', '/internal/websites/toWebSiteList', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "网站管理"), 1, 0, 1, 0, NOW());

# 将前往关键字列表权限分配给技术角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
		(SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
		(SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '网站列表') tem_resource);



 #添加网站管理一级菜单下子项“销售信息列表”
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('销售信息列表', '/internal/salesInfo/toSalesInfo', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "网站管理"), 1, 0, 1, 0, NOW());

# 将前往关键字列表权限分配给技术角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
		(SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
		(SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '销售信息列表') tem_resource);