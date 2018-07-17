package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.NegativeStandardSettingCriteria;
import com.keymanager.monitoring.entity.NegativeStandardSetting;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/6/28.
 */

public interface NegativeStandardSettingDao extends BaseMapper<NegativeStandardSetting> {

    List<NegativeStandardSetting> searchNegativeStandardSetting(Page<NegativeStandardSetting> page, @Param("negativeStandardSettingCriteria")NegativeStandardSettingCriteria negativeStandardSettingCriteria);

    int  findNegativeStandardSetting(@Param("customerUuid") Long customerUuid,@Param("keyword") String keyword,@Param("searchEngine") String searchEngine);

    List<NegativeStandardSetting> allNegativeStandardSetting(Page<NegativeStandardSetting> page, @Param("negativeStandardSettingCriteria")NegativeStandardSettingCriteria negativeStandardSettingCriteria);

    List<NegativeStandardSetting> getNegativeStandardSetting(@Param("userID") String userID);

    List<Map> findUsers();
}
