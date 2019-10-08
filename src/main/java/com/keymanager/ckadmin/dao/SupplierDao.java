package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.Supplier;
import org.springframework.stereotype.Repository;

@Repository("supplierDao2")
public interface SupplierDao extends BaseMapper<Supplier> {

}
