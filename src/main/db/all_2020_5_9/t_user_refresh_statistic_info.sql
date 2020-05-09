
#操作组合新增搜索引擎和默认操作组合字段
ALTER TABLE `t_operation_combine` ADD COLUMN `fSearchEngine` VARCHAR(50) DEFAULT '百度' COMMENT '搜索引擎' AFTER `fTerminalType`;
ALTER TABLE `t_operation_combine` ADD COLUMN `fEngineDefault` int(11) DEFAULT 0 COMMENT '标识默认操作组' AFTER `fSearchEngine`;

# 创建用户刷量历史记录表
DROP TABLE IF EXISTS `t_user_refresh_statistic_info`;
CREATE TABLE `t_user_refresh_statistic_info`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT,
  `fUserName` varchar(25) NULL DEFAULT NULL COMMENT '用户的登录名',
  `fType` varchar(50) NULL DEFAULT NULL COMMENT '链接类型',
  `fTerminalType` varchar(50)  NULL DEFAULT NULL COMMENT '终端类型',
  `fTotalKeywordCount` int(11) NULL DEFAULT NULL COMMENT '机器分组下关键词总数',
  `fNeedOptimizeKeywordCount` int(11) NULL DEFAULT NULL COMMENT '需要优化的关键词数',
  `fZeroOptimizedCount` int(11) NULL DEFAULT NULL COMMENT '零优化数',
  `fReachStandardKeywordCount` int(11) NULL DEFAULT NULL COMMENT '达标关键字数',
  `fTodaySubTotal` double NULL DEFAULT NULL COMMENT '今天小计',
  `fTotalOptimizeCount` int(11) NULL DEFAULT NULL COMMENT '总计优化数',
  `fTotalOptimizedCount` int(11) NULL DEFAULT NULL COMMENT '总优化数',
  `fNeedOptimizeCount` int(11) NULL DEFAULT NULL COMMENT '需要优化数',
  `fQueryCount` int(11) NULL DEFAULT NULL COMMENT '查询数',
  `fTotalMachineCount` int(11) NULL DEFAULT NULL COMMENT '总机器数',
  `fUnworkMachineCount` int(11) NULL DEFAULT NULL COMMENT '空闲机器数',
  `fMaxInvalidCount` int(11) NULL DEFAULT NULL COMMENT '最大无效数',
  `fIdleTotalMinutes` int(11) NULL DEFAULT NULL COMMENT '空闲总分钟数',
  `fCreateDate` date NULL DEFAULT NULL COMMENT '创建的日期',
  `fGroupName` varchar(25)  NULL DEFAULT NULL COMMENT '组名',
  `fCustomerName` varchar(25)  NULL DEFAULT NULL COMMENT '客户名',
  PRIMARY KEY (`fUuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8;
 
 

#添加用户刷量权限
INSERT INTO t_resourec_new ( fResourceName, fUrl, fVersion, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType, fCreateTime )
VALUES
	(
		"用户刷量统计",
		"/internal/userRefreshStatistic/toUserRefreshStatistic",
		"2.0",
		"fi-compass",
		( SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = "终端管理" ),
		(
		SELECT
			IFNULL( MAX( tr.fSequence ) + 1, 1 )
		FROM
			t_resource_new r
		WHERE
			r.fUrl = '#'
			AND r.fResourceName = "终端管理"
		),
		0,
		1,
		0,
	NOW( )
	);

#用户刷量统计授权
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName IN ('Technical', 'Operation', 'SEOSales', 'SEO')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '用户刷量统计' AND fVersion = '2.0') tem_resource);
#
#添加机器版本信息列表资源
INSERT INTO t_resourec_new ( fResourceName, fUrl, fVersion, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType, fCreateTime ,fOpenMode)
VALUES
	(
		'机器版本信息列表',
		"/internal/machineManage/machineVersionInfo",
		"2.0",
		"fi-compass",
		( SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '/internal/machineInfo/machineInfoStat' AND r.fResourceName = '终端统计' ),
		(
		SELECT
			IFNULL( MAX( tr.fSequence ) + 1, 1 )
		FROM
			t_resource_new r
		WHERE
			r.fUrl = '/internal/machineInfo/machineInfoStat'
			AND r.fResourceName = '终端统计'
		),
		0,
		1,
		0,
	NOW( ),
	'ajax'
	);

#赋予权限
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName in  ('AlgorithmGroup', 'Maintenance')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '机器版本信息列表' AND fVersion = '2.0') tem_resource);