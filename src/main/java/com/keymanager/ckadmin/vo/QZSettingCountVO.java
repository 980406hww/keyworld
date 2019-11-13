package com.keymanager.ckadmin.vo;

/**
 * @ClassName QZSettingCountVO
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/17 15:05
 * @Version 1.0
 */
public class QZSettingCountVO {

    private String terminalType;
    private Integer renewalStatus;

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(Integer renewalStatus) {
        this.renewalStatus = renewalStatus;
    }
}
