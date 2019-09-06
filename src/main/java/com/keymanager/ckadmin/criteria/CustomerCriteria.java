package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

/**
 * @ClassName CustomerCriteria
 * @Description 客户查询条件类
 * @Author lhc
 * @Date 2019/9/2 10:36
 * @Version 1.0
 */
public class CustomerCriteria extends BaseCriteria {
    private String contactPerson;
    private String type;
    private String qq;
    private String telphone;
    private String loginName;
    private String remark;
    private String entryType;
    private Integer status;
    private String terminalType;

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    @Override
    public String toString() {
        return "CustomerCriteria{" +
            "contactPerson='" + contactPerson + '\'' +
            ", type='" + type + '\'' +
            ", qq='" + qq + '\'' +
            ", telphone='" + telphone + '\'' +
            ", loginName='" + loginName + '\'' +
            ", remark='" + remark + '\'' +
            ", entryType='" + entryType + '\'' +
            ", status=" + status +
            ", terminalType='" + terminalType + '\'' +
            '}';
    }
}