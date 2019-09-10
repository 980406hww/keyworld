package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.QZOperationType;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("QZOperationTypeDao2")
public interface QZOperationTypeDao extends BaseMapper<QZOperationType> {

    /**
     * 通过uuid查询操作类型表
     * @param qzSettingUuid
     * @return
     */
    List<QZOperationType> searchQZOperationTypesByQZSettingUuid(
        @Param("qzSettingUuid") Long qzSettingUuid);

    /**
     * 通过uuid查询操作类型表
     * @param qzSettingUuid
     * @return
     */
    List<QZOperationType> searchQZOperationTypesIsDelete(
        @Param("qzSettingUuid") Long qzSettingUuid);

    /**
     * 删除
     * @param qzSettingUuid
     */
    void deleteByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);

    /**
     * 上一级的主键
     * @return
     */
    int selectLastId();

    void updateQZOperationTypeStandardTime(@Param("uuid") long uuid,
        @Param("isStandardFlag") int isStandardFlag);

    QZOperationType searchQZOperationTypeByQZSettingAndTerminalType(
        @Param("qzSettingUuid") long qzSettingUuid, @Param("operationType") String operationType);

    void updateStandardTimeByUuid(@Param("uuid") Long uuid, @Param("updateFlag") int updateFlag,
        @Param("lastAchieve") int lastAchieve);

    List<String> getAllOperationChargeKeywordCountByQZSettingUuid(
        @Param("qzSettingUuid") Long qzSettingUuid);

    Date getStandardTime(@Param("qzSettingUuid") long qzSettingUuid,
        @Param("terminalType") String terminalType);
}
