package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.ClientUpgrade;

import java.util.Date;
import java.util.List;

public interface ClientUpgradeService extends IService<ClientUpgrade> {

    public Page<ClientUpgrade> searchClientUpgrades(Page<ClientUpgrade> page, String terminalType);

    public void saveClientUpgrade(String terminalType, ClientUpgrade clientUpgrade);

    public void clientAutoUpgrade();

    public void updateClientUpgradeStatus(Long uuid, Boolean status);

    public void batchDeleteClientUpgrade(List<Integer> uuids);
}
