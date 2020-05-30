
# 需要同步的客户信息
INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`) VALUES ('SyncCustomerPtKeyword', 'SyncCustomerName', 'liebiaobaidu,liebiaosougou,liebiao360');

# 同步开关
INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`) VALUES ('SyncCustomerPtKeywordSwitch', CURRENT_DATE(), '1');

