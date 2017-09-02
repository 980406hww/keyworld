package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * Created by shunshikj24 on 2017/9/1.
 */
@TableName(value = "t_service_provider")
public class ServiceProvider implements Serializable{

    @TableField(value = "fUuid")
    private Long uuid;
    @TableField(value = "fServiceProviderName")
    private String serviceProviderName;
    @TableField(value = "fStatus")
    private Integer status;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
