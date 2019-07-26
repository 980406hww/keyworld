
use db_keyword;
#资源数据

INSERT INTO `db_keyword`.`t_resource`(`fUuid`, `fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) VALUES (5379, '修改所选机器机器分组', '/internal/machineInfo/batchUpdateMachineGroupSelected', 'ajax', NULL, '', 5275, 0, 0, 1, 1, NULL, '2019-07-25 09:52:13');
INSERT INTO `db_keyword`.`t_resource`(`fUuid`, `fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) VALUES (5380, '修改查询条件下机器机器分组', '/internal/machineInfo/updateMachineGroupByCriteria', 'ajax', NULL, '', 5275, 0, 0, 1, 1, NULL, '2019-07-25 09:53:54');
INSERT INTO `db_keyword`.`t_resource`(`fUuid`, `fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) VALUES (5381, '更新机器机器分组', '/internal/machineInfo/updateMachineGroup', 'ajax', NULL, '', 5275, 0, 0, 1, 1, NULL, '2019-07-25 09:54:37');
INSERT INTO `db_keyword`.`t_resource`(`fUuid`, `fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) VALUES (5382, '修改所选机器优化组', '/internal/machineInfo/batchUpdateGroupSelected', 'ajax', NULL, '', 5275, 0, 0, 1, 1, NULL, '2019-07-25 09:55:11');
INSERT INTO `db_keyword`.`t_resource`(`fUuid`, `fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) VALUES (5383, '修改查询条件下机器优化组', '/internal/machineInfo/updateGroupByCriteria', 'ajax', NULL, '', 5275, 0, 0, 1, 1, NULL, '2019-07-25 09:56:20');
INSERT INTO `db_keyword`.`t_resource`(`fUuid`, `fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) VALUES (5384, '修改所选关键字机器分组', '/internal/customerKeyword/updateMachineGroupByCustomerUuids', 'ajax', NULL, '', 5034, 0, 0, 1, 1, NULL, '2019-07-25 10:06:45');
INSERT INTO `db_keyword`.`t_resource`(`fUuid`, `fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) VALUES (5385, '修改查询条件下关键字机器分组', '/internal/customerKeyword/updateMachineGroupByCriteria', 'ajax', NULL, '', 5034, 0, 0, 1, 1, NULL, '2019-07-25 10:07:17');
INSERT INTO `db_keyword`.`t_resource`(`fUuid`, `fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) VALUES (5386, '修改关键字机器分组', '/internal/customerKeyword/updateCustomerKeywordMachineGroup', 'ajax', NULL, '', 5034, 0, 0, 1, 1, NULL, '2019-07-25 10:28:27');
INSERT INTO `db_keyword`.`t_resource`(`fUuid`, `fResourceName`, `fUrl`, `fOpenMode`, `fDescription`, `fIconCls`, `fParentID`, `fSequence`, `fStatus`, `fOpened`, `fResourceType`, `fUpdateTime`, `fCreateTime`) VALUES (5387, '机器分组工作队列', '/internal/customerKeyword/showMachineGroupAndSize', NULL, NULL, 'fi-thumbnails', 1000, 0, 0, 1, 0, NULL, '2019-07-25 18:08:48');


#角色资源表

INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22168, 18, 5387, NULL, '2019-07-25 18:12:00');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22429, 15, 5387, NULL, '2019-07-25 18:12:08');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22173, 18, 5386, NULL, '2019-07-25 18:12:00');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22434, 15, 5386, NULL, '2019-07-25 18:12:08');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22172, 18, 5385, NULL, '2019-07-25 18:12:00');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22433, 15, 5385, NULL, '2019-07-25 18:12:08');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22432, 15, 5384, NULL, '2019-07-25 18:12:08');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22171, 18, 5384, NULL, '2019-07-25 18:12:00');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22279, 18, 5383, NULL, '2019-07-25 18:12:00');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22525, 15, 5383, NULL, '2019-07-25 18:12:08');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22278, 18, 5382, NULL, '2019-07-25 18:12:00');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22524, 15, 5382, NULL, '2019-07-25 18:12:08');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22277, 18, 5381, NULL, '2019-07-25 18:12:00');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22523, 15, 5381, NULL, '2019-07-25 18:12:08');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22276, 18, 5380, NULL, '2019-07-25 18:12:00');
INSERT INTO `db_keyword`.`t_role_resource`(`fUuid`, `fRoleID`, `fResourceID`, `fUpdateTime`, `fCreateTime`) VALUES (22522, 15, 5380, NULL, '2019-07-25 18:12:08');


