package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.RequsetBean;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 算法测试任务表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Component("algorithmTestPlanDao2")
public interface AlgorithmTestPlanDao2 extends BaseMapper<AlgorithmTestPlan> {

    List<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> page, @Param("queryCondition") RequsetBean requsetBean);
}
