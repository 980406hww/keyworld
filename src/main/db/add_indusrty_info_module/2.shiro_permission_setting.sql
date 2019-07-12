
#添加行业列表 二级目录
INSERT INTO t_resource VALUES (null, "行业列表", "/internal/industry/searchIndustries", null, null, "fi-list-bullet icon-green", (SELECT r.fUuid from t_resource r where r.fUrl = '#' AND r.fResourceName = "客户管理"), (SELECT tr.fSequence + 1 from t_resource tr where tr.fUrl != '#' AND tr.fResourceName = "客户列表"), 0, 1, 0, null , NOW());
#赋予行业列表的权限
INSERT INTO t_role_resource(fRoleID, fResourceId) VALUES ((SELECT fUuid FROM t_role WHERE fRoleName = 'DepartmentManager'), (SELECT fUuid FROM t_resource WHERE fResourceName = '行业列表'));
INSERT INTO t_role_resource(fRoleID, fResourceId) VALUES ((SELECT fUuid FROM t_role WHERE fRoleName = 'Operation'), (SELECT fUuid FROM t_resource WHERE fResourceName = '行业列表'));
INSERT INTO t_role_resource(fRoleID, fResourceId) VALUES ((SELECT fUuid FROM t_role WHERE fRoleName = 'Technical'), (SELECT fUuid FROM t_resource WHERE fResourceName = '行业列表'));
INSERT INTO t_role_resource(fRoleID, fResourceId) VALUES ((SELECT fUuid FROM t_role WHERE fRoleName = 'SEOSales'), (SELECT fUuid FROM t_resource WHERE fResourceName = '行业列表'));

#查询权限
INSERT INTO t_resource VALUES (null, "查询", "/internal/industry/searchIndustries", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "行业列表"), 0, 0, 1, 1, null , NOW());

#添加权限
INSERT INTO t_resource VALUES (null, "添加", "/internal/industry/saveIndustry", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "行业列表"), 0, 0, 1, 1, null , NOW());

#删除所选权限
INSERT INTO t_resource VALUES (null, "删除所选", "/internal/industry/deleteIndustries", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "行业列表"), 0, 0, 1, 1, null , NOW());

#修改客户归属权限
INSERT INTO t_resource VALUES (null, "修改客户归属", "/internal/industry/updateIndustryUserID", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "行业列表"), 0, 0, 1, 1, null , NOW());

#删除权限
INSERT INTO t_resource VALUES (null, "删除", "/internal/industry/delIndustry", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "行业列表"), 0, 0, 1, 1, null , NOW());

#Excel上传权限
INSERT INTO t_resource VALUES (null, "Excel上传", "/internal/industry/uploadIndustryInfos", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "行业列表"), 0, 0, 1, 1, null , NOW());

#模板下载权限
INSERT INTO t_resource VALUES (null, "模板下载", "/SuperIndustrySimpleList.xls", null, null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "行业列表"), 0, 0, 1, 1, null , NOW());

#将所有行业列表子权限赋给运营人员, 技术人员和部门经理
insert into t_role_resource(fRoleID, fResourceId) select tem_role.fUuid,tem_resource.fUuid from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('DepartmentManager','Operation','Technical')) tem_role, (select t.fUuid from t_resource tr JOIN t_resource t on tr.fUUid = t.fParentID where tr.fResourceName = '行业列表') tem_resource);

#将行业列表子权限赋给SEOSales, 除了修改客户归属权限
insert into t_role_resource(fRoleID, fResourceId) select tem_role.fUuid, tem_resource.fUuid from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName = 'SEOSales') tem_role, (select t.fUuid from t_resource tr JOIN t_resource t on tr.fUUid = t.fParentID where tr.fResourceName = '行业列表' AND t.fResourceName != '修改客户归属') tem_resource);


########################################################################################################################################################
#添加网站联系信息列表 
INSERT INTO t_resource VALUES (null, "网站联系信息列表", "/internal/industryDetail/searchIndustryDetails", '无(用于上层菜单)', null, null, (SELECT r.fUuid from t_resource r where r.fResourceName = "行业列表"), 0, 0, 1, 1, null , NOW());
#赋予查看网站联系信息列表的权限
INSERT INTO t_role_resource(fRoleID, fResourceId) VALUES ((SELECT fUuid FROM t_role WHERE fRoleName = 'DepartmentManager'), (SELECT fUuid FROM t_resource WHERE fResourceName = '网站联系信息列表'));
INSERT INTO t_role_resource(fRoleID, fResourceId) VALUES ((SELECT fUuid FROM t_role WHERE fRoleName = 'Operation'), (SELECT fUuid FROM t_resource WHERE fResourceName = '网站联系信息列表'));
INSERT INTO t_role_resource(fRoleID, fResourceId) VALUES ((SELECT fUuid FROM t_role WHERE fRoleName = 'Technical'), (SELECT fUuid FROM t_resource WHERE fResourceName = '网站联系信息列表'));
INSERT INTO t_role_resource(fRoleID, fResourceId) VALUES ((SELECT fUuid FROM t_role WHERE fRoleName = 'SEOSales'), (SELECT fUuid FROM t_resource WHERE fResourceName = '网站联系信息列表'));

#查询权限
INSERT INTO t_resource VALUES (null, "查询", "/internal/industryDetail/searchIndustryDetails", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "网站联系信息列表"), 0, 0, 1, 1, null , NOW());

#添加权限
INSERT INTO t_resource VALUES (null, "添加", "/internal/industryDetail/saveIndustryDetail", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "网站联系信息列表"), 0, 0, 1, 1, null , NOW());

#删除权限
INSERT INTO t_resource VALUES (null, "删除", "/internal/industryDetail/delIndustryDetail", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "网站联系信息列表"), 0, 0, 1, 1, null , NOW());

#删除所选权限
INSERT INTO t_resource VALUES (null, "删除所选", "/internal/industryDetail/deleteIndustryDetails", 'ajax', null, null, (SELECT r.fUuid from t_resource r where r.fUrl != '#' AND r.fResourceName = "网站联系信息列表"), 0, 0, 1, 1, null , NOW());

#将所有网站联系信息列表子权限赋给运营人员, 技术人员和部门经理, SEOSales
insert into t_role_resource(fRoleID, fResourceId) select tem_role.fUuid,tem_resource.fUuid from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('DepartmentManager','Operation','Technical', 'SEOSales')) tem_role, (select t.fUuid from t_resource tr JOIN t_resource t on tr.fUUid = t.fParentID where tr.fResourceName = '网站联系信息列表') tem_resource);


