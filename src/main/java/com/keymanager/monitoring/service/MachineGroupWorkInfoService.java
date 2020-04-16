package com.keymanager.monitoring.service;


import com.keymanager.monitoring.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.monitoring.dao.MachineGroupWorkInfoDao;
import com.keymanager.monitoring.entity.MachineGroupWorkInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机器分组信息统计
 */
@Service
public class MachineGroupWorkInfoService {
    @Autowired
    private MachineInfoService machineInfoService;
    @Autowired
    private MachineGroupWorkInfoDao machineGroupWorkInfoDao;

    public List<MachineGroupWorkInfo> generateMachineGroupWorkInfo(MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria){
        List<MachineGroupWorkInfo> machineGroupWorkInfos = getMachineGroupWorkInfos(machineGroupWorkInfoCriteria);
        Map<String, MachineGroupWorkInfo> MachineGroupWorkInfoVOMap = new HashMap<>();
        for (MachineGroupWorkInfo MachineGroupWorkInfo : machineGroupWorkInfos) {
            MachineGroupWorkInfoVOMap.put(MachineGroupWorkInfo.getMachineGroup(), MachineGroupWorkInfo);
        }
        List<MachineGroupWorkInfo> csMachineGroupWorkInfos = machineInfoService.searchMachineInfoFormMachineGroupWorkInfo(machineGroupWorkInfoCriteria);
        for (MachineGroupWorkInfo csMachineGroupWorkInfo : csMachineGroupWorkInfos) {
            MachineGroupWorkInfo machineGroupWorkInfo = MachineGroupWorkInfoVOMap.get(csMachineGroupWorkInfo.getMachineGroup());
            if (null != machineGroupWorkInfo) {
                machineGroupWorkInfo.setIdleTotalMinutes(csMachineGroupWorkInfo.getIdleTotalMinutes());
                machineGroupWorkInfo.setTotalMachineCount(csMachineGroupWorkInfo.getTotalMachineCount());
                machineGroupWorkInfo.setUnworkMachineCount(csMachineGroupWorkInfo.getUnworkMachineCount());
            }
        }
        this.setMachineGroupWorkInfos(machineGroupWorkInfos);
        return machineGroupWorkInfos;
    }
    public void setMachineGroupWorkInfos(List<MachineGroupWorkInfo> machineGroupWorkInfos){
        MachineGroupWorkInfo total = new MachineGroupWorkInfo();
        total.setMachineGroup("总计");
        for(MachineGroupWorkInfo machineGroupWorkInfo : machineGroupWorkInfos){
            total.setInvalidKeywordCount(total.getInvalidKeywordCount() + machineGroupWorkInfo.getInvalidKeywordCount());
            total.setNeedOptimizeCount(total.getNeedOptimizeCount() + machineGroupWorkInfo.getNeedOptimizeCount());
            total.setNeedOptimizeKeywordCount(total.getNeedOptimizeKeywordCount() + machineGroupWorkInfo.getNeedOptimizeKeywordCount());
            total.setQueryCount(total.getQueryCount() + machineGroupWorkInfo.getQueryCount());
            total.setTotalKeywordCount(total.getTotalKeywordCount() + machineGroupWorkInfo.getTotalKeywordCount());
            total.setTotalMachineCount(total.getTotalMachineCount() + machineGroupWorkInfo.getTotalMachineCount());
            total.setTotalOptimizeCount(total.getTotalOptimizeCount() + machineGroupWorkInfo.getTotalOptimizeCount());
            total.setTotalOptimizedCount(total.getTotalOptimizedCount() + machineGroupWorkInfo.getTotalOptimizedCount());
            total.setUnworkMachineCount(total.getUnworkMachineCount() + machineGroupWorkInfo.getUnworkMachineCount());
            total.setZeroOptimizedCount(total.getZeroOptimizedCount() + machineGroupWorkInfo.getZeroOptimizedCount());
            total.setReachStandardKeywordCount(total.getReachStandardKeywordCount() + machineGroupWorkInfo.getReachStandardKeywordCount());
            total.setTodaySubTotal(total.getTodaySubTotal() + machineGroupWorkInfo.getTodaySubTotal());
            total.setMaxInvalidCount(machineGroupWorkInfo.getMaxInvalidCount());
            total.setIdleTotalMinutes(total.getIdleTotalMinutes() + machineGroupWorkInfo.getIdleTotalMinutes());
        }
        machineGroupWorkInfos.add(0, total);
    }

    private List<MachineGroupWorkInfo> getMachineGroupWorkInfos(MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria) {
        return  machineGroupWorkInfoDao.getMachineGroupWorkInfos(machineGroupWorkInfoCriteria);
    }

    public List<MachineGroupWorkInfo> getHistoryMachineGroupWorkInfo (MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria) {
        List<MachineGroupWorkInfo> historyMachineGroupWorkInfos = machineGroupWorkInfoDao.getHistoryMachineGroupWorkInfo(machineGroupWorkInfoCriteria);
        this.setMachineGroupWorkInfos(historyMachineGroupWorkInfos);
        return historyMachineGroupWorkInfos;
    }

    public void updateCustomerKeywordStatInfo (){
        List<MachineGroupWorkInfo> machineGroupWorkInfos = generateAllMachineGroupWorkInfo(new MachineGroupWorkInfoCriteria());
        for (MachineGroupWorkInfo machineGroupWorkInfo : machineGroupWorkInfos) {
            machineGroupWorkInfo.setCreateDate(new Date());
            machineGroupWorkInfoDao.insert(machineGroupWorkInfo);
        }
    }

    private List<MachineGroupWorkInfo> generateAllMachineGroupWorkInfo(MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria) {
        List<MachineGroupWorkInfo> machineGroupWorkInfos = getMachineGroupWorkInfos(machineGroupWorkInfoCriteria);
        Map<String, Map<String, Map<String, MachineGroupWorkInfo>>> refreshStatInfoRecordGroupMap = new HashMap<String, Map<String, Map<String, MachineGroupWorkInfo>>>();
        for (MachineGroupWorkInfo MachineGroupWorkInfo : machineGroupWorkInfos) {
            Map<String, Map<String, MachineGroupWorkInfo>> refreshStatInfoRecordTerminalTypeMap = refreshStatInfoRecordGroupMap.get(MachineGroupWorkInfo.getMachineGroup());
            if (null == refreshStatInfoRecordTerminalTypeMap) {
                refreshStatInfoRecordTerminalTypeMap = new HashMap<String, Map<String, MachineGroupWorkInfo>>();
            }
            Map<String, MachineGroupWorkInfo> refreshStatInfoRecordTypeMap = refreshStatInfoRecordTerminalTypeMap.get(MachineGroupWorkInfo.getTerminalType());
            if (null == refreshStatInfoRecordTypeMap) {
                refreshStatInfoRecordTypeMap = new HashMap<String, MachineGroupWorkInfo>();
            }
            refreshStatInfoRecordTypeMap.put(MachineGroupWorkInfo.getType(), MachineGroupWorkInfo);
            refreshStatInfoRecordTerminalTypeMap.put(MachineGroupWorkInfo.getTerminalType(), refreshStatInfoRecordTypeMap);
            refreshStatInfoRecordGroupMap.put(MachineGroupWorkInfo.getMachineGroup(), refreshStatInfoRecordTerminalTypeMap);
        }

        List<MachineGroupWorkInfo> searchClientStatusForMachineGroupWorkInfos = machineInfoService.searchMachineInfoFormMachineGroupWorkInfo(machineGroupWorkInfoCriteria);
        for (MachineGroupWorkInfo csMachineGroupWorkInfo : searchClientStatusForMachineGroupWorkInfos) {
            Map<String, Map<String, MachineGroupWorkInfo>> csCustomerKeywordRefreshStatInfoRecordTerminalTypeMap = refreshStatInfoRecordGroupMap.get(csMachineGroupWorkInfo.getMachineGroup());
            if (null != csCustomerKeywordRefreshStatInfoRecordTerminalTypeMap) {
                Map<String, MachineGroupWorkInfo> csMachineGroupWorkInfoTypeMap = csCustomerKeywordRefreshStatInfoRecordTerminalTypeMap.get(csMachineGroupWorkInfo.getTerminalType());
                if (null != csMachineGroupWorkInfoTypeMap) {
                    for (MachineGroupWorkInfo MachineGroupWorkInfo : csMachineGroupWorkInfoTypeMap.values()) {
                        MachineGroupWorkInfo.setIdleTotalMinutes(csMachineGroupWorkInfo.getIdleTotalMinutes());
                        MachineGroupWorkInfo.setTotalMachineCount(csMachineGroupWorkInfo.getTotalMachineCount());
                        MachineGroupWorkInfo.setUnworkMachineCount(csMachineGroupWorkInfo.getUnworkMachineCount());
                    }
                }
            }
        }
        return machineGroupWorkInfos;
    }


}
