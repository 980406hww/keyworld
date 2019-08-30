#激活关键字权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime) VALUES
	( '激活关键字', '/internal/customerKeyword/activateCustomerKeywords','ajax', '',
		(SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "客户关键字管理"), 0, 0, 1, 1, NOW() )



#将激活关键字权限分配给技术,运营角色
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
        (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical', 'Operation')) tem_role,
        (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '激活关键字') tem_resource);
