package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.QZSettingCountNumCriteria;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import com.keymanager.monitoring.vo.QZKeywordRankForSync;
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

    /**
     * 百度同步操作词曲线，非百度同步指定词曲线（2019-12-06） 若操作词曲线的跨度大于指定词时，都使用操作词曲线
     */
    List<QZKeywordRankForSync> getQZKeywordRankInfoByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid, @Param("searchEngine") String searchEngine, @Param("limitRow") int limitRow);

    /**
     * replace 站点曲线数据
     */
    void replaceQZKeywordRanks(@Param("qzKeywordRanks") List<QZKeywordRankForSync> qzKeywordRanks);

    /**
     * 清空 sys_qz_keyword_rank 表的数据
     */
    void deleteSysQzKeywordRanks();
}
