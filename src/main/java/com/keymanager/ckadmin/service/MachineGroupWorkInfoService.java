package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import java.util.List;

public interface MachineGroupWorkInfoService {

    List<MachineGroupWorkInfo> getHistoryMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria);

    List<MachineGroupWorkInfo> generateMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria);
}
