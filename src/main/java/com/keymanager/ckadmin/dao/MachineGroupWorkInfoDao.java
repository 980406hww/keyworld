package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("machineGroupWorkInfoDao2")
public interface MachineGroupWorkInfoDao extends BaseMapper<MachineGroupWorkInfo> {

    List<MachineGroupWorkInfo> getHistoryMachineGroupWorkInfo(@Param("criteria") MachineGroupWorkInfoCriteria criteria);

    List<MachineGroupWorkInfo> getMachineGroupWorkInfos(@Param("criteria") MachineGroupWorkInfoCriteria criteria);

    List<Long> findMostDistantMachineGroupWorkInfo();
}
