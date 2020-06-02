CREATE TABLE `cms_keyword` (
   `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关键词id',
   `CUSTOMER_KEYWORD_ID` bigint(20) DEFAULT NULL COMMENT '客户关键词id',
   `KEYWORD` varchar(100) NOT NULL COMMENT '关键词',
   `URL` varchar(100) NOT NULL COMMENT '链接',
   `TITLE` varchar(255) DEFAULT NULL COMMENT '标题',
   `SEARCH_ENGINE` varchar(30) NOT NULL COMMENT '搜索引擎',
   `TERMINAL_TYPE` varchar(10) NOT NULL COMMENT '终端类型',
   `KEYWORD_GROUP` varchar(100) DEFAULT NULL COMMENT '关键词分组 (用于搜索)',
   `BEAR_PAW_NUM` varchar(100) DEFAULT NULL COMMENT '熊掌号',
   `PRICE_PER_DAY` double(10,2) NOT NULL COMMENT '价格（元/天）',
   `CURRENT_POSITION` int(11) NOT NULL COMMENT '当前排名',
   `STATUS` tinyint(4) NOT NULL COMMENT '关键字状态 0-暂不操作，1-激活，2-新增  3-下架',
   `CITY` varchar(100) DEFAULT NULL COMMENT '指定城市',
   `COMPANY_CODE` varchar(100) NOT NULL COMMENT '公司编码',
   `CAPTURE_POSITION_CITY` varchar(100) DEFAULT NULL COMMENT '爬取城市',
   `CAPTURE_POSITION_TIME` datetime DEFAULT NULL COMMENT '爬取排名时间',
   `CAPTURE_STATUS` tinyint(4) DEFAULT NULL COMMENT '爬取状态（0：未爬取  1：爬取中）',
   `CREATE_TIME` datetime DEFAULT NULL COMMENT '提交时间',
   `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
   PRIMARY KEY (`ID`) USING BTREE,
   UNIQUE KEY `KEYWORD` (`KEYWORD`,`URL`,`SEARCH_ENGINE`,`TERMINAL_TYPE`),
   KEY `CUSTOMER_KEYWORD_ID` (`CUSTOMER_KEYWORD_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `cms_keyword_position_history` (
  `KEYWORD_ID` int(11) NOT NULL COMMENT '关键词id',
  `SYSTEM_POSITION` int(11) NOT NULL COMMENT '系统记录的排名',
  `CUSTOMER_POSITION` int(11) DEFAULT NULL COMMENT '客户提供的排名',
  `SEARCH_ENGINE` varchar(255) NOT NULL COMMENT '搜索引擎',
  `TERMINAL_TYPE` varchar(255) NOT NULL COMMENT '终端',
  `TODAY_FEE` varchar(255) NOT NULL COMMENT '今日收费',
  `RECORD_DATE` date NOT NULL COMMENT '记录日期',
  UNIQUE KEY `KEYWORD_ID` (`KEYWORD_ID`,`RECORD_DATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
