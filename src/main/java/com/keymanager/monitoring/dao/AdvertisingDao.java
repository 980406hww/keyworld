package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.AdvertisingCriteria;
import com.keymanager.monitoring.entity.Advertising;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdvertisingDao extends BaseMapper<Advertising> {

    List<Advertising> searchAdvertisingListsPage(Page<Advertising> page, @Param("advertisingCriteria") AdvertisingCriteria advertisingCriteria);

    Advertising getAdvertising(@Param("uuid") Long uuid);

    List<String> searchAdvertisingIds(@Param("websiteUuid") Long websiteUuid, @Param("uuids") List<String> uuids);
}
