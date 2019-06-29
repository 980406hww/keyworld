package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.OperationCombine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/6/24 13:59
 **/
public interface OperationCombineDao extends BaseMapper<OperationCombine> {


    List<String> getOperationCombineNames (@Param("terminalType") String terminalType);

    void saveOperationCombine (@Param("operationCombineName") String operationCombineName,
                               @Param("terminalType") String terminalType,
                               @Param("creator") String creator,
                               @Param("maxInvalidCount") int maxInvalidCount,
                               @Param("remainingCount") int remainingCount);

    long lastInsertID ();

    void updateOperationCombineRemainingAccount (@Param("operationCombineUuid") long operationCombineUuid,
                                      @Param("remainingAccount") int remainingAccount);

    OperationCombine getOperationCombine (@Param("groupName") String groupName, @Param("terminalType") String terminalType);

    void updateMaxInvalidCount (@Param("uuid") long uuid, @Param("maxInvalidCount") int maxInvalidCount);

    String getOperationCombineName (@Param("optimizeGroupName") String optimizeGroupName);
}
