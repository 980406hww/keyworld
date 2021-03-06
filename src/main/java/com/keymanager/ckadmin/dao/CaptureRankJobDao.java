package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.ckadmin.criteria.ExternalCaptureJobCriteria;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("captureRankJobDao2")
public interface CaptureRankJobDao extends BaseMapper<CaptureRankJob> {

    CaptureRankJob findExistCaptureRankJob(@Param("qzSettingUuid") Long qzSettingUuid, @Param("operationType") String operationType);

    void deleteCaptureRankJob(@Param("qzSettingUuid") Long qzSettingUuid, @Param("operationType") String operationType);

    Long hasUncompletedCaptureRankJob(@Param("groupNames") List<String> groupNames, @Param("rankJobArea") String rankJobArea);

    void changeCaptureRankJobStatuses(@Param("uuids") List<Long> uuids, @Param("updateBy") String updateBy, @Param("captureRankJobStatus") boolean status);

    void resetCaptureRankJobs(@Param("uuids") List<Long> uuids);

    List<Long> getCaptureRankJobUuids(@Param("uuids") List<Long> uuids);

    void updateCaptureRankJobCustomerUuids(@Param("jobUuids") List<Long> jobUuids, @Param("customerUuid") Long customerUuid);

    List<CaptureRankJob> selectPageByCriteria(Page<CaptureRankJob> page, @Param("criteria") CaptureRankJobSearchCriteria criteria);

    List<CaptureRankJob> searchFiveMiniSetCheckingJobs();

    int searchThreeMiniStatusEqualsOne(@Param("captureRankJob") CaptureRankJob captureRankJob);

    Map<String, Long> searchCountByPosition(@Param("captureRankJob") CaptureRankJob captureRankJob);

    CaptureRankJob getProcessingJob(@Param("captureJobCriteria") ExternalCaptureJobCriteria captureJobCriteria);

    CaptureRankJob provideCaptureRankJob(@Param("jobType") Integer jobType, @Param("captureJobCriteria") ExternalCaptureJobCriteria captureJobCriteria);

    Boolean getCaptureRankJobStatus(@Param("captureRankJobUuid")Long captureRankJobUuid);
}
