
# 给销售添加下架、暂停关键字的权限
INSERT INTO t_role_resource(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
        (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('SEOSales')) tem_role,
        (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '修改关键字状态') tem_resource);