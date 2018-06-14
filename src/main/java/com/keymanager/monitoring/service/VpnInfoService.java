package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.criteria.VpnInfoCriteria;
import com.keymanager.monitoring.dao.SupplierDao;
import com.keymanager.monitoring.dao.VpnInfoDao;
import com.keymanager.monitoring.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@Service
public class VpnInfoService extends ServiceImpl<VpnInfoDao, VpnInfo>{
    private static Logger logger = LoggerFactory.getLogger(VpnInfoService.class);
    @Autowired
    private VpnInfoDao vpnInfoDao;

    public List<VpnInfo> selectVpnImei(String imeiStr) {
        return vpnInfoDao.selectVpnImei(imeiStr);
    }

    public Page<VpnInfo> searchVpnInfoList(Page<VpnInfo> page, VpnInfoCriteria vpnInfoCriteria) {
        page.setRecords(vpnInfoDao.searchVpnInfoList(page,vpnInfoCriteria));
        return page;
    }

    public void deleteVpnInfo(Long uuid) {
        vpnInfoDao.deleteById(uuid);
    }

    public void deleteVpnInfoList(List<String> uuids) {
        List<Long> uuidList = new ArrayList<Long>();
        for (String uuid : uuids){
            uuidList.add(Long.valueOf(uuid));
        }
        vpnInfoDao.deleteBatchIds(uuidList);
    }

    public void saveApplyInfo(VpnInfo vpnInfo) {
        if (null != vpnInfo.getUuid()) {
            vpnInfoDao.updateById(vpnInfo);
        } else {
            vpnInfoDao.insert(vpnInfo);
        }
    }

    public Object getVpnInfo(Long uuid) {
        VpnInfo vpnInfo = vpnInfoDao.selectById(uuid);
        return vpnInfo;
    }
}
