package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ExternalAlgorithmTestTaskCriteria;
import com.keymanager.monitoring.entity.AlgorithmTestTask;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 算法测试任务表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
public interface AlgorithmTestTaskDao extends BaseMapper<AlgorithmTestTask> {

    List<AlgorithmTestTask> selectAlgorithmTestTasksByAlgorithmTestPlanUuid(Page<AlgorithmTestTask> algorithmTestTaskPage, @Param("algorithmTestPlanUuid") Long algorithmTestPlanUuid);

    void saveAlgorithmTestTask( @Param("externalAlgorithmTestTaskCriteria") ExternalAlgorithmTestTaskCriteria externalAlgorithmTestTaskCriteria);

    void deleteTaskByPlanUuid(@Param("planUuid") Long planUuid);

    void deleteTaskByPlanUuids(@Param("planUuids") List<Long> planUuids);
}
