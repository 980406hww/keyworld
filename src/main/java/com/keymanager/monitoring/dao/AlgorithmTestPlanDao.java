package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.AlgorithmTestPlanSearchCriteria;
import com.keymanager.monitoring.entity.AlgorithmTestPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 算法测试任务表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
public interface AlgorithmTestPlanDao extends BaseMapper<AlgorithmTestPlan> {
    List<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> page, @Param("algorithmTestPlanSearchCriteria") AlgorithmTestPlanSearchCriteria algorithmTestPlanSearchCriteria);

    void updateCaptureRankJobsStatus(@Param("uuids") List<Long> uuids,@Param("status") Integer status);
}
