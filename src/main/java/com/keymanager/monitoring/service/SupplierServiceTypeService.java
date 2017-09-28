package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.SupplierServiceTypeDao;
import com.keymanager.monitoring.entity.SupplierServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@Service
public class SupplierServiceTypeService extends ServiceImpl<SupplierServiceTypeDao, SupplierServiceType> {
    private static Logger logger = LoggerFactory.getLogger(SupplierServiceTypeService.class);
    @Autowired
    private SupplierServiceTypeDao supplierServiceTypeDao;

    public List<SupplierServiceType> searchSupplierServiceType() {
        return supplierServiceTypeDao.searchSupplierServiceTypes();
    }

    public SupplierServiceType getSupplierServiceType(Integer supplierServiceTypeUuid) {
        return supplierServiceTypeDao.getSupplierServiceType(supplierServiceTypeUuid);
    }
}
