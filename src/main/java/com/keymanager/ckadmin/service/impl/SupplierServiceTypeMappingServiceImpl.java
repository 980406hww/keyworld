package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.dao.SupplierServiceTypeMappingDao;
import com.keymanager.ckadmin.entity.SupplierServiceTypeMapping;
import com.keymanager.ckadmin.service.SupplierServiceTypeMappingService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("supplierServiceTypeMappingService2")
public class SupplierServiceTypeMappingServiceImpl implements SupplierServiceTypeMappingService {

    @Resource(name = "supplierServiceTypeMappingDao2")
    private SupplierServiceTypeMappingDao supplierServiceTypeMappingDao;

    @Override
    public List<SupplierServiceTypeMapping> searchSupplierServiceTypeMappings(Long supplierId) {
        return supplierServiceTypeMappingDao.searchSupplierServiceTypeMappings(supplierId);
    }

    @Override
    public void deleteSupplierBySupplierCode(Long uuid) {
        supplierServiceTypeMappingDao.deleteSupplierServiceTypes(uuid);
    }
}
