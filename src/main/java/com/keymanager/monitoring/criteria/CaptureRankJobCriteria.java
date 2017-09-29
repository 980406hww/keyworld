package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.CaptureCurrentRankJob;

public class CaptureRankJobCriteria extends BaseCriteria {
    private CaptureCurrentRankJob captureCurrentRankJob;

    public CaptureCurrentRankJob getCaptureCurrentRankJob() {
        return captureCurrentRankJob;
    }

    public void setCaptureCurrentRankJob(CaptureCurrentRankJob captureCurrentRankJob) {
        this.captureCurrentRankJob = captureCurrentRankJob;
    }

    public CaptureRankJobCriteria() {
    }

    public CaptureRankJobCriteria(CaptureCurrentRankJob captureCurrentRankJob) {
        this.captureCurrentRankJob = captureCurrentRankJob;
    }
}
