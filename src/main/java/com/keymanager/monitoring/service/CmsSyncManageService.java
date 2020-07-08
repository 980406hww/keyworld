package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CmsSyncManageDao;
import com.keymanager.monitoring.entity.CmsSyncManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("cmsSyncManageService2")
public class CmsSyncManageService extends ServiceImpl<CmsSyncManageDao, CmsSyncManage> {

    @Autowired
    private CmsSyncManageDao syncManageDao;

    public HashMap<Long, HashMap<String, CmsSyncManage>> searchSyncManageMap(String customerNameStr, String type) {
        String[] customerNames = customerNameStr.replaceAll(" ", "").split(",");
        HashMap<Long, HashMap<String, CmsSyncManage>> syncMap = new HashMap<>();
        for (String customerName : customerNames) {
            CmsSyncManage ptSyncManage = syncManageDao.selectCmsSyncManage(customerName, type);
            if (null != ptSyncManage) {
                long mapKey = ptSyncManage.getUserId();
                HashMap<String, CmsSyncManage> valueMap = syncMap.get(mapKey);
                if (valueMap == null) {
                    valueMap = new HashMap<>();
                    syncMap.put(mapKey, valueMap);
                }

                String mpaKeyValue = ptSyncManage.getCompanyCode();
                CmsSyncManage value = valueMap.get(mpaKeyValue);
                if (null == value) {
                    valueMap.put(mpaKeyValue, ptSyncManage);
                }
            }
        }
        return syncMap;
    }
}
