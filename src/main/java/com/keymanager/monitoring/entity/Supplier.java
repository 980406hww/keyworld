package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.List;


/**
 * Created by shunshikj22 on 2017/9/5.
 */
@TableName(value = "t_supplier")
public class Supplier extends BaseEntity{

    @TableField(value = "fSupplierName")
    private String supplierName;

    @TableField(value = "fContactPerson")
    private String contactPerson;

    @TableField(value = "fPhone")
    private String phone;

    @TableField(value = "fQq")
    private String qq;

    @TableField(value = "fWeChat")
    private String weChat;

    @TableField(value = "fAddress")
    private String address;

    @TableField(value = "fUrl")
    private String url;

    @TableField(value = "fEmail")
    private String email;

    @TableField(value = "fRemark")
    private String remark;

    @TableField(exist = false)
    private List<SupplierServiceTypeMapping> supplierServiceTypeMappings;

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public List<SupplierServiceTypeMapping> getSupplierServiceTypeMappings() {
        return supplierServiceTypeMappings;
    }

    public void setSupplierServiceTypeMappings(List<SupplierServiceTypeMapping> supplierServiceTypeMappings) {
        this.supplierServiceTypeMappings = supplierServiceTypeMappings;
    }
}
