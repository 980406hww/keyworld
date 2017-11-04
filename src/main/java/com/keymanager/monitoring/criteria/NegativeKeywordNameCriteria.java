package com.keymanager.monitoring.criteria;

public class NegativeKeywordNameCriteria extends BaseCriteria{
    private String group;
    private String hasEmail;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getHasEmail() {
        return hasEmail;
    }

    public void setHasEmail(String hasEmail) {
        this.hasEmail = hasEmail;
    }
}
