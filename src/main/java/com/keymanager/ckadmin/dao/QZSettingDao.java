package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;

import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.vo.QZSettingVO;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("qzSettingDao2")
public interface QZSettingDao extends BaseMapper<QZSetting> {

    List<QZSetting> searchQZSettings(Page<QZSetting> page,
        @Param("qzSettingCriteria") QZSettingCriteria qzSettingCriteria);

    List<QZSettingVO> searchQZSettingSearchEngines(@Param("customerUuid") String customerUuid,
        @Param("domain") String domain);

    int getQZSettingGroupInfo(@Param("terminalType") String terminalType,
        @Param("optimizeGroupName") String optimizeGroupName);

    int selectLastId();

    void updateQZSettingGroup(@Param("qzSetting") QZSetting qzSetting);

    void batchUpdateQZSettingUpdateStatus(@Param("uuids") String uuids);

    void batchUpdateRenewalStatus(@Param("uuids") String uuids,
        @Param("renewalStatus") int renewalStatus);

    Map<String, String> getPCPhoneGroupByUuid(@Param("uuid") Long uuid);
}

