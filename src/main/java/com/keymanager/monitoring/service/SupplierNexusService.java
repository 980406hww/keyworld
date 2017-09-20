package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.SupplierNexusDao;
import com.keymanager.monitoring.dao.SupplierServiceDao;
import com.keymanager.monitoring.entity.SupplierNexus;
import com.keymanager.monitoring.entity.SupplierServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@Service
public class SupplierNexusService extends ServiceImpl<SupplierNexusDao, SupplierNexus> {
    private static Logger logger = LoggerFactory.getLogger(SupplierNexusService.class);
    @Autowired
    private SupplierNexusDao supplierNexusDao;

    public List<SupplierNexus> findSupplierNexus(Long supplierId) {
        return supplierNexusDao.findSupplierNexus(supplierId);
    }

    public void deleteSupplierBySupplierCode(Long uuid) {
        supplierNexusDao.deleteSupplierBySupplierCode(uuid);
    }
}
