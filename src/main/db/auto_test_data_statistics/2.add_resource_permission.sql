INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime) VALUES
    ( '查看测试数据统计', '/internal/algorithmAutoTest/showTestDataStatistics','ajax', '',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW());

# 补全权限给Technical角色
INSERT INTO t_role_resource(fRoleID, fResourceId) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
    (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
    (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName IN ('查看测试数据统计')) tem_resource);