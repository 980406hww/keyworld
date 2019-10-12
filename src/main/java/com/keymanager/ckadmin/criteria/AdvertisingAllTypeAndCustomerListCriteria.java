package com.keymanager.ckadmin.criteria;


import com.keymanager.ckadmin.criteria.base.BaseCriteria;
import com.keymanager.ckadmin.entity.Customer;

import java.util.List;
import java.util.Map;

public class AdvertisingAllTypeAndCustomerListCriteria extends BaseCriteria {

    List<Map> advertisingType;
    List<Map> advertisingArcType;
    List<Customer> customerList;

    public List<Map> getAdvertisingType() {
        return advertisingType;
    }

    public void setAdvertisingType(List<Map> advertisingType) {
        this.advertisingType = advertisingType;
    }

    public List<Map> getAdvertisingArcType() {
        return advertisingArcType;
    }

    public void setAdvertisingArcType(List<Map> advertisingArcType) {
        this.advertisingArcType = advertisingArcType;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
