package com.keymanager.ckadmin.dao;

import com.keymanager.ckadmin.entity.SupplierServiceType;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("supplierServiceTypeDao2")
public interface SupplierServiceTypeDao {

    SupplierServiceType getSupplierType(@Param("supplierTypeUuid") Integer uuid);

    List<SupplierServiceType> searchSupplierTypes();
}
