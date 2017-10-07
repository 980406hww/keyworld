package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.CaptureRankJob;

public class CaptureRankJobCriteria extends BaseCriteria {
    private CaptureRankJob captureRankJob;

    public CaptureRankJob getCaptureRankJob() {
        return captureRankJob;
    }

    public void setCaptureRankJob(CaptureRankJob captureRankJob) {
        this.captureRankJob = captureRankJob;
    }

    public CaptureRankJobCriteria() {
    }

    public CaptureRankJobCriteria(CaptureRankJob captureRankJob) {
        this.captureRankJob = captureRankJob;
    }
}
