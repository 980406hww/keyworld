package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.GroupDao;
import com.keymanager.ckadmin.entity.Group;
import com.keymanager.ckadmin.service.GroupService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("groupService2")
public class GroupServiceImpl extends ServiceImpl<GroupDao, Group> implements GroupService {

    @Resource(name = "groupDao2")
    private GroupDao groupDao;

    @Override
    public void updateGroupOperationCombineUuid(Long operationCombineUuid, List<String> groupNames,
        String loginName) {
        for (String groupName : groupNames) {
            Group group = groupDao.findGroupByGroupName(groupName);
            if (null == group) {
                group = new Group();
                group.setGroupName(groupName);
                group.setOperationCombineUuid(operationCombineUuid);
                group.setCreateBy(loginName);
                groupDao.insert(group);
            } else {
                group.setOperationCombineUuid(operationCombineUuid);
                group.setUpdateTime(new Date());
                groupDao.updateById(group);
            }
        }
    }
}
