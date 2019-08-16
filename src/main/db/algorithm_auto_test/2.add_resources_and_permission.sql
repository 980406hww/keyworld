#算法自动测试权限添加

#添加顶级菜单
INSERT INTO t_resource ( fResourceName, fUrl, fIconCls, fSequence, fStatus, fOpened, fResourceType,fCreateTime )
VALUES ( ''自动化管理'',''#'', '' fi-indent-less'', (select max(tr.fSequence+ 1)  FROM t_resource tr WHERE tr.furl = "#" and tr.fparentid is null), 0,1, 0 ,NOW())

#测试计划管理权限
INSERT INTO t_resource ( fResourceName, fUrl, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( ''测试计划管理'', ''/internal/algorithmAutoTest/searchAlgorithmTestPlans'', ''fi-compass'',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl = ''#'' AND r.fResourceName = "自动化管理"),
        (SELECT IFNULL(max(tr.fSequence)+1 ,1) FROM t_resource tr WHERE tr.furl != "#" AND tr.fparentid =''自动化管理''), 0, 1, 0 ,NOW()) ;

#查询权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( ''查询测试计划列表'', ''/internal/algorithmAutoTest/searchAlgorithmTestPlans'',''ajax'', '''',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != ''#'' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() )

#添加权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( ''添加测试计划'', ''/internal/algorithmAutoTest/saveAlgorithmTestPlan'',''ajax'', '''',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != ''#'' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() )

#删除权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( ''删除测试计划'', ''/internal/algorithmAutoTest/deleteAlgorithmTestPlan'',''ajax'', '''',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != ''#'' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() )

#查看计划任务执行情况权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
VALUES ( '查看任务历史', '/internal/algorithmAutoTest/showAlgorithmTestTask','ajax', '',
    (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != '#' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() )

#查看计划任务权限
INSERT INTO t_resource ( fResourceName, fUrl, fOpenMode, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( ''查看任务历史'', ''/internal/algorithmAutoTest/showAlgorithmTestTask'',''ajax'', '''',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl != ''#'' AND r.fResourceName = "测试计划管理"), 0, 0, 1, 1, NOW() )


#顶级菜单及其子菜单按钮权限授权
insert into t_role_resource(fRoleID, fResourceId) select tem_role.fUuid, tem_resource.fUuid from (
    (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (''DepartmentManager'',''Operation'',''Technical'')) tem_role,
    (select t.fUuid from t_resource tr JOIN t_resource t on tr.fUUid = t.fParentID where tr.fResourceName = ''自动化管理'') tem_resource);

insert into t_role_resource(fRoleID, fResourceId) select tem_role.fUuid, tem_resource.fUuid from (
    (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN (''DepartmentManager'',''Operation'',''Technical'')) tem_role,
    (select t.fUuid from t_resource tr JOIN t_resource t on tr.fUUid = t.fParentID where tr.fResourceName = ''测试计划管理'') tem_resource);
