package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.SupplierServiceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
public interface SupplierServiceTypeDao extends BaseMapper<SupplierServiceType> {
    SupplierServiceType getSupplierServiceType(@Param("supplierServiceTypeUuid") Integer supplierServiceTypeUuid);

    List<SupplierServiceType> searchSupplierServiceTypes();
}
