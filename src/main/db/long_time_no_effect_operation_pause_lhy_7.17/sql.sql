ALTER TABLE t_customer_keyword ADD COLUMN `fNoEffectConsecutiveDays` int(4) NOT NULL DEFAULT 0 COMMENT '没有效果的连续天数' AFTER fOptimizeStatus;