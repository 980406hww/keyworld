package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.Group;

public interface GroupService extends IService<Group> {

    void updateGroupOperationCombineUuid(Long operationCombineUuid, String groupName);
}
