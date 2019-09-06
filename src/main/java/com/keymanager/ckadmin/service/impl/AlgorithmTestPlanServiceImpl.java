package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.AlgorithmTestCriteria;
import com.keymanager.ckadmin.dao.AlgorithmTestPlanDao;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;
import com.keymanager.ckadmin.service.AlgorithmTestPlanService;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 算法测试任务表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Service("algorithmTestPlanService2")
public class AlgorithmTestPlanServiceImpl extends ServiceImpl<AlgorithmTestPlanDao, AlgorithmTestPlan> implements
        AlgorithmTestPlanService {

    @Resource(name = "algorithmTestPlanDao2")
    private AlgorithmTestPlanDao algorithmTestPlanDao;

    @Override
    public Page<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> page, AlgorithmTestCriteria algorithmTestCriteria) {
        List<AlgorithmTestPlan> algorithmTestPlanList = algorithmTestPlanDao.searchAlgorithmTestPlans(page, algorithmTestCriteria);
        page.setRecords(algorithmTestPlanList);
        return page;
    }

    @Override
    public void saveAlgorithmTestPlan(AlgorithmTestPlan algorithmTestPlan) {
        if (algorithmTestPlan.getUuid() == null) {
            algorithmTestPlan.setStatus(1);
            algorithmTestPlanDao.insert(algorithmTestPlan);
        } else {
            algorithmTestPlan.setUpdateTime(new Date());
            algorithmTestPlanDao.updateById(algorithmTestPlan);
        }
    }
}