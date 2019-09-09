package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.QZOperationType;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("qzOperationTypeDao2")
public interface QZOperationTypeDao extends BaseMapper<QZOperationType> {

    //通过uuid查询操作类型表
    List<QZOperationType> searchQZOperationTypesByQZSettingUuid(
        @Param("qzSettingUuid") Long qzSettingUuid);
}
