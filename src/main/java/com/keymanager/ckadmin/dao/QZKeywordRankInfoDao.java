package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.criteria.QZSettingCountNumCriteria;
import com.keymanager.ckadmin.criteria.QZSettingSearchCriteria;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author zhoukai
 * @Date 2018/12/6 11:18
 **/
@Repository("qzKeywordRankInfoDao2")
public interface QZKeywordRankInfoDao extends BaseMapper<QZKeywordRankInfo> {

    List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType, @Param("websiteType") String websiteType);

    void deleteByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    QZKeywordRankInfo getQZKeywordRankInfo(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType, @Param("websiteType") String websiteType);

    QZSettingCountNumCriteria getCountNumOfRankInfo(@Param("lowerValue") double lowerValue, @Param("upperValue") double upperValue,
        @Param("differenceValue") double differenceValue,
        @Param("downOneWeekDiff") int downOneWeekDiff,
        @Param("upperOneWeekDiff") int upperOneWeekDiff,
        @Param("criteria") QZSettingSearchCriteria criteria);

    List<String> getQZKeywordRankInfoTypes(@Param("qzSettingUuid") Long qzSettingUuid);

    QZKeywordRankInfo selectByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType);

    List<QZKeywordRankInfo> searchExistingExtraQZKeywordRankInfo(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType);
}
