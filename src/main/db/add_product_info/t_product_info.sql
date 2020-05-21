
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


DROP TABLE IF EXISTS `t_product_info`;
CREATE TABLE `t_product_info`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `fProductName` varchar(50)  NULL DEFAULT NULL COMMENT '产品名',
  `fProductPrice` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品价格',
  `fCreateDate` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `fAlterDate` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`fUuid`) USING BTREE
) ENGINE = InnoDB ;

SET FOREIGN_KEY_CHECKS = 1;


#添加产品资源
INSERT INTO t_resourec_new ( fResourceName, fUrl, fVersion, fIconCls, fParentID, fSequence, fStatus, fOpened, fResourceType, fCreateTime)
VALUES
	(
		'产品操作',
		"/internal/productManage/modifyProductInfo",
		"2.0",
		"fi-compass",
		( SELECT r.fUuid FROM t_resource_new r WHERE r.fUrl = '/internal/productManage/toProductInfo' AND r.fResourceName = '产品管理' ),
		(
		SELECT
			IFNULL( MAX( tr.fSequence ) + 1, 1 )
		FROM
			t_resource_new r
		WHERE
			r.fUrl = '/internal/productManage/toProductInfo'
			AND r.fResourceName = '产品管理'
		),
		0,
		1,
		0,
	NOW( )
	);

#赋予权限
INSERT INTO t_role_resource_new(fRoleID, fResourceID) SELECT tem_role.fUuid, tem_resource.fUuid FROM (
      (SELECT r.fUuid FROM t_role r WHERE r.fRoleName in  ('Operation', 'Maintenance','AlgorithmGroup')) tem_role,
      (SELECT tr.fUuid FROM t_resource_new tr WHERE tr.fResourceName = '产品操作' AND fVersion = '2.0') tem_resource);