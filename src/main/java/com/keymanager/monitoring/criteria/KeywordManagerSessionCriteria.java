package com.keymanager.monitoring.criteria;

public class KeywordManagerSessionCriteria {

    private String sessionID;
    private String attributeName;
    private boolean resetPagingParam;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public boolean getResetPagingParam() {
        return resetPagingParam;
    }

    public void setResetPagingParam(boolean resetPagingParam) {
        this.resetPagingParam = resetPagingParam;
    }
}
