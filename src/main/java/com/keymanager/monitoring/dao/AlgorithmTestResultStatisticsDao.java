package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.AlgorithmTestDataStatistics;
import java.util.List;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 算法执行统计信息表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-27
 */
public interface AlgorithmTestResultStatisticsDao extends BaseMapper<AlgorithmTestDataStatistics> {
    void saveAlgorithmTaskDataDaily();

    List<AlgorithmTestDataStatistics> selectAlgorithmTestResultStatisticsByAlgorithmTestPlanUuid(
            Page<AlgorithmTestDataStatistics> algorithmTestResultStatisticsPage, @Param("algorithmTestPlanUuid") Long algorithmTestPlanUuid);
}
