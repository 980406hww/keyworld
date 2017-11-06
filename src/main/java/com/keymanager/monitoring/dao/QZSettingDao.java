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
}
