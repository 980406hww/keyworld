package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.CaptureRankJob;

public interface CaptureRankJobService extends IService<CaptureRankJob> {

    void deleteCaptureRankJob(Long qzSettingUuid, String operationType);
}
