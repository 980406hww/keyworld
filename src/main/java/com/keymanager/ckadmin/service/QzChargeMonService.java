package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QzChargeMon;
import java.util.Map;

public interface QzChargeMonService extends IService<QzChargeMon> {

    Map<String, Object> getQZChargeMonData(String searchEngines, String terminal, String time);
}
