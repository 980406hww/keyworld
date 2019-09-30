package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.SupplierServiceType;
import java.util.List;

public interface SupplierServiceTypeService {

    List<SupplierServiceType> searchSupplierServiceType();

    SupplierServiceType getSupplierServiceType(Integer uuid);
}