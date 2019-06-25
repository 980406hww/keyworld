package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.utils.JsonUtils;
import com.keymanager.monitoring.dao.OperationCombineDao;
import com.keymanager.monitoring.entity.OperationCombine;
import com.keymanager.monitoring.vo.OperationCombineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author zhoukai
 * @Date 2019/6/24 14:08
 **/
@Service
public class OperationCombineService extends ServiceImpl<OperationCombineDao, OperationCombine> {

    private static final Logger logger = LoggerFactory.getLogger(OperationCombineService.class);

    @Autowired
    private OperationCombineDao operationCombineDao;

    @Autowired
    private GroupService groupService;

    public String getGroupNames (Long uuid) {
        List<String> groupNameList =  groupService.getGroupNames(uuid);
        return JsonUtils.toJson(groupNameList);
    }

    public List<OperationCombineVO> searchGroupsBelowOperationCombine (Long uuid, String groupName) {
        return groupService.searchGroupsBelowOperationCombine(uuid, groupName);
    }
}
