package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.entity.AlgorithmTestDataStatistics;
import com.keymanager.ckadmin.vo.AlgorithmTestDataStatisticsVo;

/**
 * <p>
 * 算法执行统计信息表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-27
 */
public interface AlgorithmTestResultStatisticsService {

    void saveAlgorithmTaskDataDailyTask();

    Page<AlgorithmTestDataStatistics> selectAlgorithmTestResultStatisticsByAlgorithmTestPlanUuid(
        Page<AlgorithmTestDataStatistics> algorithmTestResultStatisticsPage, Long algorithmTestPlanUuid);

    Page<AlgorithmTestDataStatisticsVo> selectAlgorithmTestHistoryByAlgorithmTestPlanUuid(
            Page<AlgorithmTestDataStatisticsVo> algorithmTestResultStatisticsPage,
            Long algorithmTestPlanUuid);
}
