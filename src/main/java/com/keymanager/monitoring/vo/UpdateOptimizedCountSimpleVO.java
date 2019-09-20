package com.keymanager.monitoring.vo;

import java.io.Serializable;

public class UpdateOptimizedCountSimpleVO implements Serializable{
    private Long customerKeywordUuid;
    private int totalCount;
    private int totalSucceedCount;
    private int lastContinueFailedCount;

    public Long getCustomerKeywordUuid() {
        return customerKeywordUuid;
    }

    public void setCustomerKeywordUuid(Long customerKeywordUuid) {
        this.customerKeywordUuid = customerKeywordUuid;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalSucceedCount() {
        return totalSucceedCount;
    }

    public void setTotalSucceedCount(int totalSucceedCount) {
        this.totalSucceedCount = totalSucceedCount;
    }

    public int getLastContinueFailedCount() {
        return lastContinueFailedCount;
    }

    public void setLastContinueFailedCount(int lastContinueFailedCount) {
        this.lastContinueFailedCount = lastContinueFailedCount;
    }
}
