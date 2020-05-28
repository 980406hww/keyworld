package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "cms_keyword_position_history")
public class PtKeywordPositionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "KEYWORD_ID")
    private Long keywordId;

    @TableField(value = "SYSTEM_POSITION")
    private Integer systemPosition;

    @TableField(value = "CUSTOMER_POSITION")
    private Integer customerPosition;

    @TableField(value = "SEARCH_ENGINE")
    private String searchEngine;

    @TableField(value = "TERMINAL_TYPE")
    private String terminalType;

    @TableField(value = "TODAY_FEE")
    private Double todayFee;

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

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public Double getTodayFee() {
        return todayFee;
    }

    public void setTodayFee(Double todayFee) {
        this.todayFee = todayFee;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }
}
