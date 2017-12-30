package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.SnapshotHistory;
import java.util.List;

public class SnapshotHistoryCriteria extends BaseCriteria {
    private Long customerUuid;
    private String contactPerson;
    private Integer order;
    private String beginDate;
    private String endDate;

    private List<SnapshotHistory> snapshotList;

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<SnapshotHistory> getSnapshotList() {
        return snapshotList;
    }

    public void setSnapshotList(List<SnapshotHistory> snapshotList) {
        this.snapshotList = snapshotList;
    }
}
