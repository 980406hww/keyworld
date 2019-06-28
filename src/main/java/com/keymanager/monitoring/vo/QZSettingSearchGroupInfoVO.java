package com.keymanager.monitoring.vo;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/18 11:22
 **/
public class QZSettingSearchGroupInfoVO {
    private int customerKeywordCount;
    private String operationCombineName;
    private int machineCount;
    private List<String> categoryTagNames;

    public int getCustomerKeywordCount () {
        return customerKeywordCount;
    }

    public void setCustomerKeywordCount (int customerKeywordCount) {
        this.customerKeywordCount = customerKeywordCount;
    }

    public String getOperationCombineName () {
        return operationCombineName;
    }

    public void setOperationCombineName (String operationCombineName) {
        this.operationCombineName = operationCombineName;
    }

    public int getMachineCount () {
        return machineCount;
    }

    public void setMachineCount (int machineCount) {
        this.machineCount = machineCount;
    }

    public List<String> getCategoryTagNames () {
        return categoryTagNames;
    }

    public void setCategoryTagNames (List<String> categoryTagNames) {
        this.categoryTagNames = categoryTagNames;
    }
}
