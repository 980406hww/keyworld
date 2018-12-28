package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.vo.QZOperationTypeVO;
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

    List<QZOperationTypeVO> findQZOperationTypes(@Param("qzSettingUuid")String qzSettingUuid,@Param("operationType")String operationType,@Param("group")String group);
}
