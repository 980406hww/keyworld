package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.dao.MachineGroupWorkInfoDao;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import com.keymanager.ckadmin.service.MachineGroupWorkInfoService;
import com.keymanager.ckadmin.service.MachineInfoService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("machineGroupWorkInfoService2")
public class MachineGroupWorkInfoServiceImpl extends ServiceImpl<MachineGroupWorkInfoDao, MachineGroupWorkInfo> implements MachineGroupWorkInfoService {

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
        List<MachineGroupWorkInfo> machineGroupWorkInfos = getMachineGroupWorkInfos(criteria);
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

    private List<MachineGroupWorkInfo> getMachineGroupWorkInfos(MachineGroupWorkInfoCriteria criteria) {
        return machineGroupWorkInfoDao.getMachineGroupWorkInfos(criteria);
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

    public void updateCustomerKeywordStatInfo (){
        /*List<Long> uuids = machineGroupWorkInfoDao.findMostDistantMachineGroupWorkInfo();
        if (CollectionUtils.isNotEmpty(uuids)) {
            machineGroupWorkInfoDao.deleteBatchIds(uuids);
        }*/
        List<MachineGroupWorkInfo> machineGroupWorkInfos = generateAllMachineGroupWorkInfo(new MachineGroupWorkInfoCriteria());
        for (MachineGroupWorkInfo machineGroupWorkInfo : machineGroupWorkInfos) {
            machineGroupWorkInfo.setCreateDate(new Date());
            machineGroupWorkInfoDao.insert(machineGroupWorkInfo);
        }
    }

    private List<MachineGroupWorkInfo> generateAllMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria) {
        List<MachineGroupWorkInfo> machineGroupWorkInfos = getMachineGroupWorkInfos(criteria);
        Map<String, Map<String, Map<String, MachineGroupWorkInfo>>> refreshStatInfoRecordGroupMap = new HashMap<>();
        for (MachineGroupWorkInfo MachineGroupWorkInfo : machineGroupWorkInfos) {
            Map<String, Map<String, MachineGroupWorkInfo>> refreshStatInfoRecordTerminalTypeMap = refreshStatInfoRecordGroupMap.get(MachineGroupWorkInfo.getMachineGroup());
            if (null == refreshStatInfoRecordTerminalTypeMap) {
                refreshStatInfoRecordTerminalTypeMap = new HashMap<>();
            }
            Map<String, MachineGroupWorkInfo> refreshStatInfoRecordTypeMap = refreshStatInfoRecordTerminalTypeMap.get(MachineGroupWorkInfo.getTerminalType());
            if (null == refreshStatInfoRecordTypeMap) {
                refreshStatInfoRecordTypeMap = new HashMap<>();
            }
            refreshStatInfoRecordTypeMap.put(MachineGroupWorkInfo.getType(), MachineGroupWorkInfo);
            refreshStatInfoRecordTerminalTypeMap.put(MachineGroupWorkInfo.getTerminalType(), refreshStatInfoRecordTypeMap);
            refreshStatInfoRecordGroupMap.put(MachineGroupWorkInfo.getMachineGroup(), refreshStatInfoRecordTerminalTypeMap);
        }

        List<MachineGroupWorkInfo> searchClientStatusForMachineGroupWorkInfos = machineInfoService.searchMachineInfoFormMachineGroupWorkInfo(criteria);
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
