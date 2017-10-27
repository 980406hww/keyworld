package com.keymanager.monitoring.criteria;

public class NegativeKeywordNameCriteria extends BaseCriteria{
    private String group;
    private String isExistEmail;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIsExistEmail() {
        return isExistEmail;
    }

    public void setIsExistEmail(String isExistEmail) {
        this.isExistEmail = isExistEmail;
    }
}
