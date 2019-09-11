

#创建cpu内核和内存字段大小

ALTER TABLE `t_machine_info`ADD COLUMN `fCpuCount`  tinyint(4) NULL DEFAULT 0 AFTER `fCreateTime`;
ALTER TABLE `t_machine_info`ADD COLUMN `fMemory`  int(11) NULL DEFAULT 0 AFTER `fCreateTime`;