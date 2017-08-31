package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.vo.QZSettingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QZSettingDao extends BaseMapper<QZSetting> {
    List<QZSetting> getAvailableQZSettings();

    List<QZSetting> captureCurrentKeyword();

    List<QZSetting> searchQZSettingsByUuids(@Param("uuids") String uuids);

    List<QZSetting> searchQZSettings(Pagination page, @Param("uuid") Long uuid, @Param("customerUuid") Long customerUuid, @Param("domain") String
            domain, @Param("group") String group, @Param("updateStatus") String updateStatus);

    List<QZSetting> searchCustomerPage(Page<QZSetting> page, @Param("qzSettingVO")QZSettingVO qzSettingVO);

    int selectLastId();
}
