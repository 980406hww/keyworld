package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QzChargeMon;
import java.util.Map;

public interface QzChargeMonService extends IService<QzChargeMon> {

    Map<String, Object> selectBySe(String searchEngines, String terminal, Integer num, Integer type);
}
