package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QZChargeLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj01 on 2017/7/17.
 */
public interface QZChargeLogDao extends BaseMapper<QZChargeLog> {

    List<QZChargeLog> chargesList(@Param("fQZSettingUuid") Long fQZSettingUuid);
}
