package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.AlgorithmTestResultStatisticsDao;
import com.keymanager.ckadmin.dao.AlgorithmTestTaskDao;
import com.keymanager.ckadmin.entity.AlgorithmTestDataStatistics;
import com.keymanager.ckadmin.entity.AlgorithmTestTask;
import com.keymanager.ckadmin.service.AlgorithmTestResultStatisticsService;
import com.keymanager.ckadmin.vo.AlgorithmTestDataStatisticsVo;
import java.util.ArrayList;
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

    @Autowired
    private AlgorithmTestTaskDao algorithmTestTaskDao;

    @Override
    public void saveAlgorithmTaskDataDailyTask(){
        algorithmTestResultStatisticsDao.saveAlgorithmTaskDataDaily();
    }

    @Override
    public Page<AlgorithmTestDataStatistics> selectAlgorithmTestResultStatisticsByAlgorithmTestPlanUuid(
            Page<AlgorithmTestDataStatistics> algorithmTestResultStatisticsPage, Long algorithmTestPlanUuid) {
        List<AlgorithmTestDataStatistics> algorithmTestResultStatistics = algorithmTestResultStatisticsDao
                .selectAlgorithmTestResultStatisticsByAlgorithmTestPlanUuid(algorithmTestResultStatisticsPage, algorithmTestPlanUuid);
        algorithmTestResultStatisticsPage.setRecords(algorithmTestResultStatistics);
        return algorithmTestResultStatisticsPage;
    }

    @Override
    public Page<AlgorithmTestDataStatisticsVo> selectAlgorithmTestHistoryByAlgorithmTestPlanUuid(
            Page<AlgorithmTestDataStatisticsVo> algorithmTestResultStatisticsPage, Long algorithmTestPlanUuid) {

        List<AlgorithmTestDataStatistics> algorithmTestResultStatistics = algorithmTestResultStatisticsDao
                .selectAlgorithmTestHistoryByAlgorithmTestPlanUuid(algorithmTestResultStatisticsPage, algorithmTestPlanUuid);

        List<AlgorithmTestDataStatisticsVo> algorithmTestDataStatisticsVos = new ArrayList<>();
        AlgorithmTestDataStatisticsVo algorithmTestDataStatisticsVo = new AlgorithmTestDataStatisticsVo();
        for (AlgorithmTestDataStatistics temp : algorithmTestResultStatistics) {
            String testPlanUuid = temp.getAlgorithmTestPlanUuid();
            AlgorithmTestTask algorithmTestTask = algorithmTestTaskDao.selectByAlgorithmTestPlanUuid(testPlanUuid);
            algorithmTestDataStatisticsVo.setActualKeywordCount(algorithmTestTask.getActualKeywordCount());
            algorithmTestDataStatisticsVo.setCustomerName(algorithmTestTask.getCustomerName());
            algorithmTestDataStatisticsVo.setKeywordGroup(algorithmTestTask.getKeywordGroup());
            algorithmTestDataStatisticsVo.setContactPerson(temp.getContactPerson());
            algorithmTestDataStatisticsVo.setTopTenCount(temp.getTopTenCount());
            algorithmTestDataStatisticsVo.setZeroOptimizedCount(temp.getZeroOptimizedCount());
            algorithmTestDataStatisticsVo.setCreateTime(temp.getCreateTime());
            algorithmTestDataStatisticsVo.setRankChangeRate(temp.getRankChangeRate());
            algorithmTestDataStatisticsVo.setUpdateTime(temp.getUpdateTime());
            algorithmTestDataStatisticsVo.setRecordDate(temp.getRecordDate());
            algorithmTestDataStatisticsVos.add(algorithmTestDataStatisticsVo);
        }
        algorithmTestResultStatisticsPage.setRecords(algorithmTestDataStatisticsVos);
        return algorithmTestResultStatisticsPage;
    }
}
