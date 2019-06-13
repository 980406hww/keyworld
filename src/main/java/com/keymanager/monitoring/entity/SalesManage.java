package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @author wjianwu 2019/6/6 15:02
 */
@TableName(value = "t_sales_manage")
public class SalesManage extends BaseEntity {

    @TableField(value = "fSalesName")
    private String salesName;

    @TableField(value = "fTelephone")
    private String telephone;

    @TableField(value = "fQQ")
    private String qq;

    @TableField(value = "fWeChat")
    private String weChat;

    @TableField(value = "fQuickResponseCode")
    private String quickResponseCode;

    @TableField(value = "fEmail")
    private String email;

    @TableField(value = "fManagePart")
    private String managePart;

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getQuickResponseCode() {
        return quickResponseCode;
    }

    public void setQuickResponseCode(String quickResponseCode) {
        this.quickResponseCode = quickResponseCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getManagePart() {
        return managePart;
    }

    public void setManagePart(String managePart) {
        this.managePart = managePart;
    }
}
