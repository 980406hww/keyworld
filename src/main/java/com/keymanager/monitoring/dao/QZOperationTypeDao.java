package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QZOperationType;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface QZOperationTypeDao extends BaseMapper<QZOperationType> {

    //通过uuid查询操作类型表
    List<QZOperationType> searchQZOperationTypesByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    //通过uuid查询操作类型表
    List<QZOperationType> searchQZOperationTypesIsDelete(@Param("qzSettingUuid") Long qzSettingUuid);

    //删除
    void deleteByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    //上一级的主键
    int selectLastId();

    void updateQZOperationTypeStandardTime (@Param("uuid") long uuid, @Param("isStandardFlag") int isStandardFlag);

    QZOperationType searchQZOperationTypeByQZSettingAndTerminalType(@Param("qzSettingUuid") long qzSettingUuid, @Param("operationType") String operationType);

    void updateStandardTimeByUuid(@Param("uuid") Long uuid, @Param("updateFlag") int updateFlag, @Param("lastAchieve") int lastAchieve);

    List<String> getQZSettngStandardSpecies(@Param("qzSettingUuid") Long qzSettingUuid);

    Date getStandardTime(@Param("qzSettingUuid") long qzSettingUuid, @Param("terminalType") String terminalType);
}
