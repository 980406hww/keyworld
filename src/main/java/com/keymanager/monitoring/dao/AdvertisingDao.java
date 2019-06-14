package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.AdvertisingCriteria;
import com.keymanager.monitoring.entity.Advertising;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdvertisingDao extends BaseMapper<Advertising> {

    List<Advertising> searchAdvertisingListsPage(Page<Advertising> page, @Param("advertisingCriteria") AdvertisingCriteria advertisingCriteria);

    Advertising getAdvertising(@Param("uuid") Long uuid);

    List<String> searchAdvertisingIds(@Param("websiteUuid") Long websiteUuid, @Param("uuids") List<String> uuids);

    List<Map> searchIdByOriginalAdvertisingTagname(@Param("websiteUuid") int websiteUuid, @Param("originalAdvertisingTagname")  String originalAdvertisingTagname);

    Advertising getAdvertisingByAdvertisingTagname(@Param("websiteUuid") int websiteUuid, @Param("advertisingTagname") String advertisingTagname);

    List<String> searchAdvertisingidsByAdvertisingTagname(@Param("websiteUuid") Long websiteUuid, @Param("advertisingTagname") String advertisingTagname);

    void batchDeleteAdvertisingByAdvertisingTagname(@Param("advertisingTagname")  String advertisingTagname, @Param("websiteUuids") List<String> websiteUuids);

    List<Integer> selectByWebsiteId(@Param("websiteUuid") Long websiteUuid);

    Long selectIdByAdvertisingId(@Param("websiteUuid") Long websiteUuid, @Param("advertisingId") int advertisingId);
}
