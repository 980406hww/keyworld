package com.keymanager.monitoring.criteria;

/**
 * Created by shunshikj22 on 2017/9/6.
 */
public class ApplyKeywordCriteria extends BaseCriteria{
    private String keyword;

    private String applyName;

    private Long applyUuid;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public Long getApplyUuid() {
        return applyUuid;
    }

    public void setApplyUuid(Long applyUuid) {
        this.applyUuid = applyUuid;
    }
}
