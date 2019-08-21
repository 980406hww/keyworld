package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.ExternalAlgorithmTestTaskCriteria;
import com.keymanager.monitoring.dao.AlgorithmTestPlanDao;
import com.keymanager.monitoring.dao.AlgorithmTestTaskDao;
import com.keymanager.monitoring.entity.AlgorithmTestPlan;
import com.keymanager.monitoring.entity.AlgorithmTestTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 算法测试任务表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Service
public class AlgorithmTestTaskService extends ServiceImpl<AlgorithmTestTaskDao, AlgorithmTestTask> {

    @Autowired
    private AlgorithmTestTaskDao algorithmTestTaskDao;

    @Autowired
    private AlgorithmTestPlanDao algorithmTestPlanDao;

    public Page<AlgorithmTestTask> selectAlgorithmTestTasksByAlgorithmTestPlanUuid(Page<AlgorithmTestTask> algorithmTestTaskPage,Long algorithmTestPlanUuid) {

        List<AlgorithmTestTask> algorithmTestTasks = algorithmTestTaskDao.selectAlgorithmTestTasksByAlgorithmTestPlanUuid(algorithmTestTaskPage,algorithmTestPlanUuid);
        algorithmTestTaskPage.setRecords(algorithmTestTasks);
        return algorithmTestTaskPage;
    }

    public void saveAlgorithmTestTask(ExternalAlgorithmTestTaskCriteria externalAlgorithmTestTaskCriteria) {
        AlgorithmTestPlan algorithmTestPlan = new AlgorithmTestPlan();
        algorithmTestPlan.setUuid(externalAlgorithmTestTaskCriteria.getAlgorithmTestPlanUuid());
        algorithmTestPlan.setExcuteStatus(0);
        algorithmTestPlanDao.updateById(algorithmTestPlan);
        algorithmTestTaskDao.saveAlgorithmTestTask(externalAlgorithmTestTaskCriteria);
    }
}
