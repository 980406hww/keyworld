package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QZOperationType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QZOperationTypeDao extends BaseMapper<QZOperationType> {

    //通过uuid查询操作类型表（）
    List<QZOperationType> searchQZOperationTypesByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    //通过uuid查询操作类型表
    List<QZOperationType> searchQZOperationTypesIsDelete(@Param("qzSettingUuid") Long qzSettingUuid);

    //删除
    void deleteByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    //上一级的主键
    int selectLastId();

    String getStandardType (Long qzSettingUuid, String operationType);

    void updateQZOperationTypeStandardTime (@Param("qzSettingUuid") Long qzSettingUuid, @Param("operationType") String operationType, @Param("isStandardFlag") boolean isStandardFlag);

    QZOperationType searchQZOperationTypeByQZSettingAndTerminalType(@Param("qzSettingUuid") Long qzSettingUuid, @Param("operationType") String operationType);
}
