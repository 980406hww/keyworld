package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.ClientUpgradeDao;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import com.keymanager.ckadmin.service.ClientUpgradeService;
import com.keymanager.ckadmin.service.MachineInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("clientUpgradeService2")
public class ClientUpgradeServiceImpl extends ServiceImpl<ClientUpgradeDao, ClientUpgrade>
        implements ClientUpgradeService {

    @Resource(name = "clientUpgradeDao2")
    private ClientUpgradeDao clientUpgradeDao;

    @Resource(name = "machineInfoService2")
    private MachineInfoService machineInfoService;

    @Override
    public Page<ClientUpgrade> searchClientUpgrades(Page<ClientUpgrade> page, String terminalType) {
        page.setRecords(clientUpgradeDao.searchClientUpgrades(page, terminalType));
        return page;
    }

    @Override
    public void saveClientUpgrade(String terminalType, ClientUpgrade clientUpgrade) {
        if(clientUpgrade.getUuid() == null) {
            clientUpgrade.setTerminalType(terminalType);
            clientUpgrade.setResidualUpgradeCount(1);
            clientUpgrade.setCreateTime(new Date());
            clientUpgradeDao.insert(clientUpgrade);
        } else {
            clientUpgradeDao.updateById(clientUpgrade);
        }
    }

    @Override
    public void clientAutoUpgrade() {
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

    @Override
    public void updateClientUpgradeStatus(Long uuid, Boolean status) {
        clientUpgradeDao.updateClientUpgradeStatus(uuid, status);
    }

    @Override
    public void batchDeleteClientUpgrade(List<Integer> uuids) {
        for(Integer uuid: uuids){
            deleteById(uuid.longValue());
        }
    }
}
