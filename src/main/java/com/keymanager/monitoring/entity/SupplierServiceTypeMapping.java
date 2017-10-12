package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * Created by shunshikj22 on 2017/9/7.
 */
@TableName(value = "t_supplier_servicetype_mapping")
public class SupplierServiceTypeMapping{

    @TableId(value = "fUuid", type= IdType.AUTO)
    private Long uuid;

    @TableField(value = "fSupplierUuid")
    private Long supplierUuid;

    @TableField(value = "fSupplierServiceTypeUuid")
    private Integer supplierServiceTypeUuid;

    @TableField(exist = false)
    private SupplierServiceType supplierServiceType;

    public SupplierServiceType getSupplierServiceType() {
        return supplierServiceType;
    }

    public void setSupplierServiceType(SupplierServiceType supplierServiceType) {
        this.supplierServiceType = supplierServiceType;
    }

    public Long getSupplierUuid() {
        return supplierUuid;
    }

    public void setSupplierUuid(Long supplierUuid) {
        this.supplierUuid = supplierUuid;
    }

    public Integer getSupplierServiceTypeUuid() {
        return supplierServiceTypeUuid;
    }

    public void setSupplierServiceTypeUuid(Integer supplierServiceTypeUuid) {
        this.supplierServiceTypeUuid = supplierServiceTypeUuid;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }
}
