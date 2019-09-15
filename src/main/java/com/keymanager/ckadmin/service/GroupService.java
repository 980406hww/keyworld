package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.Group;
import java.util.List;

public interface GroupService extends IService<Group> {

    void updateGroupOperationCombineUuid(Long operationCombineUuid, List<String> groupName,
        String loginName);
    
    void deleteByGroupName(String groupName);
}
