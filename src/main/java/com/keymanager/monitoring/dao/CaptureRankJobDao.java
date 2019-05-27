package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.monitoring.entity.CaptureRankJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */

public interface CaptureRankJobDao extends BaseMapper<CaptureRankJob> {
    List<CaptureRankJob> searchCaptureRankJobs(Page<CaptureRankJob> page, @Param("captureRankJobSearchCriteria") CaptureRankJobSearchCriteria captureRankJobSearchCriteria);

    CaptureRankJob provideCaptureRankJob();

    CaptureRankJob getProcessingJob();

    Boolean getCaptureRankJobStatus(@Param("captureRankJobUuid")Long captureRankJobUuid);

    Long hasUncompletedCaptureRankJob(@Param("groupNames")List<String> groupNames);
    
    CaptureRankJob fetchCaptureRankJob();

    void deleteCaptureRankJob (@Param("qzSettingUuid") Long qzSettingUuid, @Param("operationType") String operationType);
    
    List<CaptureRankJob> searchFiveMiniSetCheckingJobs();
    
    int searchThreeMiniStatusEqualsOne(@Param("terminalType") String terminalType, @Param("groupName") String groupName);
}
