package com.keymanager.monitoring.criteria;


/**
 * Created by ljj on 2018/6/28.
 */

public class NegativeStandardSettingCriteria {

    private Long uuid;

    private String keyword;

    private String searchEngine;

    private Integer reachStandard;

    private Long customerUuid;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public Integer getReachStandard() {
        return reachStandard;
    }

    public void setReachStandard(Integer reachStandard) {
        this.reachStandard = reachStandard;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }
}
