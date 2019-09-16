#客户业务关联表
DROP TABLE IF EXISTS `t_customer_business`;
CREATE TABLE `t_customer_business`
(
    `fUuid`                 int(11)        NOT NULL AUTO_INCREMENT COMMENT '客户业务主键',
    `fCustomerUuid`         int(11)        NOT NULL COMMENT '客户ID',
    `fType`                  varchar(50)   NULL COMMENT '业务类型： keyword-关键字、qzsetting-全站、fm-负面',
    `fCreateTime`           timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `fUpdateTime`           datetime       NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`fUuid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '客户业务关联表';
