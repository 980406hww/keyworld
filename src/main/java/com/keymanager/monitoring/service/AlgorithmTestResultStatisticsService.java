package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.AlgorithmTestResultStatisticsDao;
import com.keymanager.monitoring.entity.AlgorithmTestDataStatistics;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 算法执行统计信息表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-27
 */
@Service
public class AlgorithmTestResultStatisticsService extends
        ServiceImpl<AlgorithmTestResultStatisticsDao, AlgorithmTestDataStatistics> {

    @Autowired
    private AlgorithmTestResultStatisticsDao algorithmTestResultStatisticsDao;

    public void saveAlgorithmTaskDataDailyTask(){
        algorithmTestResultStatisticsDao.saveAlgorithmTaskDataDaily();
    }

    public Page<AlgorithmTestDataStatistics> selectAlgorithmTestResultStatisticsByAlgorithmTestPlanUuid(
            Page<AlgorithmTestDataStatistics> algorithmTestResultStatisticsPage, Long algorithmTestPlanUuid) {
        List<AlgorithmTestDataStatistics> algorithmTestResultStatistics = algorithmTestResultStatisticsDao.selectAlgorithmTestResultStatisticsByAlgorithmTestPlanUuid(algorithmTestResultStatisticsPage,algorithmTestPlanUuid);
        algorithmTestResultStatisticsPage.setRecords(algorithmTestResultStatistics);
        return algorithmTestResultStatisticsPage;
    }
}
