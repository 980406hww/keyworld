package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.SupplierCriteria;
import com.keymanager.ckadmin.entity.Supplier;

public interface SupplierService extends IService<Supplier> {

    Page<Supplier> searchSuppliers(SupplierCriteria criteria);

    void deleteByUuid(Long uuid);
}
