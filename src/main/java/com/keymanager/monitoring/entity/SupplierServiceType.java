package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@TableName(value = "t_supplier_serviceType")
public class SupplierServiceType{

    @TableId(value = "fUuid", type= IdType.AUTO)
    private Integer uuid;

    @TableField(value = "fName")
    private String name;

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
