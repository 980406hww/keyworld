package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.SupplierServiceTypeMapping;
import java.util.List;

public interface SupplierServiceTypeMappingService {

    List<SupplierServiceTypeMapping> searchSupplierServiceTypeMappings(Long supplierId);

    void deleteSupplierBySupplierCode(Long uuid);
}
