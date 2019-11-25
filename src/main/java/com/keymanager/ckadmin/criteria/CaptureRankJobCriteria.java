package com.keymanager.ckadmin.criteria;


import com.keymanager.ckadmin.criteria.base.ExternalBaseCriteria;
import com.keymanager.ckadmin.entity.CaptureRankJob;

public class CaptureRankJobCriteria extends ExternalBaseCriteria {

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
