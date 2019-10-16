package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

/**
 * @ClassName AlgorithmTestTaskCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/10/16 17:22
 * @Version 1.0
 */
public class AlgorithmTestTaskCriteria extends BaseCriteria {

    private Long algorithmTestPlanUuid;

    public Long getAlgorithmTestPlanUuid() {
        return algorithmTestPlanUuid;
    }

    public void setAlgorithmTestPlanUuid(Long algorithmTestPlanUuid) {
        this.algorithmTestPlanUuid = algorithmTestPlanUuid;
    }
}
