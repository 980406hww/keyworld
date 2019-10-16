package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.AlgorithmTestCriteria;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 算法测试任务表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Repository("algorithmTestPlanDao2")
public interface AlgorithmTestPlanDao extends BaseMapper<AlgorithmTestPlan> {

    List<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> page, @Param("algorithmTestCriteria") AlgorithmTestCriteria algorithmTestCriteria);

    AlgorithmTestPlan getAlgorithmTestPlanByUuid(@Param("uuid") Long uuid);

    void updateAlgorithmTestPlansStatus(@Param("uuids") List<Integer> uuids,@Param("status") Integer status);
}
