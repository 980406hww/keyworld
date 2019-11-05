package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.monitoring.criteria.ExternalCaptureJobCriteria;
import com.keymanager.monitoring.entity.CaptureRankJob;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */

public interface CaptureRankJobDao extends BaseMapper<CaptureRankJob> {
    List<CaptureRankJob> searchCaptureRankJobs(Page<CaptureRankJob> page, @Param("captureRankJobSearchCriteria") CaptureRankJobSearchCriteria captureRankJobSearchCriteria);

    CaptureRankJob provideCaptureRankJob(@Param("jobType") Integer jobType, @Param("captureJobCriteria") ExternalCaptureJobCriteria captureJobCriteria);

    CaptureRankJob getProcessingJob(@Param("captureJobCriteria") ExternalCaptureJobCriteria captureJobCriteria);

    Boolean getCaptureRankJobStatus(@Param("captureRankJobUuid")Long captureRankJobUuid);

    void resetCaptureRankJobs(@Param("uuids") List uuids);

    Long hasUncompletedCaptureRankJob(@Param("groupNames")List<String> groupNames, @Param("rankJobArea")String rankJobArea);

    void deleteCaptureRankJob (@Param("qzSettingUuid") Long qzSettingUuid, @Param("operationType") String operationType);
    
    List<CaptureRankJob> searchFiveMiniSetCheckingJobs();
    
    int searchThreeMiniStatusEqualsOne(@Param("captureRankJob") CaptureRankJob captureRankJob);

    Map<String, Long> searchCountByPosition(@Param("captureRankJob") CaptureRankJob captureRankJob);

    CaptureRankJob findExistCaptureRankJob (@Param("qzSettingUuid") Long qzSettingUuid, @Param("operationType") String operationType);

    void updateCaptureRankJobsStatus(@Param("uuids") List<Long> uuids, @Param("updateBy") String updateBy, @Param("captureRankJobStatus") boolean captureRankJobStatus);
}
