package com.keymanager.monitoring.vo;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/5/24 14:54
 **/
public class ExternalQZKeywordRankInfoResultVO {
    private String userName;
    private String password;
    private Long qzSettingUuid;
    private String crawlerStatus;
    List<ExternalQzKeywordRankInfoVO> qzKeywordRankInfoVos;

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public Long getQzSettingUuid () {
        return qzSettingUuid;
    }

    public void setQzSettingUuid (Long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public String getCrawlerStatus () {
        return crawlerStatus;
    }

    public void setCrawlerStatus (String crawlerStatus) {
        this.crawlerStatus = crawlerStatus;
    }

    public List<ExternalQzKeywordRankInfoVO> getQzKeywordRankInfoVos () {
        return qzKeywordRankInfoVos;
    }

    public void setQzKeywordRankInfoVos (List<ExternalQzKeywordRankInfoVO> qzKeywordRankInfoVos) {
        this.qzKeywordRankInfoVos = qzKeywordRankInfoVos;
    }
}
