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
   public List<CaptureRankJob> searchCaptureRankJobs(Page<CaptureRankJob> page, @Param("captureRankJobSearchCriteria") CaptureRankJobSearchCriteria captureRankJobSearchCriteria);

   public CaptureRankJob provideCaptureRankJob();

   public CaptureRankJob getProcessingJob();

   Boolean getCaptureRankJobStatus(@Param("captureRankJobUuid")Long captureRankJobUuid);

   Boolean hasCaptureRankJob();
}
