package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.ExternalAlgorithmTestTaskCriteria;
import com.keymanager.ckadmin.entity.AlgorithmTestTask;


/**
 * <p>
 * 算法测试任务表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */

public interface AlgorithmTestTaskService {

    Page<AlgorithmTestTask> selectAlgorithmTestTasksByAlgorithmTestPlanUuid(Page<AlgorithmTestTask> algorithmTestTaskPage, Long algorithmTestPlanUuid);

    void saveAlgorithmTestTask(ExternalAlgorithmTestTaskCriteria externalAlgorithmTestTaskCriteria);
}
