package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.AlgorithmTestCriteria;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;

/**
 * <p>
 * 算法测试任务表 服务实现接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
public interface AlgorithmTestPlanInterface extends IService<AlgorithmTestPlan> {

    Page<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> page, AlgorithmTestCriteria algorithmTestCriteria);

    void saveAlgorithmTestPlan(AlgorithmTestPlan algorithmTestPlan);
}
