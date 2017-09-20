package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/7.
 */
@TableName(value = "t_supplier_servicetype_mapping")
public class SupplierNexus extends BaseEntity {

    @TableField(value = "fSupplierCode")
    private Long supplierCode;

    @TableField(value = "fSupplierServiceTypeCode")
    private Integer supplierServiceTypeCode;

    @TableField(exist = false)
    private SupplierServiceType supplierServiceType;

    public SupplierServiceType getSupplierServiceType() {
        return supplierServiceType;
    }

    public void setSupplierServiceType(SupplierServiceType supplierServiceType) {
        this.supplierServiceType = supplierServiceType;
    }

    public Long getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(Long supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Integer getSupplierServiceTypeCode() {
        return supplierServiceTypeCode;
    }

    public void setSupplierServiceTypeCode(Integer supplierServiceTypeCode) {
        this.supplierServiceTypeCode = supplierServiceTypeCode;
    }
}
