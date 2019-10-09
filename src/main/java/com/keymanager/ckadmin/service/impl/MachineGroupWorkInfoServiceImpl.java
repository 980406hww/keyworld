package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.dao.MachineGroupWorkInfoDao;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import com.keymanager.ckadmin.service.MachineGroupWorkInfoService;
import com.keymanager.ckadmin.service.MachineInfoService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("machineGroupWorkInfoService2")
public class MachineGroupWorkInfoServiceImpl implements MachineGroupWorkInfoService {

    @Resource(name = "machineInfoService2")
    private MachineInfoService machineInfoService;
    @Resource(name = "machineGroupWorkInfoDao2")
    private MachineGroupWorkInfoDao machineGroupWorkInfoDao;

    @Override
    public List<MachineGroupWorkInfo> getHistoryMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria) {
        List<MachineGroupWorkInfo> historyMachineGroupWorkInfos = machineGroupWorkInfoDao.getHistoryMachineGroupWorkInfo(criteria);
        setMachineGroupWorkInfos(historyMachineGroupWorkInfos);
        return historyMachineGroupWorkInfos;
    }

    @Override
    public List<MachineGroupWorkInfo> generateMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria) {
        List<MachineGroupWorkInfo> machineGroupWorkInfos = machineGroupWorkInfoDao.getMachineGroupWorkInfos(criteria);
        Map<String, MachineGroupWorkInfo> machineGroupWorkInMap = new HashMap<>(machineGroupWorkInfos.size());
        for (MachineGroupWorkInfo machineGroupWorkInfo : machineGroupWorkInfos) {
            machineGroupWorkInMap.put(machineGroupWorkInfo.getMachineGroup(), machineGroupWorkInfo);
        }
        List<MachineGroupWorkInfo> csMachineGroupWorkInfos = machineInfoService.searchMachineInfoFormMachineGroupWorkInfo(criteria);
        for (MachineGroupWorkInfo csMachineGroupWorkInfo : csMachineGroupWorkInfos) {
            MachineGroupWorkInfo machineGroupWorkInfo = machineGroupWorkInMap.get(csMachineGroupWorkInfo.getMachineGroup());
            if (null != machineGroupWorkInfo) {
                machineGroupWorkInfo.setIdleTotalMinutes(csMachineGroupWorkInfo.getIdleTotalMinutes());
                machineGroupWorkInfo.setTotalMachineCount(csMachineGroupWorkInfo.getTotalMachineCount());
                machineGroupWorkInfo.setUnworkMachineCount(csMachineGroupWorkInfo.getUnworkMachineCount());
            }
        }
        setMachineGroupWorkInfos(machineGroupWorkInfos);
        return machineGroupWorkInfos;
    }

    private void setMachineGroupWorkInfos(List<MachineGroupWorkInfo> machineGroupWorkInfos) {
        MachineGroupWorkInfo total = new MachineGroupWorkInfo();
        total.setMachineGroup("总计");
        for (MachineGroupWorkInfo machineGroupWorkInfo : machineGroupWorkInfos) {
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
            machineGroupWorkInfo.setAvgOptimizedCount();
            machineGroupWorkInfo.setInvalidKeywordPercentage();
            machineGroupWorkInfo.setInvalidOptimizePercentage();
            machineGroupWorkInfo.setReachStandardPercentage();
            machineGroupWorkInfo.setIdlePercentage();
        }
        total.setAvgOptimizedCount();
        total.setInvalidKeywordPercentage();
        total.setInvalidOptimizePercentage();
        total.setReachStandardPercentage();
        total.setIdlePercentage();
        machineGroupWorkInfos.add(0, total);
    }
}
