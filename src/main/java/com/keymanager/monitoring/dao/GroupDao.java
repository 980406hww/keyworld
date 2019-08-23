package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.GroupCriteria;
import com.keymanager.monitoring.criteria.OperationCombineCriteria;
import com.keymanager.monitoring.entity.Group;
import com.keymanager.monitoring.vo.OperationCombineVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/4/27 9:41
 **/
public interface GroupDao extends BaseMapper<Group> {

    List<String> getOptimizationGroups(@Param("terminalType") String terminalType);

    List<String> getGroupNames (@Param("operationCombineUuid") long operationCombineUuid);

    List<OperationCombineVO> searchGroupsBelowOperationCombine (@Param("operationCombineUuid")Long operationCombineUuid,
                                                                @Param("groupName") String groupName);

    void updateGroupOperationUuid (@Param("groupUuids") List<Long> groupUuids,
                                   @Param("operationCombineUuid")Long operationCombineUuid);

    Long searchExistingGroupUuid (@Param("terminalType") String terminalType, @Param("groupName") String groupName);

    void insertBatchGroups (@Param("operationCombineCriteria") OperationCombineCriteria operationCombineCriteria);

    void deleteGroupByGroupName (@Param("operationCombineUuid")long operationCombineUuid, @Param("groupNames") List<String> groupNames);

    void updateQZSettingGroupOperationCombineUuid (@Param("operationCombineUuid") Long operationCombineUuid,
                                                   @Param("groupName") String groupName);

    Group findExistingGroup (@Param("optimizeGroupName") String optimizeGroupName);

    void saveExternalGroup(@Param("groupCriteria")GroupCriteria groupCriteria);
}
