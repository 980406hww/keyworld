package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.criteria.QZRateStatisticsCountCriteria;
import com.keymanager.ckadmin.entity.QZRateStatistics;
import com.keymanager.ckadmin.vo.QZRateStatisticsCountVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-10-22
 */
@Repository("qzRateStatisticsDao2")
public interface QZRateStatisticsDao extends BaseMapper<QZRateStatistics> {

    void insertOrUpdateQZRateStatistics(@Param("qzRateStatisticsList") List<QZRateStatistics> qzRateStatisticsList);

    List<QZRateStatisticsCountVO> getQZRateStatisticCount(@Param("qzRateStatisticsCountCriteria") QZRateStatisticsCountCriteria qzRateStatisticsCountCriteria);

    Integer getRate(@Param("qzUuid") Long qzUuid, @Param("terminalType") String terminalType, @Param("rateFullDate") String rateFullDate);

    Map getQzRateHistory(@Param("qzUuid") Long qzUuid, @Param("terminalType") String terminalType);
}
