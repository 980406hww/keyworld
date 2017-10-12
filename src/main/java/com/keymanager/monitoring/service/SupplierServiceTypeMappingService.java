package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.SupplierServiceTypeMappingDao;
import com.keymanager.monitoring.entity.SupplierServiceTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@Service
public class SupplierServiceTypeMappingService extends ServiceImpl<SupplierServiceTypeMappingDao, SupplierServiceTypeMapping> {
    private static Logger logger = LoggerFactory.getLogger(SupplierServiceTypeMappingService.class);
    @Autowired
    private SupplierServiceTypeMappingDao supplierServiceTypeMappingDao;

    public List<SupplierServiceTypeMapping> searchSupplierServiceTypeMappings(Long supplierId) {
        return supplierServiceTypeMappingDao.searchSupplierServiceTypeMappings(supplierId);
    }

    public void deleteSupplierBySupplierCode(Long uuid) {
        supplierServiceTypeMappingDao.deleteSupplierServiceTypes(uuid);
    }
}
