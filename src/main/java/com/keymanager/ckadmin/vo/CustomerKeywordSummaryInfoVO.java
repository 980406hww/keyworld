package com.keymanager.ckadmin.vo;

/**
 * @Author zhoukai
 * @Date 2018/11/27 15:46
 **/
public class CustomerKeywordSummaryInfoVO {

    private String keyword;
    private String terminalType;
    private int status;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

