# 删除 cms_keyword_position_history
DROP TABLE IF EXISTS `cms_keyword_position_history`;

DROP TABLE IF EXISTS `t_pt_customer_keyword`;
CREATE TABLE `t_pt_customer_keyword` (
 `fUuid` int(11) NOT NULL,
 `fCurrentPosition` int(11) NOT NULL,
 `fPrice` decimal(10,2) NOT NULL,
 `fCaptureStatus` int(11) NOT NULL,
 `fCapturePositionQueryTime` datetime NOT NULL,
 `fCity` varchar(20) DEFAULT NULL,
 `fCapturePositionCity` varchar(100) DEFAULT NULL,
 PRIMARY KEY (`fUuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='中间表 用于存储同步前的数据';