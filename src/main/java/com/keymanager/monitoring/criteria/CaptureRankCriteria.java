package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.CaptureCurrentRankJob;

public class CaptureRankCriteria extends BaseCriteria {
    private CaptureCurrentRankJob captureRankJob;

    public CaptureCurrentRankJob getCaptureRankJob() {
        return captureRankJob;
    }

    public void setCaptureRankJob(CaptureCurrentRankJob captureRankJob) {
        this.captureRankJob = captureRankJob;
    }
}
