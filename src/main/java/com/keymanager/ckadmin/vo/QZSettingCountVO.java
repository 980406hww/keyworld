package com.keymanager.ckadmin.vo;

/**
 * @ClassName QZSettingCountVO
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/17 15:05
 * @Version 1.0
 */
public class QZSettingCountVO {
    private Long customerUuid;
    private Integer totalCount;
    private Integer activeCount;
    private Integer pauseCount;
    private Integer downCount;
    private Integer otherCount;

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
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

    public Integer getPauseCount() {
        return pauseCount;
    }

    public void setPauseCount(Integer pauseCount) {
        this.pauseCount = pauseCount;
    }

    public Integer getDownCount() {
        return downCount;
    }

    public void setDownCount(Integer downCount) {
        this.downCount = downCount;
    }

    public Integer getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(Integer otherCount) {
        this.otherCount = otherCount;
    }
}
