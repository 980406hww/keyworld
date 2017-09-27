package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.SupplierServiceTypeMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
public interface SupplierServiceTypeMappingDao extends BaseMapper<SupplierServiceTypeMapping> {
    List<SupplierServiceTypeMapping> searchSupplierServiceTypeMappings(@Param("supplierUuid") Long supplierUuid);

    void deleteSupplierServiceTypes(@Param("supplierUuid") Long supplierUuid);

}
