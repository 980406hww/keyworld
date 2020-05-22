
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


DROP TABLE IF EXISTS `t_product_info`;
CREATE TABLE `t_product_info`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `fProductName` varchar(50)  NULL DEFAULT NULL COMMENT '产品名',
  `fProductPrice` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品价格',
  `fCreateTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `fUpdateTime` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`fUuid`) USING BTREE
) ENGINE = InnoDB ;

SET FOREIGN_KEY_CHECKS = 1;


# 产品管理
INSERT INTO t_resource_new ( fResourceName, fUrl, fVersion, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType, fCreateTime )
VALUES
	(
		'产品管理',
		'/internal/productManage/toProductInfo',
		'2.0',
		'fi-compass',
		( SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '#' AND r.fResourceName = '终端管理' AND fVersion = '2.0' ),
		(
		SELECT
			IFNULL( MAX( r.fSequence ) + 1, 1 )
		FROM
			t_resource_new r
		WHERE
			r.fUrl = '/internal/machineManage/toMachineInfos'
			AND r.fResourceName = '机器管理'
		),
		0,
		1,
		0,
		NOW( )
	);

#赋予权限
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName in  ('Operation', 'Maintenance','AlgorithmGroup')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '产品管理' AND fVersion = '2.0') tem_resource);