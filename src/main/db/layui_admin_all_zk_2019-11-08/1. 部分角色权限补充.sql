## 把终端管理/机器管理/修改机器分组  的权限给到运维
insert into t_role_resource_new(fRoleID, fResourceId)
select tem_role.fUuid, tem_resource.fUuid
from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName = 'Maintenance') tem_role,
      (select t.fUuid
       from t_resource_new tr
                JOIN t_resource_new t on tr.fUUid = t.fParentID
       where tr.fResourceName = '机器管理' AND t.fResourceName = '修改机器分组' AND tr.fVersion = '2.0') tem_resource);