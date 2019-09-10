package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.entity.OperationCombine;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author zhoukai
 * @Date 2019/6/24 13:59
 **/
@Repository("OperationCombineDao2")
public interface OperationCombineDao extends BaseMapper<OperationCombine> {

    List<String> getOperationCombineNames(@Param("terminalType") String terminalType);

    void saveOperationCombine(@Param("operationCombineName") String operationCombineName,
        @Param("terminalType") String terminalType,
        @Param("creator") String creator,
        @Param("maxInvalidCount") int maxInvalidCount,
        @Param("remainingCount") int remainingCount);

    long lastInsertID();

    void updateOperationCombineRemainingAccount(
        @Param("operationCombineUuid") long operationCombineUuid,
        @Param("remainingAccount") int remainingAccount);

    OperationCombine getOperationCombine(@Param("groupName") String groupName,
        @Param("terminalType") String terminalType);

    void updateMaxInvalidCount(@Param("uuid") long uuid,
        @Param("maxInvalidCount") int maxInvalidCount);

    /**
     * 获取操作组合
     * @param optimizeGroupName
     * @return
     */
    String getOperationCombineName(@Param("optimizeGroupName") String optimizeGroupName);

    List<OperationCombine> searchOperationCombines(@Param("page") Page<OperationCombine> page,
        @Param("groupSettingCriteria") GroupSettingCriteria groupSettingCriteria);

    void updateOperationCombineName(@Param("uuid") long uuid,
        @Param("operationCombineName") String operationCombineName);
}
