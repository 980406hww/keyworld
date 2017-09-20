package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.dao.SupplierDao;
import com.keymanager.monitoring.dao.SupplierNexusDao;
import com.keymanager.monitoring.dao.SupplierServiceDao;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.Supplier;
import com.keymanager.monitoring.entity.SupplierNexus;
import com.keymanager.monitoring.entity.SupplierServiceType;
import org.apache.commons.collections.CollectionUtils;
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
    private SupplierNexusService supplierNexusService;

    public Page<Supplier> searchSupplier(Page<Supplier> page, SupplierCriteria supplierCriteria) {
        List<Supplier> suppliers = supplierDao.searchSupplier(page,supplierCriteria);
        for(Supplier supplier : suppliers){
            List<SupplierNexus> supplierNexuses =supplierNexusService.findSupplierNexus(supplier.getUuid());
            for (SupplierNexus supplierNexuse : supplierNexuses) {
                SupplierServiceType supplierServiceTypes = supplierTypeService. getSupplierServiceType(supplierNexuse.getSupplierServiceTypeCode());
                supplierNexuse.setSupplierServiceType(supplierServiceTypes);
            }
            supplier.setSupplierNexus(supplierNexuses);
        }
        page.setRecords(suppliers);
        return page;
    }

    public void deleteSupplier(Long uuid) {
        supplierNexusService.deleteSupplierBySupplierCode(uuid);
        supplierDao.deleteById(uuid);
    }

    public void deleteAll(List<String> uuids) {
        for (String uuid : uuids) {
            deleteSupplier(Long.valueOf(uuid));
        }
    }

    public Supplier  getSupplier(Long uuid) {
        Supplier supplier = supplierDao.selectById(uuid);
        if (supplier != null ) {
             List<SupplierNexus> supplierNexus = supplierNexusService.findSupplierNexus(supplier.getUuid());
             supplier.setSupplierNexus(supplierNexus);
        }
        return supplier;
    }

    public void saveSupplier(Supplier supplier) {
        if (null != supplier.getUuid()) {
            updateSupplier(supplier);
        } else {
            supplierDao.insert(supplier);
            for (SupplierNexus supplierNexus : supplier.getSupplierNexus()) {
                supplierNexus.setSupplierCode(supplier.getUuid());
                supplierNexus.setSupplierServiceTypeCode(supplierNexus.getSupplierServiceTypeCode());
                supplierNexusService.insert(supplierNexus);
            }
        }
    }

    private void updateSupplier(Supplier supplier) {
        supplierNexusService.deleteSupplierBySupplierCode(supplier.getUuid());
        for (SupplierNexus supplierNexus : supplier.getSupplierNexus()) {
            supplierNexus.setSupplierCode(supplier.getUuid());
            supplierNexus.setSupplierServiceTypeCode(supplierNexus.getSupplierServiceTypeCode());
            supplierNexusService.insert(supplierNexus);
        }
        List<SupplierNexus> supplierNexuses =supplierNexusService.findSupplierNexus(supplier.getUuid());
        supplier.setSupplierNexus(supplierNexuses);
        supplier.setUpdateTime(new Date());
        supplierDao.updateById(supplier);
    }
}
