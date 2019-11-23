
# 菜单改按钮
UPDATE t_resource_new SET fResourceType = 1 WHERE fResourceName IN ('每日排名涨幅详情', '整站收费操作记录详情');

# 统计中心权限赋予排名销售,运营
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Operation', 'SEOSales')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '统计中心' AND fVersion = '2.0') tem_resource);

# 将整站收费操作记录详情权限分配给排名销售,运营
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Operation', 'SEOSales')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '整站收费操作记录详情') tem_resource);

# 将每日排名涨幅详情权限分配给排名销售,运营
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Operation', 'SEOSales')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '每日排名涨幅详情') tem_resource);

# 将每日站点趋势详情权限分配给排名销售,运营
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Operation', 'SEOSales')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '每日站点趋势详情') tem_resource);