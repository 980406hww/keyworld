package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.entity.OperationCombine;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author zhoukai
 * @Date 2019/6/24 13:59
 **/
@Repository("operationCombineDao2")
public interface OperationCombineDao extends BaseMapper<OperationCombine> {

    List<Map<String, Object>> getOperationCombineNames(@Param("terminalType") String terminalType);

    void saveOperationCombine(@Param("operationCombineName") String operationCombineName,
        @Param("terminalType") String terminalType,
        @Param("creator") String creator,
        @Param("maxInvalidCount") int maxInvalidCount,
        @Param("remainingCount") int remainingCount, @Param("searchEngine") String searchEngine,@Param("engineDefault") int engineDefault);

    long lastInsertID();

    void updateOperationCombineRemainingAccount(@Param("operationCombineUuid") long operationCombineUuid, @Param("remainingAccount") int remainingAccount);

    OperationCombine getOperationCombine(@Param("groupName") String groupName, @Param("terminalType") String terminalType);

    void updateMaxInvalidCount(@Param("uuid") long uuid, @Param("maxInvalidCount") int maxInvalidCount);

    /**
     * 获取操作组合
     */
    String getOperationCombineName(@Param("optimizeGroupName") String optimizeGroupName);

    List<OperationCombine> searchOperationCombines(Page<OperationCombine> page, @Param("groupSettingCriteria") GroupSettingCriteria groupSettingCriteria);

    void updateOperationCombineName(@Param("uuid") long uuid, @Param("operationCombineName") String operationCombineName);

    /**
     * 获取用户名以及id
     */
    List<Map<String, Object>> getUserName(@Param("terminal") String terminal);

    /**
     * 更改是否为默认操作组
     */
    void updateSearchEngine(@Param("oc") OperationCombine oc);

    /**
     * 重新设置terminalType，
     */
    void resetEngineDefault(@Param("terminalType") String terminalType, @Param("searchEngine") String searchEngine);

    /**
     * 获取默认操作组合
     */
    OperationCombine getOperationCombineForSearchEngineDefault(@Param("searchEngine") String searchEngine, @Param("terminal") String terminal);
}
