package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.criteria.VpnInfoCriteria;
import com.keymanager.monitoring.dao.SupplierDao;
import com.keymanager.monitoring.dao.VpnInfoDao;
import com.keymanager.monitoring.entity.Supplier;
import com.keymanager.monitoring.entity.SupplierServiceType;
import com.keymanager.monitoring.entity.SupplierServiceTypeMapping;
import com.keymanager.monitoring.entity.VpnInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
