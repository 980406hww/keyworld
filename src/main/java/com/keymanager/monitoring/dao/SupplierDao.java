package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.entity.Supplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
public interface SupplierDao extends BaseMapper<Supplier> {

    List<Supplier> searchSupplier(Page<Supplier> page, @Param("supplierCriteria") SupplierCriteria supplierCriteria);

}
