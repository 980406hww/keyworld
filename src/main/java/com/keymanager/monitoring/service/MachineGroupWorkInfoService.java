package com.keymanager.monitoring.service;


import com.keymanager.monitoring.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.monitoring.dao.MachineGroupWorkInfoDao;
import com.keymanager.monitoring.entity.MachineGroupWorkInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机器分组工作信息统计
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
        //将record list的数据按照组名 group 放入  record map
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
        List<MachineGroupWorkInfo> machineGroupWorkInfos = machineGroupWorkInfoDao.getMachineGroupWorkInfos(machineGroupWorkInfoCriteria);
        return machineGroupWorkInfos;
    }

    public List<MachineGroupWorkInfo> getHistoryMachineGroupWorkInfo (MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria) {
        List<MachineGroupWorkInfo> historyMachineGroupWorkInfos = machineGroupWorkInfoDao.getHistoryMachineGroupWorkInfo(machineGroupWorkInfoCriteria);
        this.setMachineGroupWorkInfos(historyMachineGroupWorkInfos);
        return historyMachineGroupWorkInfos;
    }


}
