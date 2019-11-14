package com.keymanager.ckadmin.vo;

/**
 * @ClassName KeywordCountVO
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/17 14:03
 * @Version 1.0
 */
public class KeywordCountVO {

    private String terminalType;
    private Integer totalCount;
    private Integer activeCount;

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }
}
