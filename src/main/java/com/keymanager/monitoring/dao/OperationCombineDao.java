package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.OperationCombineCriteria;
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
                               @Param("maxInvalidCount") int maxInvalidCount);

    long lastInsertID ();
}
