

# 升级清除Config缓存菜单为2.0
UPDATE `t_resource_new`
SET `fVersion` = '2.0',fParentID = (SELECT * from (SELECT fUuid FROM t_resource_new WHERE fVersion = '2.0' and fResourceName='其他') as a)
WHERE
	fResourceName = '清除Config缓存';

# 升级系统图标菜单为2.0
UPDATE `t_resource_new`
SET `fVersion` = '2.0',fParentID = (SELECT * from (SELECT fUuid FROM t_resource_new WHERE fVersion = '2.0' and fResourceName='日志监控') as a)
WHERE
	fResourceName = '系统图标';

# 删除临时表
DROP TABLE
IF EXISTS `dl`;

#创建临时表用于存储需要删掉的菜单
CREATE TEMPORARY TABLE dl AS (
	SELECT
		fUuid,
		fResourceName
	FROM
		t_resource_new
	WHERE
		fResourceType = 0
	AND fVersion = 1.0
	AND fResourceName NOT IN (
		'角色管理',
		'用户管理',
		'部门管理',
		'资源管理',
		'权限管理',
		'资源管理',
		'客户管理',
		'终端管理',
		'供应商管理',
		'应用程序管理',
		'其他',
		'日志监控'
	)
);

# 删掉临时表的按钮子资源
DELETE
FROM
	t_resource_new
WHERE
	fResourceType = 1
AND fVersion = '1.0'
AND fParentID IN (SELECT fUuid FROM dl);

# 删掉临时表的二级菜单资源
DELETE
FROM
	t_resource_new
WHERE
	fResourceType = 0
AND fVersion = '1.0'
AND fUuid IN (SELECT fUuid FROM dl);

# 删掉一些版本为1.0 的一级菜单

DELETE
FROM
	t_resource_new
WHERE
	fResourceType = 0
AND fVersion = '1.0'
AND fResourceName IN (
		'客户管理',
		'终端管理',
		'供应商管理',
		'应用程序管理',
		'其他',
		'日志监控'
);

# 删除临时表
DROP TABLE
IF EXISTS `dl`;

