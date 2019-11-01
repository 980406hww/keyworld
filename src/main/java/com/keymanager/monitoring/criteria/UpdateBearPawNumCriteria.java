package com.keymanager.monitoring.criteria;

/**
 * @Decription TODO
 * @Author: rwxian
 * @Date 2019/9/30 15:12
 */
public class UpdateBearPawNumCriteria extends BaseCriteria {

    private String relatedUrl;
    private String bearPawNum;
    private String loginToken;

    public String getRelatedUrl() {
        return relatedUrl;
    }

    public void setRelatedUrl(String relatedUrl) {
        this.relatedUrl = relatedUrl;
    }

    public String getBearPawNum() {
        return bearPawNum;
    }

    public void setBearPawNum(String bearPawNum) {
        this.bearPawNum = bearPawNum;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
}
