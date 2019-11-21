
DROP TABLE IF EXISTS `t_qz_charge_mon`;
CREATE TABLE `t_qz_charge_mon`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT,
  `fQzSettingUuid` int(11) NOT NULL COMMENT '全站UUid',
  `fQzDomain` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作对象域名',
  `fQzCustomer` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作对象客户',
  `fSearchEngine` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全站对应搜索引擎',
  `fTerminalType` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全站对应终端类型',
  `fOperationUser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `fOperationType` int(2) NOT NULL COMMENT '操作类型：2首次收费,0暂停,1续费,3下架,4删除',
  `fIsDel` int(2) NOT NULL DEFAULT 1 COMMENT '全站是否删除',
  `fOperationDate` date NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`fUuid`) USING BTREE,
  INDEX `fOperationDate_fSearchEngine_index`(`fOperationDate`, `fSearchEngine`) USING BTREE,
  INDEX `fOperationDate_index`(`fOperationDate`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收费状态记录表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
