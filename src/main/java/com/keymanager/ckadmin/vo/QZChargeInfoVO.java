package com.keymanager.ckadmin.vo;

public class QZChargeInfoVO {

    private String initialKeywordCount;
    private String currentKeywordCount;
    private String receivableAmount;
    private String planChargeDate;
    private String qzOperationTypeUuid;
    private String operationType;


    public String getInitialKeywordCount() {
        return initialKeywordCount;
    }

    public void setInitialKeywordCount(String initialKeywordCount) {
        this.initialKeywordCount = initialKeywordCount;
    }

    public String getCurrentKeywordCount() {
        return currentKeywordCount;
    }

    public void setCurrentKeywordCount(String currentKeywordCount) {
        this.currentKeywordCount = currentKeywordCount;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getPlanChargeDate() {
        return planChargeDate;
    }

    public void setPlanChargeDate(String planChargeDate) {
        this.planChargeDate = planChargeDate;
    }

    public String getQzOperationTypeUuid() {
        return qzOperationTypeUuid;
    }

    public void setQzOperationTypeUuid(String qzOperationTypeUuid) {
        this.qzOperationTypeUuid = qzOperationTypeUuid;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
