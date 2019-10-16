package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.AdvertisingCriteria;
import com.keymanager.ckadmin.entity.Advertising;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository(value = "advertisingDao2")
public interface AdvertisingDao extends BaseMapper<Advertising> {

    List<Advertising> searchAdvertisingListsPage(@Param("page") Page<Advertising> page, @Param("advertisingCriteria") AdvertisingCriteria advertisingCriteria);

    Advertising getAdvertising(@Param("uuid") Long uuid);

    List<String> searchAdvertisingIds(@Param("websiteUuid") Long websiteUuid, @Param("uuids") List<String> uuids);

    List<Map> searchIdByOriginalAdvertisingTagname(@Param("websiteUuid") int websiteUuid, @Param("originalAdvertisingTagname") String originalAdvertisingTagname);

    Advertising getAdvertisingByAdvertisingTagname(@Param("websiteUuid") int websiteUuid, @Param("advertisingTagname") String advertisingTagname);

    List<String> searchAdvertisingidsByAdvertisingTagname(@Param("websiteUuid") Long websiteUuid, @Param("advertisingTagname") String advertisingTagname);

    void batchDeleteAdvertisingByAdvertisingTagname(@Param("advertisingTagname") String advertisingTagname, @Param("websiteUuids") List<String> websiteUuids);

    List<Integer> selectByWebsiteId(@Param("websiteUuid") Long websiteUuid);

    Long selectIdByAdvertisingId(@Param("websiteUuid") Long websiteUuid, @Param("advertisingId") int advertisingId);

    int searchAdvertisingCount(@Param("websiteUuid") Long websiteUuid);
}
