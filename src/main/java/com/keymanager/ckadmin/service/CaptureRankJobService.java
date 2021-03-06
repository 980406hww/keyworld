package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.ckadmin.criteria.ExternalCaptureJobCriteria;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import java.util.List;
import java.util.Map;

public interface CaptureRankJobService extends IService<CaptureRankJob> {

    void qzAddCaptureRankJob(String group, long qzSettingUuid, long customerUuid, String operationType, String userName);

    CaptureRankJob findExistCaptureRankJob(Long qzSettingUuid, String operationType);

    void deleteCaptureRankJob(Long qzSettingUuid, String operationType);

    Boolean hasUncompletedCaptureRankJob(List<String> groupNames, String china);

    Page<CaptureRankJob> selectPageByCriteria(CaptureRankJobSearchCriteria criteria);

    void saveCaptureRankJob(Map map, String loginName);

    CaptureRankJob getCaptureRankJobAndCustomerName(Long uuid);

    void changeCaptureRankJobStatus(CaptureRankJob captureRankJob, String loginName);

    void changeCaptureRankJobStatuses(List<Long> uuids, String loginName, boolean status);

    void resetCaptureRankJobs(List<Long> uuids);

    List<Long> getCaptureRankJobUuids(List<Long> uuids);

    void updateCaptureRankJobCustomerUuids(List<Long> jobUuids, Long customerUuid);

    void searchFiveMiniSetCheckingJobs();

    CaptureRankJob provideCaptureRankJob(ExternalCaptureJobCriteria captureJobCriteria);

    Boolean getCaptureRankJobStatus(Long captureRankJobUuid);

    void completeCaptureRankJob(CaptureRankJob captureRankJob);

    void completeCaptureRankJobTempTwo(CaptureRankJob captureRankJob);

    CaptureRankJob checkingCaptureRankJobCompleted(ExternalCaptureJobCriteria captureJobCriteria);
}
