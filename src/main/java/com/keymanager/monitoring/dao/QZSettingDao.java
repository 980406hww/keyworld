package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.vo.DateRangeTypeVO;
import com.keymanager.monitoring.vo.QZSettingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QZSettingDao extends BaseMapper<QZSetting> {

    List<DateRangeTypeVO> getChargeRemindData();

    List<QZSetting> getAvailableQZSettings();

    List<QZSetting> captureCurrentKeyword();

    List<QZSetting> searchQZSettingsByUuids(@Param("uuids") String uuids);

    List<QZSetting> searchQZSettings(Page<QZSetting> page, @Param("qzSettingSearchCriteria")QZSettingSearchCriteria qzSettingSearchCriteria);

    int selectLastId();

    void deleteQZSettingGroup(@Param("qzSetting") QZSetting qzSetting);

    void updateQZSettingStatus(@Param("uuids") List<Long> uuids, @Param("status") Integer status);

    void updateImmediately(@Param("uuids") String uuids);

    void updateExceedMaxCountFlag(@Param("pcKeywordExceedMaxCount") boolean pcKeywordExceedMaxCount, @Param("phoneKeywordExceedMaxCount") boolean phoneKeywordExceedMaxCount);

    void updatePCExceedMaxCountFlag(@Param("pcKeywordExceedMaxCount") boolean pcKeywordExceedMaxCount, @Param("uuids") List<Long> uuids);

    void updatePhoneExceedMaxCountFlag(@Param("phoneKeywordExceedMaxCount") boolean phoneKeywordExceedMaxCount, @Param("uuids") List<Long> uuids);

    List<Long> getPCKeywordExceedMaxCount();

    List<Long> getPhoneKeywordExceedMaxCount();
}

