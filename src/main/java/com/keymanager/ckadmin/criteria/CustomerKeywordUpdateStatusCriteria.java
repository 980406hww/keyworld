package com.keymanager.ckadmin.criteria;

import java.util.List;

/**
 * @ClassName CustomerKeywordUpdateStatusCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/26 10:00
 * @Version 1.0
 */
public class CustomerKeywordUpdateStatusCriteria {

    private List<Long> uuids;
    private Long customerUuid;
    private Long qzUuid;
    private String terminalType;
    private String type;
    private Integer targetStatus;

    public List<Long> getUuids() {
        return uuids;
    }

    public void setUuids(List<Long> uuids) {
        this.uuids = uuids;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public Long getQzUuid() {
        return qzUuid;
    }

    public void setQzUuid(Long qzUuid) {
        this.qzUuid = qzUuid;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Integer targetStatus) {
        this.targetStatus = targetStatus;
    }
}
