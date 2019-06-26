package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.utils.JsonUtils;
import com.keymanager.monitoring.criteria.OperationCombineCriteria;
import com.keymanager.monitoring.dao.OperationCombineDao;
import com.keymanager.monitoring.entity.OperationCombine;
import com.keymanager.monitoring.vo.OperationCombineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author zhoukai
 * @Date 2019/6/24 14:08
 **/
@Service
public class OperationCombineService extends ServiceImpl<OperationCombineDao, OperationCombine> {

    @Autowired
    private OperationCombineDao operationCombineDao;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupSettingService groupSettingService;

    public String getGroupNames (Long uuid) {
        List<String> groupNameList =  groupService.getGroupNames(uuid);
        return JsonUtils.toJson(groupNameList);
    }

    public List<OperationCombineVO> searchGroupsBelowOperationCombine (Long uuid, String groupName) {
        return groupService.searchGroupsBelowOperationCombine(uuid, groupName);
    }

    public List<String> getOperationCombineNames (String terminalType) {
        return operationCombineDao.getOperationCombineNames(terminalType);
    }

    public void deleteOperationCombine (long uuid) {
        operationCombineDao.deleteById(uuid);
        groupService.updateGroupOperationCombineUuid(uuid);
        groupSettingService.deleteGroupSettingByOperationCombineUuid(uuid);
    }

    public void saveOperationCombine (OperationCombineCriteria operationCombineCriteria) {
        operationCombineDao.saveOperationCombine(operationCombineCriteria.getOperationCombineName(), operationCombineCriteria.getTerminalType(),
                operationCombineCriteria.getCreator(), operationCombineCriteria.getMaxInvalidCount());
        long lastInsertID = operationCombineDao.lastInsertID();
        groupService.saveGroupsBelowOperationCombine(operationCombineCriteria);
        operationCombineCriteria.getGroupSetting().setOperationCombineUuid(lastInsertID);
        groupSettingService.saveGroupSetting(operationCombineCriteria.getGroupSetting(), false);
    }
}
