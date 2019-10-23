package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

@TableName("t_qz_charge_mon")
public class QzChargeMon {

    @TableField("fUuid ")
    private Long uuid;

    @TableField("fOperationDate")
    private Date operationDate;

    @TableField("fOperationType")
    private Integer operationType;

    @TableField("fOperationObj")
    private String operationObj;

    @TableField("fOperationAmount")
    private String operationAmount;

    @TableField("fSearchEngine")
    private String searchEngine;

    @TableField("fTerminalType")
    private String terminalType;

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public Long getUuid() {
        return uuid;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getOperationObj() {
        return operationObj;
    }

    public void setOperationObj(String operationObj) {
        this.operationObj = operationObj;
    }

    public String getOperationAmount() {
        return operationAmount;
    }

    public void setOperationAmount(String operationAmount) {
        this.operationAmount = operationAmount;
    }
}
