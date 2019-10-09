#添加客户管理一级菜单下子项“前往客户列表”
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,  `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('前往客户列表', '/internal/customer/toCustomers', 'fi-thumbnails',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "客户管理"), 1, 0, 1, 0, NOW());

# 将前往客户列表权限分配给技术角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
      (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '前往客户列表') tem_resource);




#添加关键字管理一级菜单下子项“前往客户关键字列表”按钮权限
INSERT INTO `db_keyword`.`t_resource`(`fResourceName`, `fUrl`, `fIconCls`,  `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`)
VALUES ('前往客户关键字列表', '/internal/customerKeyword/toCustomerKeywords', 'fi-thumbnails',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理"), 1, 0, 1, 1, NOW());

# 将前往客户关键字列表按钮权限分配给技术角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
    (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
    (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '前往客户关键字列表') tem_resource);


