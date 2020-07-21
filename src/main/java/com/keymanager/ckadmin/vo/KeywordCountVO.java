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
    private Integer invalidDaysStopCount;
    private Integer optimizeStopCount;
    private Integer noEffectStopCount;

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

    public Integer getInvalidDaysStopCount() {
        return invalidDaysStopCount;
    }

    public void setInvalidDaysStopCount(Integer invalidDaysStopCount) {
        this.invalidDaysStopCount = invalidDaysStopCount;
    }

    public Integer getOptimizeStopCount() {
        return optimizeStopCount;
    }

    public void setOptimizeStopCount(Integer optimizeStopCount) {
        this.optimizeStopCount = optimizeStopCount;
    }

    public Integer getNoEffectStopCount() {
        return noEffectStopCount;
    }

    public void setNoEffectStopCount(Integer noEffectStopCount) {
        this.noEffectStopCount = noEffectStopCount;
    }
}
