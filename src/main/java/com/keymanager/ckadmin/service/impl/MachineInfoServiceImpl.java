package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.dao.MachineInfoDao;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import com.keymanager.ckadmin.service.MachineInfoService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("machineInfoService2")
public class MachineInfoServiceImpl implements MachineInfoService {

    @Resource(name = "machineInfoDao2")
    private MachineInfoDao machineInfoDao;

    @Override
    public List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria) {
        return machineInfoDao.searchMachineInfoFormMachineGroupWorkInfo(criteria);
    }
}
