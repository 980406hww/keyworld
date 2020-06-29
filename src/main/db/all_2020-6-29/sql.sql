
# 加索引
CREATE INDEX fQzDomain_index ON `db_keyword`.`t_qz_charge_mon`(fQzDomain);
CREATE INDEX fQzCustomer_index ON `db_keyword`.`t_qz_charge_mon`(fQzCustomer);

CREATE INDEX fMachineGroup_index ON `db_keyword`.`t_customer_keyword`(fMachineGroup, fType, fTerminalType);

