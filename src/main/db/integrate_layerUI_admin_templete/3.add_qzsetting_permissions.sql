
# 修改所选站点操作组合
INSERT INTO t_resource
VALUES (null, "修改所选站点操作组合", "/internal/group/updateGroupOperationCombineUuid", 'ajax', null, null,
        (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "全站设置信息"), 0, 0,
        1, 1, null, NOW());
#将全站设置信息下的修改所选站点操作组合子权限赋给运营人员, 技术人员
insert into t_role_resource(fRoleID, fResourceId)
select tem_role.fUuid, tem_resource.fUuid
from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Operation', 'Technical')) tem_role,
      (select t.fUuid
       from t_resource tr
                JOIN t_resource t on tr.fUUid = t.fParentID
       where tr.fResourceName = '全站设置信息'
         AND t.fResourceName = '修改所选站点操作组合') tem_resource);


# 修改续费状态
INSERT INTO t_resource
VALUES (null, "修改续费状态", "/internal/qzsetting/updateRenewalStatus", 'ajax', null, null,
        (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "全站设置信息"), 0, 0,
        1, 1, null, NOW());
#将全站设置信息下的修改续费状态子权限赋给运营人员, 技术人员
insert into t_role_resource(fRoleID, fResourceId)
select tem_role.fUuid, tem_resource.fUuid
from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Operation', 'Technical')) tem_role,
      (select t.fUuid
       from t_resource tr
                JOIN t_resource t on tr.fUUid = t.fParentID
       where tr.fResourceName = '全站设置信息'
         AND t.fResourceName = '修改续费状态') tem_resource);