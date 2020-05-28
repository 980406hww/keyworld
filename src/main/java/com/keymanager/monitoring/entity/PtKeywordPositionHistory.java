package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "cms_keyword_position_history")
public class PtKeywordPositionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "fUuid")
    private Long keywordId;

    @TableField(value = "SYSTEM_POSITION")
    private Integer systemPosition;

    @TableField(value = "CUSTOMER_POSITION")
    private Integer customerPosition;

    @TableField(value = "RECORD_DATE")
    private Date recordDate;

    public Long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
    }

    public Integer getSystemPosition() {
        return systemPosition;
    }

    public void setSystemPosition(Integer systemPosition) {
        this.systemPosition = systemPosition;
    }

    public Integer getCustomerPosition() {
        return customerPosition;
    }

    public void setCustomerPosition(Integer customerPosition) {
        this.customerPosition = customerPosition;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }
}
