package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CaptureRankJobDao;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import com.keymanager.ckadmin.enums.CaptureRankExectionStatus;
import com.keymanager.ckadmin.service.CaptureRankJobService;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("captureRankJobService2")
public class CaptureRankJobServiceImpl extends
    ServiceImpl<CaptureRankJobDao, CaptureRankJob> implements CaptureRankJobService {

    @Resource(name = "captureRankJobDao2")
    private CaptureRankJobDao captureRankJobDao;

    @Override
    public void qzAddCaptureRankJob(String group, long qzSettingUuid, long customerUuid,
        String operationType, String userName) {
        CaptureRankJob captureRankJob = new CaptureRankJob();
        captureRankJob.setGroupNames(group);
        captureRankJob.setQzSettingUuid(qzSettingUuid);
        captureRankJob.setCustomerUuid(customerUuid);
        captureRankJob.setOperationType(operationType);
        captureRankJob.setExectionType("Everyday");
        captureRankJob.setCaptureInterval(500);
        captureRankJob.setPageSize(50);
        captureRankJob.setRowNumber(50);
        captureRankJob.setExectionTime(Time.valueOf("00:05:00"));
        captureRankJob.setExectionStatus(CaptureRankExectionStatus.New.name());
        captureRankJob.setCreateBy(userName);
        captureRankJob.setUpdateBy(userName);
        captureRankJob.setRankJobType("Common");// 配上QZSettingUuid确定为整站任务
        captureRankJob.setRankJobArea("China");
        captureRankJob.setUpdateTime(new Date());
        captureRankJobDao.insert(captureRankJob);
    }

    @Override
    public CaptureRankJob findExistCaptureRankJob(Long qzSettingUuid, String operationType) {
        return captureRankJobDao.findExistCaptureRankJob(qzSettingUuid, operationType);
    }

    @Override
    public void deleteCaptureRankJob(Long qzSettingUuid, String operationType) {
        captureRankJobDao.deleteCaptureRankJob(qzSettingUuid, operationType);
    }

    @Override
    public Boolean hasUncompletedCaptureRankJob(List<String> groupNames, String rankJobArea) {
        return captureRankJobDao.hasUncompletedCaptureRankJob(groupNames, rankJobArea) != null;
    }
}
