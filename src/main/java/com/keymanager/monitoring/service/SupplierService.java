package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.dao.SupplierDao;
import com.keymanager.monitoring.entity.Supplier;
import com.keymanager.monitoring.entity.SupplierServiceTypeMapping;
import com.keymanager.monitoring.entity.SupplierServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@Service
public class SupplierService extends ServiceImpl<SupplierDao , Supplier>{
    private static Logger logger = LoggerFactory.getLogger(SupplierService.class);
    @Autowired
    private SupplierDao supplierDao;

    @Autowired
    private SupplierServiceTypeService supplierTypeService;

    @Autowired
    private SupplierServiceTypeMappingService supplierServiceTypeMappingService;

    public Page<Supplier> searchSuppliers(Page<Supplier> page, SupplierCriteria supplierCriteria) {
        List<Supplier> suppliers = supplierDao.searchSuppliers(page,supplierCriteria);
        for(Supplier supplier : suppliers){
            supplementServiceType(supplier);
        }
        page.setRecords(suppliers);
        return page;
    }

    private void supplementServiceType(Supplier supplier) {
        List<SupplierServiceTypeMapping> supplierServiceTypeMappings = supplierServiceTypeMappingService.searchSupplierServiceTypeMappings(supplier.getUuid());
        for (SupplierServiceTypeMapping supplierServiceTypeMapping : supplierServiceTypeMappings) {
            SupplierServiceType supplierServiceTypes = supplierTypeService. getSupplierServiceType(supplierServiceTypeMapping.getSupplierServiceTypeUuid());
            supplierServiceTypeMapping.setSupplierServiceType(supplierServiceTypes);
        }
        supplier.setSupplierServiceTypeMappings(supplierServiceTypeMappings);
    }

    public void deleteSupplier(Long uuid) {
        supplierServiceTypeMappingService.deleteSupplierBySupplierCode(uuid);
        supplierDao.deleteById(uuid);
    }

    public void deleteSuppliers(List<String> uuids) {
        for (String uuid : uuids) {
            deleteSupplier(Long.valueOf(uuid));
        }
    }

    public Supplier  getSupplier(Long uuid) {
        Supplier supplier = supplierDao.selectById(uuid);
        if (supplier != null ) {
            supplementServiceType(supplier);
        }
        return supplier;
    }

    public void saveSupplier(Supplier supplier) {
        if (null != supplier.getUuid()) {
            updateSupplier(supplier);
        } else {
            supplierDao.insert(supplier);
            for (SupplierServiceTypeMapping supplierServiceTypeMapping : supplier.getSupplierServiceTypeMappings()) {
                supplierServiceTypeMapping.setSupplierUuid(supplier.getUuid());
                supplierServiceTypeMapping.setSupplierServiceTypeUuid(supplierServiceTypeMapping.getSupplierServiceTypeUuid());
                supplierServiceTypeMappingService.insert(supplierServiceTypeMapping);
            }
        }
    }

    private void updateSupplier(Supplier supplier) {
        supplierServiceTypeMappingService.deleteSupplierBySupplierCode(supplier.getUuid());
        for (SupplierServiceTypeMapping supplierServiceTypeMapping : supplier.getSupplierServiceTypeMappings()) {
            supplierServiceTypeMapping.setSupplierUuid(supplier.getUuid());
            supplierServiceTypeMappingService.insert(supplierServiceTypeMapping);
        }
        List<SupplierServiceTypeMapping> supplierServiceTypeMappings =  supplierServiceTypeMappingService.searchSupplierServiceTypeMappings(supplier.getUuid());
        supplier.setSupplierServiceTypeMappings(supplierServiceTypeMappings);
        supplier.setUpdateTime(new Date());
        supplierDao.updateById(supplier);
    }
}
