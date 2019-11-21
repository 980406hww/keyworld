package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.entity.AlgorithmTestDataStatistics;
import com.keymanager.ckadmin.vo.AlgorithmTestDataStatisticsVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * 算法执行统计信息表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-27
 */
@Repository("algorithmTestResultStatisticsDao2")
public interface AlgorithmTestResultStatisticsDao extends BaseMapper<AlgorithmTestDataStatistics> {

    void saveAlgorithmTaskDataDaily();

    List<AlgorithmTestDataStatistics> selectAlgorithmTestResultStatisticsByAlgorithmTestPlanUuid(Page<AlgorithmTestDataStatistics> algorithmTestResultStatisticsPage,
        @Param("algorithmTestPlanUuid") Long algorithmTestPlanUuid);

    List<AlgorithmTestDataStatistics> selectAlgorithmTestHistoryByAlgorithmTestPlanUuid(Page<AlgorithmTestDataStatisticsVo> algorithmTestResultStatisticsPage,
        @Param("algorithmTestPlanUuid") Long algorithmTestPlanUuid);
}
