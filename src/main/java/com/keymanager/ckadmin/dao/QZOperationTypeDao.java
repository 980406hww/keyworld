package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.QZOperationType;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("qzOperationTypeDao2")
public interface QZOperationTypeDao extends BaseMapper<QZOperationType> {

    /**
     * 通过uuid查询操作类型表
     */
    List<QZOperationType> searchQZOperationTypesByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    /**
     * 查询多条操作类型信息
     */
    List<QZOperationType> searchQZOperationTypesIsDelete(@Param("qzSettingUuid") Long qzSettingUuid);

    /**
     * 删除
     */
    void deleteByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    /**
     * 上一级的主键
     */
    long selectLastId();

    void updateQZOperationTypeStandardTime(@Param("uuid") long uuid, @Param("isStandardFlag") int isStandardFlag);

    QZOperationType searchQZOperationType(@Param("qzSettingUuid") long qzSettingUuid, @Param("operationType") String operationType);

    void updateStandardTimeByUuid(@Param("uuid") Long uuid, @Param("updateFlag") int updateFlag, @Param("lastAchieve") int lastAchieve);

    Date getStandardTime(@Param("qzSettingUuid") long qzSettingUuid, @Param("terminalType") String terminalType);

    String getQZSettingStandardSpecie(@Param("qzSettingUuid") long qzSettingUuid, @Param("terminalType") String terminalType);

    /**
     * 获取站点达标类别和限制词数
     */
    String getQZStandardSpecieAndMaxKeywordCount(@Param("qzSettingUuid") long qzSettingUuid, @Param("terminalType") String terminalType);

    QZOperationType searchQZOperationTypeByQZSettingAndTerminalType(@Param("qzSettingUuid") long qzSettingUuid, @Param("operationType") String operationType);
}
