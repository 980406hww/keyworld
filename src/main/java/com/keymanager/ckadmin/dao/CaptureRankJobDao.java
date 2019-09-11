package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("captureRankJobDao2")
public interface CaptureRankJobDao extends BaseMapper<CaptureRankJob> {

    CaptureRankJob findExistCaptureRankJob(@Param("qzSettingUuid") Long qzSettingUuid,
        @Param("operationType") String operationType);

    void deleteCaptureRankJob(@Param("qzSettingUuid") Long qzSettingUuid,
        @Param("operationType") String operationType);
}
