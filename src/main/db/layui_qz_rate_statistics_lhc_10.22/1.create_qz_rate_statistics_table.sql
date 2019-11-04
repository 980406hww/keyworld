DROP TABLE IF EXISTS `t_qz_rate_statistics`;
CREATE TABLE `t_qz_rate_statistics`  (
     `fUuid` int(11) NOT NULL AUTO_INCREMENT,
     `fQZSettingUuid` int(11) NOT NULL COMMENT '全站id',
     `fCustomerUuid` int(11) NULL DEFAULT NULL COMMENT '客户id',
     `fTerminalType` varchar(30) NULL DEFAULT NULL COMMENT '终端类型',
     `fRate` int(8) NULL DEFAULT NULL COMMENT '涨幅',
     `fRateDate` varchar(25) NOT NULL COMMENT '记录日期',
     `fRateFullDate` varchar(50) NOT NULL COMMENT '带年份记录日期',
     `fCreateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
     `fUpdateTime` datetime NULL DEFAULT NULL,
     PRIMARY KEY (`fUuid`) USING BTREE,
     UNIQUE INDEX `uniqueRecord`(`fQZSettingUuid`, `fTerminalType`, `fRateFullDate`) USING BTREE COMMENT '根据全站id，终端，记录日期确保每天只有一条记录'
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '站点每日涨幅表';
