package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.SupplierServiceTypeMapping;
import java.util.List;

public interface SupplierServiceTypeMappingService extends IService<SupplierServiceTypeMapping> {

    List<SupplierServiceTypeMapping> searchSupplierServiceTypeMappings(Long supplierId);

    void deleteSupplierBySupplierCode(Long uuid);
}
