package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.SnapshotHistory;
import java.util.List;

public class SnapshotHistoryCriteria extends BaseCriteria {
    private List<SnapshotHistory> snapshotList;

    public List<SnapshotHistory> getSnapshotList() {
        return snapshotList;
    }

    public void setSnapshotList(List<SnapshotHistory> snapshotList) {
        this.snapshotList = snapshotList;
    }
}
