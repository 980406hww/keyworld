package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.ClientUpgrade;

import java.util.Date;
import java.util.List;

public interface ClientUpgradeService extends IService<ClientUpgrade> {

    Page<ClientUpgrade> searchClientUpgrades(Page<ClientUpgrade> page, String terminalType);

    void saveClientUpgrade(String terminalType, ClientUpgrade clientUpgrade);

    void clientAutoUpgrade();

    void updateClientUpgradeStatus(Long uuid, Boolean status);

    void batchDeleteClientUpgrade(List<Integer> uuids);
}
