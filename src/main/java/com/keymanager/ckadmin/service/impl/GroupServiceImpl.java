package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.GroupDao;
import com.keymanager.ckadmin.entity.Group;
import com.keymanager.ckadmin.service.GroupService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("groupService2")
public class GroupServiceImpl extends ServiceImpl<GroupDao, Group> implements GroupService {

    @Resource(name = "groupDao2")
    private GroupDao groupDao;

    @Override
    public void updateGroupOperationCombineUuid(Long operationCombineUuid,
        String groupName) {
        groupDao.updateGroupOperationCombineUuid(operationCombineUuid, groupName);
    }
}
