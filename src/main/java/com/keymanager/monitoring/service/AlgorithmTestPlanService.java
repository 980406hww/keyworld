package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.AlgorithmTestPlanSearchCriteria;
import com.keymanager.monitoring.dao.AlgorithmTestPlanDao;
import com.keymanager.monitoring.entity.AlgorithmTestPlan;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 算法测试任务表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Service
public class AlgorithmTestPlanService extends ServiceImpl<AlgorithmTestPlanDao, AlgorithmTestPlan>  {

    @Autowired
    private AlgorithmTestPlanDao algorithmTestPlanDao;

    public Page<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> algorithmTestPlanPage, AlgorithmTestPlanSearchCriteria algorithmTestPlanSearchCriteria) {
        List<AlgorithmTestPlan> algorithmTestPlans =  algorithmTestPlanDao.searchAlgorithmTestPlans(algorithmTestPlanPage,algorithmTestPlanSearchCriteria);
        algorithmTestPlanPage.setRecords(algorithmTestPlans);
        return algorithmTestPlanPage;
    }

    public void saveAlgorithmTestPlan(AlgorithmTestPlan algorithmTestPlan) {
        if (algorithmTestPlan.getUuid() == null){
            algorithmTestPlan.setStatus(1);
            algorithmTestPlanDao.insert(algorithmTestPlan);
        }else {
            algorithmTestPlan.setUpdateTime(new Date());
            algorithmTestPlanDao.updateById(algorithmTestPlan);
        }
    }

    public void changeAlgorithmTestPlanStatus(Long uuid, Integer status) {
        AlgorithmTestPlan algorithmTestPlan = new AlgorithmTestPlan();
        algorithmTestPlan.setUuid(uuid);
        algorithmTestPlan.setStatus(status);
        algorithmTestPlanDao.updateById(algorithmTestPlan);
    }

    public void updateAlgorithmTestPlansStatus(List<Long> uuids, Integer status) {
        algorithmTestPlanDao.updateAlgorithmTestPlansStatus(uuids,status);
    }

    public synchronized AlgorithmTestPlan selectOneAvailableAlgorithmTestPlan() {
        AlgorithmTestPlan algorithmTestPlan = algorithmTestPlanDao.selectOneAvailableAlgorithmTestPlan();
        algorithmTestPlanDao.updateExcuteQueryTime(algorithmTestPlan.getUuid());
        return algorithmTestPlan;
    }
}
