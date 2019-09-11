package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.CaptureRankJob;

public interface CaptureRankJobService extends IService<CaptureRankJob> {

    void qzAddCaptureRankJob(String group, long qzSettingUuid, long customerUuid,
        String operationType, String userName);

    CaptureRankJob findExistCaptureRankJob(Long qzSettingUuid, String operationType);

    void deleteCaptureRankJob(Long qzSettingUuid, String operationType);
}
