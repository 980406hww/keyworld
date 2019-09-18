package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.MachineInfoDao;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import com.keymanager.ckadmin.entity.MachineInfo;
import com.keymanager.ckadmin.service.MachineInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("machineInfoService2")
public class MachineInfoServiceImpl extends ServiceImpl<MachineInfoDao, MachineInfo>
        implements MachineInfoService {

    @Resource(name = "machineInfoDao2")
    private MachineInfoDao machineInfoDao;


    @Override
    public Integer getUpgradingMachineCount(ClientUpgrade clientUpgrade) {
        return machineInfoDao.getUpgradingMachineCount(clientUpgrade);
    }

    @Override
    public Integer getResidualMachineCount(ClientUpgrade clientUpgrade) {
        return machineInfoDao.getResidualMachineCount(clientUpgrade);
    }

    @Override
    public void updateMachineTargetVersion(ClientUpgrade clientUpgrade) {
        machineInfoDao.updateMachineTargetVersion(clientUpgrade);
    }
}
