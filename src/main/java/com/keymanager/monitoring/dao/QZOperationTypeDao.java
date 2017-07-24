package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.entity.QZSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QZOperationTypeDao extends BaseMapper<QZOperationType> {

    //通过uuid查询操作类型表（）
    List<QZOperationType> searchQZOperationTypesByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    //通过uuid查询操作类型表
    List<QZOperationType> searchQZOperationTypesIsDelete(@Param("qzSettingUuid") Long qzSettingUuid);

    //删除
    void deleteByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    //上一级的主键
    int selectLastId();

    //过期缴费
    List<QZSetting> expiredCharge();
    //当天缴费
    List<QZSetting> nowCharge();
    //三天缴费
    List<QZSetting> threeCharge();
    //七天缴费
    List<QZSetting> sevenCharge();

}
