#赋予统计下的权限
insert into t_role_resource_new(fRoleID, fResourceId)
select tem_role.fUuid, tem_resource.fUuid
from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('SEOSales', 'Operation', 'Maintenance', 'SEO')) tem_role,
      (select tr.fUuid
       from t_resource_new tr
       where tr.fResourceName = '统计' AND tr.fVersion = '2.0') tem_resource);
		 
# 赋予统计中心权限
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('SEOSales', 'Operation', 'Maintenance', 'SEO')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '统计中心' AND fVersion = '2.0') tem_resource);

# 赋予每日排名趋势权限
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('SEOSales', 'Operation', 'Maintenance', 'SEO')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '每日排名涨幅详情' AND fVersion = '2.0') tem_resource);

