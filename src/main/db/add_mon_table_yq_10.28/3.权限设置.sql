

#添加一级菜单“统计”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)VALUES ('统计', '#', 'fi-thumbnails', 90, 0, 1, 0, NOW(), '2.0');

#将统计下的权限赋给技术人员
insert into t_role_resource_new(fRoleID, fResourceId)
select tem_role.fUuid, tem_resource.fUuid
from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName = 'Technical') tem_role,
      (select tr.fUuid
       from t_resource_new tr
       where tr.fResourceName = '统计' AND tr.fVersion = '2.0') tem_resource);
		 
# 添加二级菜单“统计中心”
INSERT INTO `db_keyword`.`t_resource_new`(`fResourceName`, `fUrl`, `fIconCls`,`fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`,  `fCreateTime`, `fVersion`)
VALUES ('统计中心', '/internal/layer/home', 'fi-thumbnails',(SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "统计" AND fVersion = '2.0'), 1, 0, 1, 0, NOW(), '2.0');

# 统计中心权限赋予技术人员
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName = 'Technical') tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '统计中心' AND fVersion = '2.0') tem_resource);

