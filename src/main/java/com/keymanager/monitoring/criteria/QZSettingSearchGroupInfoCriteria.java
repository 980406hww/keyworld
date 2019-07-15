package com.keymanager.monitoring.criteria;

/**
 * @Author zhoukai
 * @Date 2018/12/18 10:59
 **/
public class QZSettingSearchGroupInfoCriteria {
    private Long qzSettingUuid;
    private String terminalType;
    private String optimizeGroupName;

    public Long getQzSettingUuid () {
        return qzSettingUuid;
    }

    public void setQzSettingUuid (Long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public String getOptimizeGroupName () {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName (String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }
}
