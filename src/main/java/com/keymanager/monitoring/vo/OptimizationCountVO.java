package com.keymanager.monitoring.vo;

/**
 * Created by shunshikj08 on 2018/1/25.
 */
public class OptimizationCountVO {
    private String loginName;
    private String email;
    private String keyword;
    private String optimizeGroupName;
    private Integer keywordCount;
    private Integer optimizedCount;
    private Integer invalidRefreshCount;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOptimizeGroupName() {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName(String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }

    public Integer getKeywordCount() {
        return keywordCount;
    }

    public void setKeywordCount(Integer keywordCount) {
        this.keywordCount = keywordCount;
    }

    public Integer getOptimizedCount() {
        return optimizedCount;
    }

    public void setOptimizedCount(Integer optimizedCount) {
        this.optimizedCount = optimizedCount;
    }

    public Integer getInvalidRefreshCount() {
        return invalidRefreshCount;
    }

    public void setInvalidRefreshCount(Integer invalidRefreshCount) {
        this.invalidRefreshCount = invalidRefreshCount;
    }
}
