package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.monitoring.entity.MachineGroupWorkInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MachineGroupWorkInfoDao extends BaseMapper<MachineGroupWorkInfo> {

    List<MachineGroupWorkInfo> getMachineGroupWorkInfos(@Param("machineGroupWorkInfoCriteria") MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria);

    List<MachineGroupWorkInfo> getHistoryMachineGroupWorkInfo(@Param("machineGroupWorkInfoCriteria") MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria);
}
