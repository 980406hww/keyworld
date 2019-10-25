package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

@TableName("t_qz_charge_mon")
public class QzChargeMon {

    @TableField("fUuid ")
    private Long uuid;

    @TableField("fQzSettingUuid ")
    private Long qzzSettingUuid;

    @TableField("fOperationDate")
    private Date operationDate;

    @TableField("fOperationType")
    private Integer operationType;

    @TableField("fQzDomain")
    private String qzDomain;

    @TableField("fQzCustomer")
    private String qzCustomer;

    @TableField("fSearchEngine")
    private String searchEngine;

    @TableField("fTerminalType")
    private String terminalType;

    @TableField("fOperationUser")
    private String operationUser;

    @TableField("fIsDel")
    private Integer isDel;

    public Long getQzzSettingUuid() {
        return qzzSettingUuid;
    }

    public void setQzzSettingUuid(Long qzzSettingUuid) {
        this.qzzSettingUuid = qzzSettingUuid;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

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

    public String getQzDomain() {
        return qzDomain;
    }

    public void setQzDomain(String qzDomain) {
        this.qzDomain = qzDomain;
    }

    public String getQzCustomer() {
        return qzCustomer;
    }

    public void setQzCustomer(String qzCustomer) {
        this.qzCustomer = qzCustomer;
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
}
