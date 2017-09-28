package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CaptureCurrentRankJobCriteria;
import com.keymanager.monitoring.dao.CaptureCurrentRankJobDao;
import com.keymanager.monitoring.entity.CaptureCurrentRankJob;
import com.keymanager.monitoring.enums.CaptureRankExectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@Service
public class CaptureCurrentRankJobService extends ServiceImpl<CaptureCurrentRankJobDao, CaptureCurrentRankJob> {
    @Autowired
    private CaptureCurrentRankJobDao captureCurrentRankJobDao;

    public CaptureCurrentRankJob provideCaptureCurrentRankJob() {
        CaptureCurrentRankJob captureCurrentRankJob = null;
        captureCurrentRankJob = captureCurrentRankJobDao.searchProcessingJob();
        if (captureCurrentRankJob != null) {
            return captureCurrentRankJob;
        } else {
            captureCurrentRankJob = captureCurrentRankJobDao.provideCaptureCurrentRankJob();
            captureCurrentRankJob.setStartTime(new Date());
            captureCurrentRankJob.setExectionStatus(CaptureRankExectionStatus.Processing.name());
            captureCurrentRankJobDao.updateById(captureCurrentRankJob);
            return captureCurrentRankJob;
        }
    }

    public Page<CaptureCurrentRankJob> searchCaptureCurrentRankJob(Page<CaptureCurrentRankJob> page, CaptureCurrentRankJobCriteria captureCurrentRankJobCriteria) {
        List<CaptureCurrentRankJob> captureCurrentRankJobs = captureCurrentRankJobDao.searchCaptureCurrentRankJob(page, captureCurrentRankJobCriteria);
        page.setRecords(captureCurrentRankJobs);
        return page;
    }
}
