
# 新建字段达标备注
ALTER TABLE t_qz_operation_type ADD COLUMN `fMonitorRemark` text(1000) DEFAULT NULL COMMENT '达标备注' AFTER `fSubDomainName`;	 