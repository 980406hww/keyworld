package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.MachineInfoCriteria;
import com.keymanager.monitoring.entity.MachineInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MachineInfoDao extends BaseMapper<MachineInfo> {

    List<MachineInfo> searchMachineInfosOrByHost(@Param("terminalType") String terminalType, @Param("comfirm") String comfirm);

    MachineInfo getMachineInfoByMachineID(@Param("clientID") String clientID, @Param("terminalType") String terminalType);

    void resetRestartStatusForProcessing();

    List<MachineInfo> searchMachineInfos(Page<MachineInfo> page, @Param("machineInfoCriteria") MachineInfoCriteria machineInfoCriteria);

    List<MachineInfo> searchBadMachineInfo(Page<MachineInfo> page, @Param("machineInfoCriteria") MachineInfoCriteria machineInfoCriteria);

    void updateMachineInfoTargetVersion(@Param("clientIDs") List<String> clientIDs, @Param("targetVersion") String targetVersion);

    void updateMachineInfoTargetVPSPassword(@Param("clientIDs") List<String> clientIDs, @Param("targetVPSPassword") String targetVPSPassword);

    void deleteMachineInfos(@Param("clientIDs") List<String> clientIDs);

    void reopenMachineInfo(@Param("clientIDs") List<String> clientIDs, @Param("downloadProgramType") String downloadProgramType, @Param("switchGroupName") String switchGroupName);

    void updateStartUpStatusForCompleted(@Param("clientIDs") List<String> clientIDs);

    Integer selectMaxIdByMachineID(@Param("clientID") String clientID);

    void batchUpdateMachineInfo(@Param("clientIDs") String[] clientIDs, @Param("mi") MachineInfo mi, @Param("machineInfo") MachineInfo machineInfo);

    void batchChangeStatus(@Param("clientIds") String[] clientIds, @Param("valid") Boolean valid);

    void batchChangeTerminalType(@Param("clientIds") String[] clientIds, @Param("terminalType") String terminalType);

}
