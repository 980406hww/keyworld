#新增每日排名涨幅详情页面
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,
	`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime` , `fVersion`)
VALUES ('每日排名涨幅详情', '/internal/ckpositionsummary/toCustomerKeywordPositionSummary', 'fi-thumbnails',
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "统计"), 1, 0, 1, 0, NOW(), '2.0');

# 将前往每日排名涨幅详情权限分配给技术角色
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '每日排名涨幅详情') tem_resource);