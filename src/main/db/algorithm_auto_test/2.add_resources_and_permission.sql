#算法自动测试权限添加

#添加顶级菜单
INSERT INTO t_resource ( fResourceName, fUrl, fIconCls, fSequence, fStatus, fOpened, fResourceType,fCreateTime )
VALUES ( '自动化管理','#', 'fi-indent-less', (SELECT MAX(tr.fSequence+ 1)  FROM t_resource tr WHERE tr.furl = "#" AND tr.fparentid IS NULL), 0,1, 0 ,NOW());

#测试计划管理权限
INSERT INTO t_resource ( fResourceName, fUrl, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( '测试计划管理', '/internal/algorithmAutoTest/searchAlgorithmTestPlans', 'fi-compass',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "自动化管理"),
        (SELECT IFNULL(MAX(tr.fSequence)+1 ,1) FROM t_resource tr WHERE tr.furl != "#" AND tr.fparentid ='自动化管理'), 0, 1, 0 ,NOW()) ;

#查询权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( '查询测试计划列表', '/internal/algorithmAutoTest/searchAlgorithmTestPlans','ajax', '',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() );

#添加权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( '添加测试计划', '/internal/algorithmAutoTest/saveAlgorithmTestPlan','ajax', '',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() );

#删除权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( '删除测试计划', '/internal/algorithmAutoTest/deleteAlgorithmTestPlan','ajax', '',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() );

#查看计划任务执行情况权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
VALUES ( '查看任务历史', '/internal/algorithmAutoTest/showAlgorithmTestTask','ajax', '',
    (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() );

#查看计划任务权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( '查看任务历史', '/internal/algorithmAutoTest/showAlgorithmTestTask','ajax', '',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() );


#顶级菜单及其子菜单按钮权限授权
INSERT INTO t_role_resource(fRoleID, fResourceId) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
    (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
    (SELECT t.fUuid FROM t_resource tr JOIN t_resource t ON tr.fUUid = t.fParentID WHERE tr.fResourceName = '自动化管理') tem_resource);

INSERT INTO t_role_resource(fRoleID, fResourceId) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
    (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical')) tem_role,
    (SELECT t.fUuid FROM t_resource tr JOIN t_resource t ON tr.fUUid = t.fParentID WHERE tr.fResourceName = '测试计划管理') tem_resource);
