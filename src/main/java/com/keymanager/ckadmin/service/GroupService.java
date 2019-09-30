package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.OperationCombineCriteria;
import com.keymanager.ckadmin.entity.Group;
import com.keymanager.ckadmin.vo.OperationCombineVO;
import java.util.List;

public interface GroupService extends IService<Group> {

    void updateGroupOperationCombineUuid(Long operationCombineUuid, List<String> groupName,
        String loginName);
    
    void deleteByGroupName(String groupName);

    void saveGroupsBelowOperationCombine (OperationCombineCriteria operationCombineCriteria);

    List<String> getGroupNames (long operationCombineUuid);

    List<OperationCombineVO> searchGroupsBelowOperationCombine (Long operationCombineUuid, String groupName);

    void updateGroupsBelowOperationCombine (List<Long> groupUuids, Long operationCombineUuid);
}
