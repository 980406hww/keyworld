DROP TABLE IF EXISTS `t_pt_customer_keyword`;
CREATE TABLE `t_pt_customer_keyword` (
  `fUuid` int(11) NOT NULL,
  `fTitle` varchar(255) DEFAULT NULL,
  `fCurrentPosition` int(11) DEFAULT NULL,
  `fPrice` decimal(10,2) DEFAULT NULL,
  `fCaptureStatus` int(11) DEFAULT NULL,
  `fCapturePositionQueryTime` datetime DEFAULT NULL,
  `fCity` varchar(20) DEFAULT NULL,
  `fCapturePositionCity` varchar(100) DEFAULT NULL,
  `fMark` tinyint(4) DEFAULT '0' COMMENT '同步标识',
  `fOperaStatus` tinyint(4) DEFAULT '1' COMMENT '操作状态 1：可操作，0：不可操作',
  PRIMARY KEY (`fUuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='中间表 用于存储同步前的数据';

# 2020-6-30 cms_keyword fOperaStatus
ALTER TABLE `db_keyword`.`cms_keyword` ADD COLUMN `OPERA_STATUS` TINYINT(4) DEFAULT 1 COMMENT '操作状态 1：可操作，0：不可操作' AFTER `STATUS`;

# 配置最近同步操作状态时间
INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`) 
VALUES ('SyncPtOperaStatusTime', 'leibiaobaidu', '2020-06-30 18:40');

INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`) 
VALUES ('SyncPtOperaStatusTime', 'leibiaosougou', '2020-06-30 18:40');

INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`) 
VALUES ('SyncPtOperaStatusTime', 'leibiao360', '2020-06-30 18:40');

INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`) 
VALUES ('SyncPtOperaStatusTime', 'leibiaouc', '2020-06-30 18:40');

INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`) 
VALUES ('SyncPtOperaStatusTime', 'guorenbaidu', '2020-06-30 18:40');

# 先查询是否存在, 不存在执行insert语句
select * from `db_keyword`.`t_config` WHERE fConfigType = 'SyncPtKeywordTime' and fKey = 'leibiaouc';
#INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`) VALUES ('SyncPtKeywordTime', 'leibiaouc', '2020-06-29 18:40');