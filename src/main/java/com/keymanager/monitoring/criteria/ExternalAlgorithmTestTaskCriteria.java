package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.AlgorithmTestTask;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import java.util.List;

/**
 * @ClassName ExternalAlgorithmTestTaskCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/17 10:26
 * @Version 1.0
 */
public class ExternalAlgorithmTestTaskCriteria extends BaseCriteria{

    private Customer customer;
    private GroupCriteria groupCriteria;
    private List<CustomerKeyword> customerKeywords;
    private AlgorithmTestTask algorithmTestTask;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public GroupCriteria getGroupCriteria() {
        return groupCriteria;
    }

    public void setGroupCriteria(GroupCriteria groupCriteria) {
        this.groupCriteria = groupCriteria;
    }

    public List<CustomerKeyword> getCustomerKeywords() {
        return customerKeywords;
    }

    public void setCustomerKeywords(List<CustomerKeyword> customerKeywords) {
        this.customerKeywords = customerKeywords;
    }

    public AlgorithmTestTask getAlgorithmTestTask() {
        return algorithmTestTask;
    }

    public void setAlgorithmTestTask(AlgorithmTestTask algorithmTestTask) {
        this.algorithmTestTask = algorithmTestTask;
    }
}
