package com.keymanager.monitoring.criteria;

/**
 * Created by shunshikj22 on 2017/9/6.
 */
public class SupplierCriteria extends BaseCriteria{
    private String contactPerson;

    private String qq;

    private String phone;

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
