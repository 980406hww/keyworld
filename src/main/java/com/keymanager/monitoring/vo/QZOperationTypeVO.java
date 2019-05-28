package com.keymanager.monitoring.vo;

public class QZOperationTypeVO {
     private Long qzSettingUuid;
     private String operationType;
     private String standardType;
     private String standardSpecies;
     private String startKeywordCount;
     private String endKeywordCount;
     private String amount;

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

    public String getStandardType () {
        return standardType;
    }

    public void setStandardType (String standardType) {
        this.standardType = standardType;
    }

    public String getStandardSpecies () {
        return standardSpecies;
    }

    public void setStandardSpecies (String standardSpecies) {
        this.standardSpecies = standardSpecies;
    }

    public String getStartKeywordCount() {
        return startKeywordCount;
    }

    public void setStartKeywordCount(String startKeywordCount) {
        this.startKeywordCount = startKeywordCount;
    }

    public String getEndKeywordCount() {
        return endKeywordCount;
    }

    public void setEndKeywordCount(String endKeywordCount) {
        this.endKeywordCount = endKeywordCount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
