
#添加客户管理一级菜单下子项“客户列表”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('客户列表', '/internal/customer/toCustomers', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "客户列表" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "客户管理" AND fVersion = '2.0'), 1, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角色
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '客户列表' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '客户列表' AND fVersion = '2.0') tem_resource);
			
# 添加客户管理一级菜单下子项“行业列表”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('行业列表', '/internal/industryList/toIndustryList', (SELECT fIconCls FROM t_resource_new r WHERE r.fResourceName = "行业列表" AND fVersion = '1.0'),
	(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "客户管理" AND fVersion = '2.0'), 2, 0, 1, 0, NOW(), '2.0');
# 新二级菜单赋予给原角色
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (select fRoleName from t_role where fUuid in (select fRoleID from t_role_resource_new where fResourceID in (select fUuid from t_resource_new where fResourceName = '行业列表' and fVersion = '1.0')))) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '行业列表' AND fVersion = '2.0') tem_resource);
