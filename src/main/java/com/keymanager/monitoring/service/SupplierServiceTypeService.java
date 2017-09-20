package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.SupplierServiceDao;
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
public class SupplierServiceTypeService extends ServiceImpl<SupplierServiceDao , SupplierServiceType> {
    private static Logger logger = LoggerFactory.getLogger(SupplierServiceTypeService.class);
    @Autowired
    private SupplierServiceDao supplierServiceDao;

    public List<SupplierServiceType> searchSupplierServiceType() {
        return supplierServiceDao.searchSupplierServiceType();
    }

    public SupplierServiceType getSupplierServiceType(Integer supplierServiceTypeCode) {
        return supplierServiceDao.getSupplierServiceType(supplierServiceTypeCode);
    }
}
