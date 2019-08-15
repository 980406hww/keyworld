package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.criteria.QZSettingSearchGroupInfoCriteria;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.vo.DateRangeTypeVO;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import com.keymanager.monitoring.vo.QZSettingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QZSettingDao extends BaseMapper<QZSetting> {

    List<DateRangeTypeVO> getChargeRemindData();

    List<QZSetting> getAvailableQZSettings();

    List<QZSetting> captureCurrentKeyword();

    List<QZSetting> searchQZSettings(Page<QZSetting> page, @Param("qzSettingSearchCriteria")QZSettingSearchCriteria qzSettingSearchCriteria);

    int selectLastId();

    void updateQZSettingGroup(@Param("qzSetting") QZSetting qzSetting);

    void updateQZSettingStatus(@Param("uuids") List<Long> uuids, @Param("status") Integer status);

    void updateImmediately(@Param("uuids") String uuids);

    void updateExceedMaxCountFlag(@Param("pcKeywordExceedMaxCount") boolean pcKeywordExceedMaxCount, @Param("phoneKeywordExceedMaxCount") boolean phoneKeywordExceedMaxCount);

    void updatePCExceedMaxCountFlag(@Param("pcKeywordExceedMaxCount") boolean pcKeywordExceedMaxCount, @Param("uuids") List<Long> uuids);

    void updatePhoneExceedMaxCountFlag(@Param("phoneKeywordExceedMaxCount") boolean phoneKeywordExceedMaxCount, @Param("uuids") List<Long> uuids);

    List<Long> getKeywordExceedMaxCount(@Param("operationType") String operationType);

    void updateQzSetting(@Param("qzSetting") QZSetting qzSetting);

    void updateCrawlerStatus(@Param("uuids")Long[] uuids);

    int getQZSettingGroupInfo (@Param("qzSettingSearchGroupInfoCriteria") QZSettingSearchGroupInfoCriteria qzSettingSearchGroupInfoCriteria);

    List<String> getAvailableOptimizationGroups (@Param("groupSettingCriteria") GroupSettingCriteria groupSettingCriteria);

    void startMonitorImmediately (@Param("uuids") String uuids);
    
    List<ExternalQzSettingVO> getQZSettingTask(@Param("crawlerHour")Integer crawlerHour, @Param("taskNumber")Integer taskNumber);

    List<QZSettingVO> searchQZSettingSearchEngines(@Param("customerUuid") String customerUuid, @Param("domain") String domain);

    void updateQZSettingRenewalStatus(@Param("uuids") List<Long> uuids, @Param("renewalStatus") Integer renewalStatus);

    List<QZSetting> searchAllQZSettingForGenerateRankingCurve();

    ExternalQzSettingVO selectQZSettingForAutoOperate();
}

