package com.keymanager.monitoring.criteria;

/**
 * Created by shunshikj24 on 2017/9/27.
 */
public class CaptureRankJobSearchCriteria{
    private String groupNames;
    private String customerID;
    private String operationType;
    private String exectionType;
    private String createBy;
    private String updateBy;

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getExectionType() {
        return exectionType;
    }

    public void setExectionType(String exectionType) {
        this.exectionType = exectionType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
