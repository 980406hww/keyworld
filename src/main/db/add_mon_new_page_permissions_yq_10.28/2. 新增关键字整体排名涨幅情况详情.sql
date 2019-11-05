#新增前往关键字整体排名涨幅情况详情页面
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime` , `fVersion`)
VALUES ('前往关键字整体排名涨幅情况详情', '/internal/customerkeywordmon/toCustomerKeywordMon', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "日志监控"), 1, 0, 1, 0, NOW(), '2.0');

# 将前往关键字整体排名涨幅情况详情页面权限分配给技术角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
      (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '前往关键字整体排名涨幅情况详情') tem_resource);