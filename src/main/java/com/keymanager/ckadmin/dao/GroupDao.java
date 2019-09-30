package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.criteria.GroupCriteria;
import com.keymanager.ckadmin.criteria.OperationCombineCriteria;
import com.keymanager.ckadmin.entity.Group;
import com.keymanager.ckadmin.vo.OperationCombineVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author zhoukai
 * @Date 2019/9/10 17:41
 **/
@Repository("groupDao2")
public interface GroupDao extends BaseMapper<Group> {

    Group findGroupByGroupName (@Param("groupName") String groupName);

    void deleteByGroupName(@Param("groupName") String groupName);

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

    void saveExternalGroup(@Param("groupCriteria") GroupCriteria groupCriteria);
}
