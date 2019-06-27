package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.OperationCombineCriteria;
import com.keymanager.monitoring.entity.Group;
import com.keymanager.monitoring.vo.GroupVO;
import com.keymanager.monitoring.vo.OperationCombineVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/4/27 9:41
 **/
public interface GroupDao extends BaseMapper<Group> {

    List<GroupVO> searchGroups (Page<GroupVO> page, @Param("groupSettingCriteria") GroupSettingCriteria groupSettingCriteria);

    List<String> getOptimizationGroups(@Param("terminalType") String terminalType);

    List<String> getGroupNames (@Param("operationCombineUuid") long operationCombineUuid);

    List<OperationCombineVO> searchGroupsBelowOperationCombine (@Param("operationCombineUuid")Long operationCombineUuid,
                                                                @Param("groupName") String groupName);

    void updateGroupOperationUuid (@Param("groupUuids") List<Long> groupUuids,
                                   @Param("operationCombineUuid")Long operationCombineUuid);

    Long searchExistingGroupUuid (@Param("terminalType") String terminalType, @Param("groupName") String groupName);

    void insertBatchGroups (@Param("operationCombineCriteria") OperationCombineCriteria operationCombineCriteria);

    void updateGroupOperationCombineUuid (@Param("operationCombineUuid")long operationCombineUuid);

    void updateOperationCombineUuidByGroupName (@Param("groupNames") List<String> groupNames, @Param("operationCombineUuid")long operationCombineUuid);
}
