package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.QZSettingCountNumCriteria;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/6 11:18
 **/
public interface QZKeywordRankInfoDao extends BaseMapper<QZKeywordRankInfo> {

    List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (@Param("qzSettingUuid") Long qzSettingUuid,
                                                             @Param("terminalType")String terminalType,
                                                             @Param("websiteType")String websiteType);

    void deleteByQZSettingUuid (@Param("qzSettingUuid") Long qzSettingUuid);

    QZKeywordRankInfo getQZKeywordRankInfo(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType, @Param("websiteType") String websiteType);

    QZSettingCountNumCriteria getCountNumOfRankInfo(@Param("lowerValue") double lowerValue,
                                                    @Param("upperValue") double upperValue,
                                                    @Param("differenceValue") double differenceValue,
                                                    @Param("downOneWeekDiff") int downOneWeekDiff,
                                                    @Param("upperOneWeekDiff") int upperOneWeekDiff,
                                                    @Param("criteria") QZSettingSearchCriteria criteria);

    List<String> getQZKeywordRankInfoTypes (@Param("qzSettingUuid") Long qzSettingUuid);

    QZKeywordRankInfo selectByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType);

    List<QZKeywordRankInfo> searchExistingExtraQZKeywordRankInfo (@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType")String terminalType);
}
