package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/6 11:18
 **/
public interface QZKeywordRankInfoDao extends BaseMapper<QZKeywordRankInfo> {

    List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (@Param("qzSettingUuid") Long qzSettingUuid, @Param("qzSettingSearchCriteria")QZSettingSearchCriteria qzSettingSearchCriteria);

    void deleteByQZSettingUuid (@Param("qzSettingUuid") Long qzSettingUuid);

    QZKeywordRankInfo getQZKeywordRankInfo(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType, @Param("websiteType") String websiteType);

    QZSettingSearchCriteria getCountNumOfRankInfo(@Param("upper")double upper, @Param("lower")double lower, @Param("differenceValue") double differenceValue, @Param("terminalType") String terminalType, @Param("loginName") String loginName);

    List<String> getQZKeywordRankInfoTypes (@Param("qzSettingUuid") Long qzSettingUuid);

    QZKeywordRankInfo selectByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType);

    int standardCountByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType);
}
