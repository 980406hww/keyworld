package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.Customer;

import java.util.List;
import java.util.Map;

public class AdvertisingAllTypeAndCustomerListCriteria extends BaseCriteria{

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
