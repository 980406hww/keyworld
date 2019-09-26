package com.keymanager.ckadmin.dao;

import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("machineInfoDao2")
public interface MachineInfoDao {

    List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(@Param("criteria") MachineGroupWorkInfoCriteria criteria);
}
