﻿
# 站点信息表
CREATE TABLE `sys_qz_setting` (
  `QS_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `CUSTOMER_ID` int(11) NOT NULL COMMENT '客户ID',
  `DOMAIN` varchar(200) NOT NULL COMMENT '域名',
  `SEARCH_ENGINE` varchar(50) DEFAULT NULL COMMENT '搜索引擎',
  `BEAR_PAW_ID` varchar(100) DEFAULT NULL COMMENT '熊掌号',
  `RENEWAL_STATUS` tinyint(1) DEFAULT '0' COMMENT '续费状态 0.未续费 1.续费 2.首次收费 3.下架 4.其他',
  `PC_GROUP` varchar(50) DEFAULT NULL COMMENT 'PC 优化组',
  `PHONE_GROUP` varchar(50) DEFAULT NULL COMMENT 'Phone 优化组',
  `COMPANY_CODE` varchar(100) DEFAULT NULL COMMENT '公司编码',
  PRIMARY KEY (`QS_ID`) USING BTREE,
  KEY `CUSTOMER_ID` (`CUSTOMER_ID`) USING BTREE,
  KEY `DOMAIN` (`DOMAIN`) USING BTREE,
  KEY `SEARCH_ENGINE` (`SEARCH_ENGINE`) USING BTREE,
  KEY `COMPANY_CODE` (`COMPANY_CODE`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

# 站点曲线表
CREATE TABLE `sys_qz_keyword_rank` (
  `QK_ID` int(11) NOT NULL AUTO_INCREMENT,
  `QS_ID` int(11) DEFAULT NULL COMMENT '全站的ID',
  `TERMINAL_TYPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '终端类型（PC, Phone）',
  `WEBSITE_TYPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'aiZhan' COMMENT '网站种类 (爱站，5118)',
  `PROCESSING_TYPE` tinyint(1) DEFAULT '1' COMMENT '处理类型 0 不处理 1 处理',
  `TOP_TEN` text COLLATE utf8mb4_unicode_ci COMMENT '前10曲线值',
  `TOP_TWENTY` text COLLATE utf8mb4_unicode_ci COMMENT '前20曲线值',
  `TOP_THIRTY` text COLLATE utf8mb4_unicode_ci COMMENT '前30曲线值',
  `TOP_FORTY` text COLLATE utf8mb4_unicode_ci COMMENT '前40曲线值',
  `TOP_FIFTY` text COLLATE utf8mb4_unicode_ci COMMENT '前50曲线值',
  `TOP_HUNDRED` text COLLATE utf8mb4_unicode_ci COMMENT '前100曲线值',
  `DATE` text COLLATE utf8mb4_unicode_ci COMMENT '月日(横坐标) 爱站',
  `INCREASE` double DEFAULT '0' COMMENT '涨幅',
  `TODAY_DIFF_VAL` int(11) DEFAULT '0' COMMENT '今日top10差值',
  `ONE_WEEK_DIFF_VAL` int(11) DEFAULT '0' COMMENT '一周top10差值',
  `RECORD_DATE` text COLLATE utf8mb4_unicode_ci COMMENT '年月日(横坐标) 收录',
  `RECORD` text COLLATE utf8mb4_unicode_ci COMMENT '收录曲线',
  `INIT_TOP_TEN_NUM` int(11) DEFAULT NULL COMMENT '初始前10',
  `INIT_TOP_FIFTY_NUM` int(11) DEFAULT NULL COMMENT '初始前50',
  PRIMARY KEY (`QK_ID`) USING BTREE,
  KEY `QS_ID` (`QS_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT;

# 关键词表
CREATE TABLE `sys_customer_keyword` (
  `QS_ID` int(11) DEFAULT NULL COMMENT '全站ID',
  `URL` varchar(255) DEFAULT NULL COMMENT 'URL',
  `TERMINAL_TYPE` varchar(20) DEFAULT 'PC' COMMENT '终端类型',
  `CUSTOMER_ID` int(11) NOT NULL COMMENT '客户id',
  `KEYWORD` varchar(200) DEFAULT NULL COMMENT '关键词',
  `STATUS` tinyint(4) DEFAULT NULL COMMENT '关键词操作状态 0-暂不操作，1-激活，2-新增  3-下架',
  `INITIAL_POSITION` int(11) DEFAULT NULL COMMENT '原排名',
  `CURRENT_POSITION` int(11) DEFAULT NULL COMMENT '现排名',
  `OPTIMIZE_PLAN_COUNT` int(11) DEFAULT '0' COMMENT '预计刷量',
  `OPTIMIZED_COUNT` int(11) DEFAULT '0' COMMENT '已刷',
  `CREATE_TIME` date DEFAULT NULL COMMENT '同步日期',
  KEY `QS_ID` (`QS_ID`,`CUSTOMER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;