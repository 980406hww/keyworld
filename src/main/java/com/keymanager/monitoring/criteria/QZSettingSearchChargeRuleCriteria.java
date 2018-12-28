package com.keymanager.monitoring.criteria;

/**
 * @Author zhoukai
 * @Date 2018/12/28 11:12
 **/
public class QZSettingSearchChargeRuleCriteria {
    private long qzSettingUuid;
    private String terminalType;

    public long getQzSettingUuid () {
        return qzSettingUuid;
    }

    public void setQzSettingUuid (long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }
}
