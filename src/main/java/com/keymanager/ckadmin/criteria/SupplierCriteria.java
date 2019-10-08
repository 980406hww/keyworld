package com.keymanager.ckadmin.criteria;


import com.keymanager.ckadmin.criteria.base.BaseCriteria;

/**
 * Created by shunshikj22 on 2017/9/6.
 */
public class SupplierCriteria extends BaseCriteria {

    private String contactPerson;

    private String qq;

    private String phone;

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
