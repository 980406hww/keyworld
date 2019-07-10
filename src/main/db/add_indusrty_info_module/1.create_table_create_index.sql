
DROP TABLE IF EXISTS `t_industry_info`;
CREATE TABLE `t_industry_info` (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fIndustryName` varchar(100) DEFAULT NULL COMMENT '行业名称',
  `fUserID` varchar(50) DEFAULT NULL COMMENT '所有者',
  `fSearchEngine` varchar(50) DEFAULT NULL COMMENT '搜索引擎',
  `fTerminalType` varchar(50) DEFAULT NULL COMMENT '终端类型',
  `fTargetUrl` varchar(200) DEFAULT NULL COMMENT '目标起始网址',
  `fPageNum` tinyint(4) DEFAULT NULL COMMENT '页数',
  `fPagePerNum` tinyint(4) DEFAULT NULL COMMENT '每页条数',
  `fStatus` tinyint(4) DEFAULT NULL COMMENT '状态 0: 未爬取，1: 爬取中 2: 爬取完',
  `fCreateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fUpdateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`fUuid`),
  KEY `fUserID` (`fUserID`(15)),
  KEY `fIndustryName` (`fIndustryName`(20),`fSearchEngine`(4),`fStatus`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='行业表';

DROP TABLE IF EXISTS `t_industry_detail`;
CREATE TABLE `t_industry_detail` (
  `fUuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fIndustryID` int(11) NOT NULL COMMENT '行业ID',
  `fWebsite` varchar(100) DEFAULT NULL COMMENT '网站域名',
  `fQQ` varchar(255) DEFAULT NULL COMMENT 'QQ',
  `fTelephone` varchar(255) DEFAULT NULL COMMENT '电话',
  `fWeight` tinyint(4) DEFAULT NULL COMMENT '权重',
  `fRemark` varchar(255) DEFAULT NULL COMMENT '备注',
  `fLevel` tinyint(4) DEFAULT NULL COMMENT '层级',
  `fCreateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fUpdateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`fUuid`),
  KEY `fIndustryID` (`fIndustryID`),
  KEY `fWebsite` (`fWebsite`(20),`fWeight`,`fRemark`(20))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='行业详情表';