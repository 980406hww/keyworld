package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.CustomerKeyword;
import java.util.List;

public class ExternalCustomerKeywordCriteria extends BaseCriteria{
    List<CustomerKeyword> customerKeywords;

    public List<CustomerKeyword> getCustomerKeywords() {
        return customerKeywords;
    }

    public void setCustomerKeywords(List<CustomerKeyword> customerKeywords) {
        this.customerKeywords = customerKeywords;
    }
}
