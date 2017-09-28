package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CaptureCurrentRankJobCriteria;
import com.keymanager.monitoring.entity.CaptureCurrentRankJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */

public interface CaptureCurrentRankJobDao extends BaseMapper<CaptureCurrentRankJob> {
   public List<CaptureCurrentRankJob> searchCaptureCurrentRankJob(Page<CaptureCurrentRankJob> page, @Param("captureCurrentRankJobCriteria") CaptureCurrentRankJobCriteria captureCurrentRankJobCriteria);
   public CaptureCurrentRankJob provideCaptureCurrentRankJob();
   public CaptureCurrentRankJob searchProcessingJob();
}
