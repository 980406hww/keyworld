package com.keymanager.monitoring.vo;

/**
 * @Author zhoukai
 * @Date 2018/11/27 15:46
 **/
public class CustomerKeywordSummaryInfoVO {
    private String keyword_terminal;
    private String terminalType;
    private int status;

    public String getKeyword_terminal () {
        return keyword_terminal;
    }

    public void setKeyword_terminal (String keyword_terminal) {
        this.keyword_terminal = keyword_terminal;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }
}
