package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.SupplierCriteria;
import com.keymanager.ckadmin.entity.Supplier;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("supplierDao2")
public interface SupplierDao extends BaseMapper<Supplier> {

    List<Supplier> searchSuppliers(Page<Supplier> page, @Param("criteria") SupplierCriteria criteria);
}
