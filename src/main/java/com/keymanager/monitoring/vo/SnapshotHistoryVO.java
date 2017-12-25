package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.SnapshotHistory;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/12/22.
 */
public class SnapshotHistoryVO {
    private List<String> customerInfos;
    private List<String> searchEngineInfos;
    private List<SnapshotHistory> snapshotHistories;

    public List<String> getCustomerInfos() {
        return customerInfos;
    }

    public void setCustomerInfos(List<String> customerInfos) {
        this.customerInfos = customerInfos;
    }

    public List<String> getSearchEngineInfos() {
        return searchEngineInfos;
    }

    public void setSearchEngineInfos(List<String> searchEngineInfos) {
        this.searchEngineInfos = searchEngineInfos;
    }

    public List<SnapshotHistory> getSnapshotHistories() {
        return snapshotHistories;
    }

    public void setSnapshotHistories(List<SnapshotHistory> snapshotHistories) {
        this.snapshotHistories = snapshotHistories;
    }
}
