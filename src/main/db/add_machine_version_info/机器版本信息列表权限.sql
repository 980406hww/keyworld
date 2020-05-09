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
