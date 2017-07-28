package com.keymanager.monitoring.vo;

import java.util.Date;

public class QZChargeInfoVO {
    private String pcInitialKeywordCount;
    private String pcCurrentKeywordCount;
    private String pcReceivableAmount;
    private String pcPlanChargeDate;
    private String pcQzOperationTypeUuid;

    public String getPcInitialKeywordCount() {
        return pcInitialKeywordCount;
    }

    public void setPcInitialKeywordCount(String pcInitialKeywordCount) {
        this.pcInitialKeywordCount = pcInitialKeywordCount;
    }

    public String getPcCurrentKeywordCount() {
        return pcCurrentKeywordCount;
    }

    public void setPcCurrentKeywordCount(String pcCurrentKeywordCount) {
        this.pcCurrentKeywordCount = pcCurrentKeywordCount;
    }

    public String getPcReceivableAmount() {
        return pcReceivableAmount;
    }

    public void setPcReceivableAmount(String pcReceivableAmount) {
        this.pcReceivableAmount = pcReceivableAmount;
    }

    public String getPcPlanChargeDate() {
        return pcPlanChargeDate;
    }

    public void setPcPlanChargeDate(String pcPlanChargeDate) {
        this.pcPlanChargeDate = pcPlanChargeDate;
    }

    public String getPcQzOperationTypeUuid() {
        return pcQzOperationTypeUuid;
    }

    public void setPcQzOperationTypeUuid(String pcQzOperationTypeUuid) {
        this.pcQzOperationTypeUuid = pcQzOperationTypeUuid;
    }
}
