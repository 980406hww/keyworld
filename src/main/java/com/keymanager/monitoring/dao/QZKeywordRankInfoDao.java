package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/6 11:18
 **/
public interface QZKeywordRankInfoDao extends BaseMapper<QZKeywordRankInfo> {

    List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (@Param("qzSettingUuid") Long qzSettingUuid, @Param("qzSettingSearchCriteria")QZSettingSearchCriteria qzSettingSearchCriteria);

    void deleteByQZSettingUuid (@Param("qzSettingUuid") Long qzSettingUuid);
    
    List<ExternalQzSettingVO> getQZSettingTask(@Param("crawlerHour")Integer crawlerHour,@Param("taskNumber")Integer taskNumber);
    
    QZKeywordRankInfo getQZKeywordRankInfo(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType, @Param("websiteType") String websiteType);

    List<Long> getQzKeywordRankInfos(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType);

    QZSettingSearchCriteria getCountNumOfRankInfo(@Param("upper")double upper, @Param("lower")double lower, @Param("differenceValue") double differenceValue, @Param("terminalType") String terminalType, @Param("loginName") String loginName);

    List<String> getQZKeywordRankInfoTypes (@Param("qzSettingUuid") Long qzSettingUuid);
}
