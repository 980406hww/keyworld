package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import com.keymanager.ckadmin.entity.MachineInfo;

public interface MachineInfoService extends IService<MachineInfo> {
    public Integer getUpgradingMachineCount(ClientUpgrade clientUpgrade);

    public Integer getResidualMachineCount(ClientUpgrade clientUpgrade);

    public void updateMachineTargetVersion(ClientUpgrade clientUpgrade);
}
