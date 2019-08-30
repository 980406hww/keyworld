#在algorithm_auto_test分支上的资源权限SQL有问题,查看任务的资源sql写了两条，漏了修改计划状态的资源sql

#删除重复的查看任务资源和权限
DELETE FROM t_role_resource WHERE fResourceID IN (SELECT fUuid FROM t_resource WHERE fUrl = '/internal/algorithmAutoTest/showAlgorithmTestTask');

DELETE FROM t_resource WHERE furl = '/internal/algorithmAutoTest/showAlgorithmTestTask';

#添加改变计划状态权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime) VALUES
    ( '改变测试计划状态', '/internal/algorithmAutoTest/changeAlgorithmTestPlanStatus','ajax', '',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() ) ;

#添加查看计划任务执行情况权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime) VALUES
    ( '查看任务历史', '/internal/algorithmAutoTest/showAlgorithmTestTask','ajax', '',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() );

# 补全权限给Technical角色
INSERT INTO t_role_resource(fRoleID, fResourceId) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
    (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
    (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName IN ('改变测试计划状态','查看任务历史', '添加测试计划')) tem_resource);
