package com.keymanager.ckadmin.vo;

/**
 * @ClassName QZRateStatisticsCountVO
 * @Description TODO
 * @Author lhc
 * @Date 2019/10/23 10:56
 * @Version 1.0
 */
public class QZRateStatisticsCountVO {
    private String rateDate;
    private Integer totalCount;
    private Integer riseCount;
    private Integer unchangedCount;
    private Integer fallCount;

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getRiseCount() {
        return riseCount;
    }

    public void setRiseCount(Integer riseCount) {
        this.riseCount = riseCount;
    }

    public Integer getUnchangedCount() {
        return unchangedCount;
    }

    public void setUnchangedCount(Integer unchangedCount) {
        this.unchangedCount = unchangedCount;
    }

    public Integer getFallCount() {
        return fallCount;
    }

    public void setFallCount(Integer fallCount) {
        this.fallCount = fallCount;
    }
}
