package com.keymanager.monitoring.criteria;

/**
 * Created by shunshikj24 on 2017/9/27.
 */
public class CaptureRankJobSearchCriteria{
    private String groupNames;
    private String customerUuid;
    private String operationType;
    private String jobType;
    private String exectionType;
    private String exectionStatus;
    private String createBy;
    private String updateBy;
    private String groupNameFuzzyQuery;

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getExectionType() {
        return exectionType;
    }

    public String getExectionStatus() {
        return exectionStatus;
    }

    public void setExectionStatus(String exectionStatus) {
        this.exectionStatus = exectionStatus;
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

    public String getGroupNameFuzzyQuery() {
        return groupNameFuzzyQuery;
    }

    public void setGroupNameFuzzyQuery(String groupNameFuzzyQuery) {
        this.groupNameFuzzyQuery = groupNameFuzzyQuery;
    }
}
