# 需要重新建立dblink的表
# dblink
# 例：CONNECTION='mysql://usrname:password@192.168.1.98:3306/databasename/table'
# cms_keyword, sys_customer_keyword, sys_qz_setting, sys_qz_keyword_rank
DROP TABLE IF EXISTS `cms_keyword`;
CREATE TABLE `cms_keyword` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关键词id',
  `USER_ID` bigint(20) NOT NULL COMMENT '用户id',
  `CUSTOMER_KEYWORD_ID` bigint(20) DEFAULT NULL COMMENT '客户关键词id',
  `KEYWORD` varchar(100) NOT NULL COMMENT '关键词',
  `URL` varchar(100) NOT NULL COMMENT '链接',
  `TITLE` varchar(255) DEFAULT NULL COMMENT '标题',
  `SEARCH_ENGINE` varchar(20) NOT NULL COMMENT '搜索引擎',
  `TERMINAL_TYPE` varchar(10) NOT NULL COMMENT '终端类型',
  `KEYWORD_GROUP` varchar(100) DEFAULT NULL COMMENT '关键词分组 (用于搜索)',
  `PRICE_PER_DAY` double(10,2) NOT NULL COMMENT '价格（元/天）',
  `CURRENT_POSITION` int(11) NOT NULL COMMENT '当前排名',
  `STATUS` tinyint(4) NOT NULL COMMENT '关键字状态 0-暂不操作，1-激活，2-新增  3-下架',
  `OPERA_STATUS` tinyint(4) DEFAULT '1' COMMENT '操作状态 1：可操作，0：不可操作',
  `CAPTURE_POSITION_TIME` datetime DEFAULT NULL COMMENT '爬取排名时间',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '提交时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEYWORD` (`USER_ID`,`KEYWORD`,`URL`,`SEARCH_ENGINE`,`TERMINAL_TYPE`) USING BTREE,
  KEY `CUSTOMER_KEYWORD_ID` (`USER_ID`,`CUSTOMER_KEYWORD_ID`) USING BTREE,
  KEY `SEARCH_ENGINE` (`USER_ID`,`SEARCH_ENGINE`,`TERMINAL_TYPE`,`KEYWORD_GROUP`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=97630 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `sys_customer_keyword`;
CREATE TABLE `sys_customer_keyword` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `KEYWORD_ID` bigint(20) DEFAULT NULL COMMENT '关键词id (用于关联更新)',
  `QS_ID` bigint(20) DEFAULT NULL COMMENT '全站id(用于查询)',
  `KEYWORD` varchar(200) DEFAULT NULL COMMENT '关键词',
  `URL` varchar(255) DEFAULT NULL COMMENT 'URL',
  `TERMINAL_TYPE` varchar(20) DEFAULT 'PC' COMMENT '终端类型',
  `KEYWORD_GROUP` varchar(100) DEFAULT NULL COMMENT '关键词分组 (用于搜索)',
  `STATUS` tinyint(4) DEFAULT NULL COMMENT '关键词操作状态 0-暂不操作，1-激活，2-新增  3-下架',
  `OPERA_STATUS` tinyint(4) DEFAULT NULL COMMENT '关键词操作状态 0-暂不操作，1-激活，2-新增  3-下架',
  `CAPTURE_POSITION_TIME` datetime DEFAULT NULL COMMENT '爬取排名时间',
  `INITIAL_POSITION` int(11) DEFAULT NULL COMMENT '原排名',
  `CURRENT_POSITION` int(11) DEFAULT NULL COMMENT '现排名',
  `OPTIMIZE_PLAN_COUNT` int(11) DEFAULT '0' COMMENT '预计刷量',
  `OPTIMIZED_COUNT` int(11) DEFAULT '0' COMMENT '已刷',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '提交时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  KEY `QS_ID` (`QS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=84486 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `sys_qz_setting`;
CREATE TABLE `sys_qz_setting` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `USER_ID` bigint(20) NOT NULL COMMENT '用户id',
  `QS_ID` bigint(20) DEFAULT NULL COMMENT '全站id',
  `DOMAIN` varchar(200) NOT NULL COMMENT '域名',
  `SEARCH_ENGINE` varchar(50) DEFAULT NULL COMMENT '搜索引擎',
  `PC_GROUP` varchar(50) DEFAULT NULL COMMENT 'PC 优化组',
  `PHONE_GROUP` varchar(50) DEFAULT NULL COMMENT 'Phone 优化组',
  PRIMARY KEY (`ID`),
  KEY `QS_ID` (`QS_ID`),
  KEY `DOMAIN` (`USER_ID`,`DOMAIN`) USING BTREE,
  KEY `SEARCH_ENGINE` (`USER_ID`,`SEARCH_ENGINE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `sys_qz_keyword_rank`;
CREATE TABLE `sys_qz_keyword_rank` (
  `QK_ID` int(11) NOT NULL,
  `QS_ID` int(11) NOT NULL COMMENT '全站的ID',
  `TERMINAL_TYPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '终端类型（PC, Phone）',
  `WEBSITE_TYPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'aiZhan' COMMENT '网站种类 (爱站，5118)',
  `TOP_TEN` text COLLATE utf8mb4_unicode_ci COMMENT '前10曲线值',
  `TOP_TWENTY` text COLLATE utf8mb4_unicode_ci COMMENT '前20曲线值',
  `TOP_THIRTY` text COLLATE utf8mb4_unicode_ci COMMENT '前30曲线值',
  `TOP_FORTY` text COLLATE utf8mb4_unicode_ci COMMENT '前40曲线值',
  `TOP_FIFTY` text COLLATE utf8mb4_unicode_ci COMMENT '前50曲线值',
  `DATE` text COLLATE utf8mb4_unicode_ci COMMENT '月日(横坐标) 爱站',
  KEY `QS_ID` (`QS_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# 新建 cms_sync_manage 表的创建
# dblink
# 例：CONNECTION='mysql://usrname:password@192.168.1.98:3306/databasename/table'
DROP TABLE IF EXISTS `cms_sync_manage`;
CREATE TABLE `cms_sync_manage` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` int(11) NOT NULL COMMENT '用户ID',
  `COMPANY_CODE` varchar(100) NOT NULL COMMENT '编码',
  `TYPE` varchar(20) NOT NULL COMMENT '类型 pt,qz',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USER_ID` (`USER_ID`,`COMPANY_CODE`,`TYPE`) USING BTREE,
  KEY `COMPANY_CODE` (`COMPANY_CODE`,`TYPE`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='同步管理表';

# 重新建表
DROP TABLE IF EXISTS `t_pt_customer_keyword`;
CREATE TABLE `t_pt_customer_keyword` (
  `fUuid` int(11) NOT NULL COMMENT '主键id',
  `fTitle` varchar(255) DEFAULT NULL COMMENT '标题',
  `fCurrentPosition` int(11) DEFAULT NULL COMMENT '当前排名',
  `fPrice` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `fCapturePositionQueryTime` datetime DEFAULT NULL COMMENT '同步排名时间',
  `fMark` tinyint(4) DEFAULT '0' COMMENT '同步标识',
  `fOperaStatus` tinyint(4) DEFAULT '1' COMMENT '操作状态 1：可操作，0：不可操作',
  PRIMARY KEY (`fUuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='中间表 用于存储同步前的数据';

# 新建表
DROP TABLE IF EXISTS `t_qz_customer_keyword`;
CREATE TABLE `t_qz_customer_keyword` (
  `fUuid` int(11) NOT NULL COMMENT '主键id',
  `fCurrentPosition` int(11) DEFAULT NULL COMMENT '当前排名',
  `fCapturePositionQueryTime` datetime DEFAULT NULL COMMENT '同步排名时间',
  `fOptimizePlanCount` int(11) DEFAULT '0' COMMENT '预计刷量',
  `fOptimizedCount` int(11) DEFAULT '0' COMMENT '已刷',
  `fMark` tinyint(4) DEFAULT '0' COMMENT '同步标识',
  `fOperaStatus` tinyint(4) DEFAULT '1' COMMENT '操作状态 1：可操作，0：不可操作',
  PRIMARY KEY (`fUuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='中间表 用于存储同步前的数据';
