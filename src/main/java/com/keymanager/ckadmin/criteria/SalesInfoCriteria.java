package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

public class SalesInfoCriteria extends BaseCriteria {

    private String salesName;

    private String managePart;

    public String getManagePart() {
        return managePart;
    }

    public void setManagePart(String managePart) {
        this.managePart = managePart;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }
}
