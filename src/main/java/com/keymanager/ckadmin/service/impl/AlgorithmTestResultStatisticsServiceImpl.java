package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.AlgorithmTestResultStatisticsDao;
import com.keymanager.ckadmin.entity.AlgorithmTestDataStatistics;
import com.keymanager.ckadmin.service.AlgorithmTestResultStatisticsService;
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
@Service("algorithmTestResultStatisticsService2")
public class AlgorithmTestResultStatisticsServiceImpl extends
        ServiceImpl<AlgorithmTestResultStatisticsDao, AlgorithmTestDataStatistics> implements AlgorithmTestResultStatisticsService {

    @Autowired
    private AlgorithmTestResultStatisticsDao algorithmTestResultStatisticsDao;

    @Override
    public void saveAlgorithmTaskDataDailyTask(){
        algorithmTestResultStatisticsDao.saveAlgorithmTaskDataDaily();
    }

    @Override
    public Page<AlgorithmTestDataStatistics> selectAlgorithmTestResultStatisticsByAlgorithmTestPlanUuid(
            Page<AlgorithmTestDataStatistics> algorithmTestResultStatisticsPage, Long algorithmTestPlanUuid) {
        List<AlgorithmTestDataStatistics> algorithmTestResultStatistics = algorithmTestResultStatisticsDao.selectAlgorithmTestResultStatisticsByAlgorithmTestPlanUuid(algorithmTestResultStatisticsPage,algorithmTestPlanUuid);
        algorithmTestResultStatisticsPage.setRecords(algorithmTestResultStatistics);
        return algorithmTestResultStatisticsPage;
    }
}
