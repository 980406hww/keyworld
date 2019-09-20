
# 将原先分组信息移到销售备注，客户分组置为普通客户
update t_customer set fSaleRemark = ftype , ftype = '普通客户';


# 修改客户分组字段为客户类型字段、默认为'普通客户'
ALTER TABLE t_customer MODIFY COLUMN `ftype` varchar(20) DEFAULT '普通客户' NULL COMMENT '客户类型: 普通客户、测试客户';


#添加微信字段
ALTER TABLE `t_customer` ADD COLUMN `fWechat` varchar(100) NULL COMMENT '微信号' AFTER `fQQ` ;