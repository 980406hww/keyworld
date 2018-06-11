package com.keymanager.monitoring.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.ApplyInfoCriteria;
import com.keymanager.monitoring.dao.ApplyInfoDao;
import com.keymanager.monitoring.entity.ApplyInfo;
import com.keymanager.monitoring.entity.SupplierServiceTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@Service
public class ApplyInfoService extends ServiceImpl<ApplyInfoDao, ApplyInfo> {
    private static Logger logger = LoggerFactory.getLogger(ApplyInfoService.class);
    @Autowired
    private ApplyInfoDao applyInfoDao;

    public List<ApplyInfo> selectApplyInfoList() {
        return applyInfoDao.selectApplyInfoList();
    }

    public Page<ApplyInfo> searchApplyInfoList(Page<ApplyInfo> page, ApplyInfoCriteria applyInfoCriteria) {
        page.setRecords(applyInfoDao.searchApplyInfoList(page,applyInfoCriteria));
        return page;
    }

    public void deleteApplyInfo(Long uuid) {
        applyInfoDao.deleteById(uuid);
    }

    public void deleteApplyInfoList(List<String> uuids) {
        List<Long> uuidList = new ArrayList<Long>();
        for (String uuid : uuids){
            uuidList.add(Long.valueOf(uuid));
        }
        applyInfoDao.deleteBatchIds(uuidList);
    }

    public void saveApplyInfo(ApplyInfo applyInfo) {
        if (null != applyInfo.getUuid()) {
            updateById(applyInfo);
        } else {
            applyInfoDao.insert(applyInfo);
        }
    }

    public Object getApplyInfo(Long uuid) {
        ApplyInfo applyInfo = applyInfoDao.selectById(uuid);
        return applyInfo;
    }
}
