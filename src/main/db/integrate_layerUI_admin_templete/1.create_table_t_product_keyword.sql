#新关键字表
DROP TABLE IF EXISTS `t_product_keyword`;
CREATE TABLE `t_product_keyword`  (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '单词表ID',
  `fCustomerUuid` int(11) NOT NULL COMMENT '客户ID',
  `fKeyword` varchar(200) NOT NULL COMMENT '关键字',
	`fStatus` tinyint(4) NULL DEFAULT 0 COMMENT '关键字状态 0-暂不操作，1-激活，2-新增  3-下架',
  `fUrl` varchar(128) NULL COMMENT '链接',
  `fOriginalUrl` varchar(1000) NULL COMMENT '原始链接',
  `fTerminalType` varchar(10) NULL DEFAULT NULL COMMENT '终端类型',
  `fSearchEngine` varchar(100) NULL DEFAULT NULL COMMENT '搜索引擎',
  `fBearPawNumber` varchar(100) NULL DEFAULT NULL COMMENT '熊掌号',
	`fInitialPosition` int(11) NULL DEFAULT NULL  COMMENT '初始排名',
	`fCurrentPosition` int(11) NULL DEFAULT NULL  COMMENT '当前排名',
	`fIndexCount` int(11) NULL DEFAULT NULL  COMMENT '指数',
	`fPositionFirstFee` decimal(11, 2) NULL DEFAULT NULL  COMMENT '第一收费',
  `fPositionSecondFee` decimal(11, 2) NULL DEFAULT NULL  COMMENT '第二收费',
  `fPositionThirdFee` decimal(11, 2) NULL DEFAULT NULL  COMMENT '第三收费',
  `fPositionForthFee` decimal(11, 2) NULL DEFAULT NULL  COMMENT '第四收费',
  `fPositionFifthFee` decimal(11, 2) NULL DEFAULT NULL  COMMENT '第五收费',
  `fPositionFirstPageFee` decimal(11, 2) NULL DEFAULT NULL  COMMENT '首页收费',
	`fCollectMethod` varchar(20) NULL DEFAULT NULL COMMENT '收费方式: PerDay/PerMonth',
  `fCreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fUpdateTime` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`fUuid`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '新关键字表' ;
