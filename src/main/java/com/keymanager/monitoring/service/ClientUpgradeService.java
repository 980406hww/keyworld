package com.keymanager.monitoring.service;

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
    private MachineInfoService machineInfoService;

    public Page<ClientUpgrade> searchClientUpgrades(Page<ClientUpgrade> page, String terminalType) {
        page.setRecords(clientUpgradeDao.searchClientUpgrades(page, terminalType));
        return page;
    }

    public void saveClientUpgrade(String terminalType, ClientUpgrade clientUpgrade) throws Exception {
        if(clientUpgrade.getUuid() == null) {
            clientUpgrade.setTerminalType(terminalType);
            clientUpgrade.setResidualUpgradeCount(0);
            clientUpgrade.setCreateTime(new Date());
            clientUpgradeDao.insert(clientUpgrade);
        } else {
            clientUpgradeDao.updateById(clientUpgrade);
        }
    }

    public void clientAutoUpgrade() throws Exception {
        List<ClientUpgrade> clientUpgradeList = clientUpgradeDao.findClientUpgradeJobs();
        for (ClientUpgrade clientUpgrade : clientUpgradeList) {
            // 获取正在升级的机器数量
            Integer upgradingCount = machineInfoService.getUpgradingMachineCount(clientUpgrade);
            // 获取剩余升级机器数量
            Integer residualCount = machineInfoService.getResidualMachineCount(clientUpgrade);

            upgradingCount = clientUpgrade.getMaxUpgradeCount() - upgradingCount;
            if(upgradingCount > 0 && residualCount > 0) {
                clientUpgrade.setResidualUpgradeCount(residualCount - upgradingCount);
                clientUpgradeDao.updateById(clientUpgrade);
                // 修改目标版本号
                clientUpgrade.setMaxUpgradeCount(upgradingCount);
                machineInfoService.updateMachineTargetVersion(clientUpgrade);
            } else {
                clientUpgrade.setResidualUpgradeCount(residualCount);
                clientUpgradeDao.updateById(clientUpgrade);
            }
        }
    }

    public void updateClientUpgradeStatus(Long uuid, Boolean status) {
        clientUpgradeDao.updateClientUpgradeStatus(uuid, status);
    }
}