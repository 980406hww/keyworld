package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClientUpgradeDao;
import com.keymanager.monitoring.entity.ClientUpgrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ClientUpgradeService extends ServiceImpl<ClientUpgradeDao, ClientUpgrade>{

    @Autowired
    private ClientUpgradeDao clientUpgradeDao;

    @Autowired
    private ClientStatusService clientStatusService;

    public Page<ClientUpgrade> searchClientUpgrades(Page<ClientUpgrade> page) {
        page.setRecords(clientUpgradeDao.searchClientUpgrades(page));
        return page;
    }

    public void saveClientUpgrade(ClientUpgrade clientUpgrade) throws Exception {
        if(clientUpgrade.getUuid() == null) {
            clientUpgrade.setCreateTime(new Date());
            clientUpgradeDao.insert(clientUpgrade);
        } else {
            clientUpgradeDao.updateById(clientUpgrade);
        }
    }

    public void clientAutoUpgrade() throws Exception {
        // 获取升级任务
        ClientUpgrade clientUpgrade = new ClientUpgrade();
        clientUpgrade.setStatus(true);
        List<ClientUpgrade> clientUpgradeList = clientUpgradeDao.selectList(new EntityWrapper<ClientUpgrade>(clientUpgrade));
        for (ClientUpgrade cu : clientUpgradeList) {
            // 获取正在升级的机器数量
            Integer clientCount = clientStatusService.getUpgradingClientCount(clientUpgrade);
            if(clientCount == null) {
                clientStatusService.updateClientTargetVersion(cu);
            } else {
                clientCount = cu.getMaxUpgradeCount() - clientCount;
                if(clientCount > 0) {
                    cu.setMaxUpgradeCount(clientCount);
                    clientStatusService.updateClientTargetVersion(cu);
                }
            }
        }
    }
}