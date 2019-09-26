package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import java.util.List;

public interface MachineInfoService {

    List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria);
}
