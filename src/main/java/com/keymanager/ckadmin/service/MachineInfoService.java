package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import java.util.List;
import java.util.Map;

public interface MachineInfoService {

    List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria);

    List<Map<String, Object>> getMachineInfos();

    Map<String, Object> getMachineInfoBody(String cityName);
}
