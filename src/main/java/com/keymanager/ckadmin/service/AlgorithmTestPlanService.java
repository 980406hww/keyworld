package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.AlgorithmTestCriteria;
import com.keymanager.ckadmin.criteria.AlgorithmTestTaskCriteria;
import com.keymanager.ckadmin.entity.AlgorithmTestDataStatistics;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;
import java.util.List;

/**
 * <p>
 * 算法测试任务表 服务实现接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
public interface AlgorithmTestPlanService extends IService<AlgorithmTestPlan> {

    Page<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> page, AlgorithmTestCriteria algorithmTestCriteria);

    void saveAlgorithmTestPlan(AlgorithmTestPlan algorithmTestPlan);

    AlgorithmTestPlan getAlgorithmTestPlanByUuid(Long uuid);

    void deletePlanAndTaskByPlanId(Long uuid);

    void deletePlanAndTaskByPlanIds(List<Long> PlanUuids);

    void changeAlgorithmTestPlanStatus(Integer uuid, Integer status);

    void updateAlgorithmTestPlansStatus(List<Integer> uuids, Integer status);

}
