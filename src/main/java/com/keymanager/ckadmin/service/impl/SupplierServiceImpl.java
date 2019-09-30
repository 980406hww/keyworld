package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.SupplierCriteria;
import com.keymanager.ckadmin.dao.SupplierDao;
import com.keymanager.ckadmin.entity.Supplier;
import com.keymanager.ckadmin.entity.SupplierServiceType;
import com.keymanager.ckadmin.entity.SupplierServiceTypeMapping;
import com.keymanager.ckadmin.service.SupplierService;
import com.keymanager.ckadmin.service.SupplierServiceTypeMappingService;
import com.keymanager.ckadmin.service.SupplierServiceTypeService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("supplierService2")
public class SupplierServiceImpl extends ServiceImpl<SupplierDao, Supplier> implements SupplierService {

    @Resource(name = "supplierDao2")
    private SupplierDao supplierDao;

    @Resource(name = "supplierServiceTypeService2")
    private SupplierServiceTypeService supplierTypeService;

    @Resource(name = "supplierServiceTypeMappingService2")
    private SupplierServiceTypeMappingService supplierServiceTypeMappingService;

    @Override
    public Page<Supplier> searchSuppliers(SupplierCriteria criteria) {
        Page<Supplier> page = new Page<>(criteria.getPage(), criteria.getLimit());
        page.setOrderByField("createTime");
        page.setAsc(false);
        Wrapper<Supplier> wrapper = new EntityWrapper<>();
        wrapper.like("fContactperson", criteria.getContactPerson());
        wrapper.like("fQq", criteria.getQq());
        wrapper.like("fPhone", criteria.getPhone());
        page = selectPage(page, wrapper);
        for (Supplier supplier : page.getRecords()) {
            supplementServiceType(supplier);
        }
        return page;
    }

    @Override
    public void deleteByUuid(Long uuid) {
        supplierServiceTypeMappingService.deleteSupplierBySupplierCode(uuid);
        supplierDao.deleteById(uuid);
    }

    private void supplementServiceType(Supplier supplier) {
        List<SupplierServiceTypeMapping> supplierServiceTypeMappings = supplierServiceTypeMappingService.searchSupplierServiceTypeMappings(supplier.getUuid());
        for (SupplierServiceTypeMapping supplierServiceTypeMapping : supplierServiceTypeMappings) {
            SupplierServiceType supplierServiceTypes = supplierTypeService.getSupplierServiceType(supplierServiceTypeMapping.getSupplierServiceTypeUuid());
            supplierServiceTypeMapping.setSupplierServiceType(supplierServiceTypes);
        }
        supplier.setSupplierServiceTypeMappings(supplierServiceTypeMappings);
    }
}
