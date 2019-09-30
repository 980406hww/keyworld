package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.dao.SupplierServiceTypeDao;
import com.keymanager.ckadmin.entity.SupplierServiceType;
import com.keymanager.ckadmin.service.SupplierServiceTypeService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("supplierServiceTypeService2")
public class SupplierServiceServiceTypeServiceImpl implements SupplierServiceTypeService {

    @Resource(name = "supplierServiceTypeDao2")
    private SupplierServiceTypeDao supplierServiceTypeDao;

    @Override
    public List<SupplierServiceType> searchSupplierServiceType() {
        return supplierServiceTypeDao.searchSupplierTypes();
    }

    @Override
    public SupplierServiceType getSupplierServiceType(Integer uuid) {
        return supplierServiceTypeDao.getSupplierType(uuid);
    }
}
