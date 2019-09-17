package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.QZChargeLog;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("qzChargeLogDao2")
public interface QZChargeLogDao extends BaseMapper<QZChargeLog> {

    List<QZChargeLog> chargesList(@Param("fQZSettingUuid") Long fQZSettingUuid);
}
