package com.keymanager.ckadmin.vo;

/**
 * @ClassName KeywordCountVO
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/17 14:03
 * @Version 1.0
 */
public class KeywordCountVO {
    private Long customerUuid;
    private Integer totalCount;
    private Integer activeCount;

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
}
