package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.AlgorithmTestTask;

import java.util.List;

/**
 * @ClassName ExternalAlgorithmTestTaskCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/17 10:26
 * @Version 1.0
 */
public class ExternalAlgorithmTestTaskCriteria extends BaseCriteria{
    List<AlgorithmTestTask> algorithmTestTasks;

    public List<AlgorithmTestTask> getAlgorithmTestTasks() {
        return algorithmTestTasks;
    }

    public void setAlgorithmTestTasks(List<AlgorithmTestTask> algorithmTestTasks) {
        this.algorithmTestTasks = algorithmTestTasks;
    }
}
