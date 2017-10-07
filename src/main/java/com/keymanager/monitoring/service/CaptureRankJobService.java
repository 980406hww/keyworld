package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.monitoring.dao.CaptureRankJobDao;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.enums.CaptureRankExectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@Service
public class CaptureRankJobService extends ServiceImpl<CaptureRankJobDao, CaptureRankJob> {
    @Autowired
    private CaptureRankJobDao captureRankJobDao;

    public CaptureRankJob provideCaptureRankJob() {
        CaptureRankJob captureRankJob = captureRankJobDao.getProcessingJob();
        if (captureRankJob == null) {
            captureRankJob = captureRankJobDao.provideCaptureRankJob();
           if(captureRankJob != null){
               captureRankJob.setStartTime(new Date());
               captureRankJob.setExectionStatus(CaptureRankExectionStatus.Processing.name());
               captureRankJobDao.updateById(captureRankJob);
           }
        }
        return captureRankJob;
    }

    public Page<CaptureRankJob> searchCaptureRankJob(Page<CaptureRankJob> page, CaptureRankJobSearchCriteria captureRankJobSearchCriteria) {
        List<CaptureRankJob> captureRankJobs = captureRankJobDao.searchCaptureRankJobs(page, captureRankJobSearchCriteria);
        page.setRecords(captureRankJobs);
        return page;
    }

    public void saveCaptureRankJob(CaptureRankJob captureRankJob, String terminalType, String loginName)
    {
        captureRankJob.setUpdateTime(new Date());
        captureRankJob.setUpdateBy(loginName);
        captureRankJob.setOperationType(terminalType);
        if (captureRankJob.getUuid() == null) {
            captureRankJob.setExectionStatus(CaptureRankExectionStatus.New.name());
            captureRankJob.setCreateBy(loginName);
            captureRankJobDao.insert(captureRankJob);
        } else {
            captureRankJobDao.updateById(captureRankJob);
        }
    }
}
