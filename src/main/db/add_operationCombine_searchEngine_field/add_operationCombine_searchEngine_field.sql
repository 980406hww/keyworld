#操作组合新增搜索引擎和默认操作组合字段
ALTER TABLE `t_operation_combine` ADD COLUMN `fSearchEngine` VARCHAR(50) DEFAULT '百度' COMMENT '搜索引擎' AFTER `fTerminalType`;
ALTER TABLE `t_operation_combine` ADD COLUMN `fEngineDefault` int(11) DEFAULT 0 COMMENT '标识默认操作组' AFTER `fSearchEngine`;