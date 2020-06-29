# 重置机器操作总数和操作成功数
update t_machine_info
set fOptimizationTotalCount = 0, fOptimizationSucceedCount = 0
WHERE fRunningProgramType = 'Super';

# Super 运行程序类型的机器记录清理操作总数，成功次数的时间
INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`) VALUES ('CleanTime', 'Super', CURRENT_DATE());

# 机器表加productID索引
CREATE INDEX fk_productID ON `db_keyword`.`t_machine_info`(`fProductID`);

# 产品统计 二级菜单
INSERT INTO `db_keyword`.`t_resource_new`
(`fResourceName`, `fUrl`, `fOpenMode`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fCreateTime`, `fVersion`)
VALUES ('产品统计', '/internal/productManage/toProductStatistics', null, 'fi-compass', (
    SELECT r.fUuid from t_resource_new r where r.fUrl = '#' AND r.fResourceName = '终端管理' AND fVersion = '2.0'), 8, 0, 1, 0, NOW(), '2.0');

#将权限赋给运维人员, 技术人员
insert into t_role_resource_new(fRoleID, fResourceId)
select tem_role.fUuid, tem_resource.fUuid
from ((SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Maintenance', 'Technical')) tem_role,
      (select t.fUuid
       from t_resource_new tr JOIN t_resource_new t on tr.fUuid = t.fParentID
       where tr.fResourceName = '终端管理' AND t.fResourceName = '产品统计' AND tr.fVersion = '2.0') tem_resource);
