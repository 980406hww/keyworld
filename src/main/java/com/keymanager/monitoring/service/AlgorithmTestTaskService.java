package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.AlgorithmTestTaskDao;
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

    public List<AlgorithmTestTask> selectAlgorithmTestTasksByAlgorithmTestPlanUuid(Long algorithmTestPlanUuid) {
        return algorithmTestTaskDao.selectAlgorithmTestTasksByAlgorithmTestPlanUuid(algorithmTestPlanUuid);
    }
}
