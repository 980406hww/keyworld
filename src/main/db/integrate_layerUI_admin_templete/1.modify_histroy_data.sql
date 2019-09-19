
# 修改不是整站销售的人的站点状态为其他状态
UPDATE t_qz_setting SET fRenewalStatus = 4 WHERE fCustomerUuid IN (
SELECT DISTINCT c.fUuid
FROM t_customer c
LEFT JOIN t_userinfo u 
ON c.fUserID = u.fLoginName
LEFT JOIN t_organization o 
ON o.fUuid = u.fOrganizationID
WHERE u.fstatus = 0 
AND o.fOrganizationName != '整站销售部');

# 修改销售部门的人的现有暂停续费站点状态为下架状态
UPDATE t_qz_setting SET fRenewalStatus = 3 WHERE fRenewalStatus = 0;

# 删除部门管理员角色关联的资源记录
DELETE FROM  WHERE fRoleID = (SELECT fUuid FROM t_role WHERE fRoleName = 'DepartmentManager');