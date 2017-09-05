package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by shunshikj20 on 2017/9/5.
 */
@TableName(value = "t_ck_position_index_log")
public class CustomerKeywordPositionIndexLog extends BaseEntity {
    @TableField(value = "fType")
    private String type;

    @TableField(value = "fCustomerKeywordUuid")
    private Long customerKeywordUuid;

    @TableField(value = "fPositionNumber")
    private Integer positionNumber;

    @TableField(value = "fIndexCount")
    private Integer indexCount;

    @TableField(value = "fIP")
    private String ip;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCustomerKeywordUuid() {
        return customerKeywordUuid;
    }

    public void setCustomerKeywordUuid(Long customerKeywordUuid) {
        this.customerKeywordUuid = customerKeywordUuid;
    }

    public Integer getPositionNumber() {
        return positionNumber;
    }

    public void setPositionNumber(Integer positionNumber) {
        this.positionNumber = positionNumber;
    }

    public Integer getIndexCount() {
        return indexCount;
    }

    public void setIndexCount(Integer indexCount) {
        this.indexCount = indexCount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
