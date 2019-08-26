
#关键字数量统计
INSERT INTO t_resource ( fResourceName, fUrl, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType ,fCreateTime)
    VALUES ( '单词统计', '/internal/customerKeyword/searchKeywordAmountCountLists', 'fi-compass',
        (SELECT r.fUuid FROM t_resource r WHERE r.fUrl = '#' AND r.fResourceName = "关键字管理"), 2, 0, 1, 0, NOW());


#关键字数量统计权限赋给技术,SEO,运维,运维和销售
INSERT INTO t_role_resource(fRoleID, fResourceId) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
        (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical', 'SEO', 'Maintenance', 'Operation', 'SEOSales')) tem_role,
        (SELECT tr.fUuid FROM t_resource tr WHERE tr.fResourceName = '单词统计') tem_resource);

