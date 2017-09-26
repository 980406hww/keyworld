package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CaptureCurrentRankJob;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */

public interface CaptureCurrentRankJobDao extends BaseMapper<CaptureCurrentRankJob> {
   public List<CaptureCurrentRankJob> searchCaptureCurrentRankJobs();
}
