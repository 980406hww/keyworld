
# 给关键字加上fCity字段
ALTER TABLE t_customer_keyword ADD COLUMN `fCity` VARCHAR(20) DEFAULT NULL COMMENT '目标城市' AFTER `fSearchEngine`;

