package com.keymanager.monitoring.vo;

public class QZOperationTypeVO {
     private Long qzSettingUuid;
     private String operationType;
     private Integer startKeywordCount;
     private Integer endKeywordCount;
     private Integer amount;

    public Long getQzSettingUuid() {
        return qzSettingUuid;
    }

    public void setQzSettingUuid(Long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public int getStartKeywordCount() {
        return startKeywordCount;
    }

    public void setStartKeywordCount(int startKeywordCount) {
        this.startKeywordCount = startKeywordCount;
    }

    public int getEndKeywordCount() {
        return endKeywordCount;
    }

    public void setEndKeywordCount(int endKeywordCount) {
        this.endKeywordCount = endKeywordCount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
