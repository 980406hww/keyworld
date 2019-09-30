package com.keymanager.ckadmin.dao;

import com.keymanager.ckadmin.entity.SupplierServiceTypeMapping;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("supplierServiceTypeMappingDao2")
public interface SupplierServiceTypeMappingDao {

    List<SupplierServiceTypeMapping> searchSupplierServiceTypeMappings(@Param("supplierUuid") Long supplierUuid);

    void deleteSupplierServiceTypes(@Param("supplierUuid") Long supplierUuid);
}
