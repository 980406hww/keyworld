alter table `db_keyword`.`t_customer_keyword`
   add column `fMachineGroup` varchar(50) DEFAULT 'Default' NULL after `fOptimizeDate`;

alter table `db_keyword`.`t_machine_info`
   add column `fMachineGroup` varchar(50) DEFAULT 'Default' NULL after `fGroup`;

CREATE INDEX `CacheKeyword` ON t_customer_keyword(`fMachineGroup`,`fTerminalType`,`fInvalidFlag`,`fStatus`,`fQueryTime`);