package com.keymanager.monitoring.criteria;

import java.util.Date;

/**
 * @ClassName ExternalAlgorithmTestTaskCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/17 10:26
 * @Version 1.0
 */
public class ExternalAlgorithmTestTaskCriteria extends BaseCriteria{

    private Long algorithmTestPlanUuid;

    private String keywordGroup;

    private String customerName;

    private Integer actualKeywordCount;

    private Date startDate;

    public Long getAlgorithmTestPlanUuid() {
        return algorithmTestPlanUuid;
    }

    public void setAlgorithmTestPlanUuid(Long algorithmTestPlanUuid) {
        this.algorithmTestPlanUuid = algorithmTestPlanUuid;
    }

    public String getKeywordGroup() {
        return keywordGroup;
    }

    public void setKeywordGroup(String keywordGroup) {
        this.keywordGroup = keywordGroup;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getActualKeywordCount() {
        return actualKeywordCount;
    }

    public void setActualKeywordCount(Integer actualKeywordCount) {
        this.actualKeywordCount = actualKeywordCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
