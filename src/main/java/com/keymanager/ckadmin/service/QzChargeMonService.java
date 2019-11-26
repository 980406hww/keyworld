package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.QZChargeMonCriteria;
import com.keymanager.ckadmin.entity.QzChargeMon;
import java.util.List;
import java.util.Map;

public interface QzChargeMonService extends IService<QzChargeMon> {

    Map<String, Object> getQZChargeMonData(String searchEngines, String terminal, String time, String loginName);

    List<QzChargeMon> getMonDateByCondition(Page<QzChargeMon> page, QZChargeMonCriteria criteria);

    void deleteByQZSettingUuid(Long qzSettingUuid);
}
