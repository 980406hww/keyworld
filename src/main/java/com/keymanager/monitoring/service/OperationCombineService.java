package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.utils.JsonUtils;
import com.keymanager.monitoring.criteria.OperationCombineCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.dao.OperationCombineDao;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.entity.OperationCombine;
import com.keymanager.monitoring.vo.OperationCombineVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public List<String> getGroupNames (long uuid) {
        return groupService.getGroupNames(uuid);
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

    public void updateOperationCombineRemainingAccount (Long operationCombineUuid, int remainingAccount) {
        operationCombineDao.updateOperationCombineRemainingAccount(operationCombineUuid, remainingAccount);
    }

    public void updateOperationCombine (long operationCombineUuid, UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        List<Long> groupSettingUuids = groupSettingService.getGroupSettingUuids(operationCombineUuid);
        if (CollectionUtils.isNotEmpty(groupSettingUuids)) {
            for (Long groupSettingUuid : groupSettingUuids) {
                updateGroupSettingCriteria.getGroupSetting().setUuid(groupSettingUuid);
                groupSettingService.updateGroupSetting(updateGroupSettingCriteria.getGs(), updateGroupSettingCriteria.getGroupSetting());
                GroupSetting groupSetting = groupSettingService.selectById(updateGroupSettingCriteria.getGroupSetting().getUuid());
                this.updateOperationCombineUpdateTime(groupSetting.getOperationCombineUuid());
            }

        }
        if (1 == updateGroupSettingCriteria.getGs().getMachineUsedPercent()) {
                this.updateOperationCombineRemainingAccount(updateGroupSettingCriteria.getGroupSetting().getOperationCombineUuid(), updateGroupSettingCriteria.getGroupSetting().getRemainingAccount());
        }
        if (1 == updateGroupSettingCriteria.getGs().getMaxInvalidCount()) {
            this.updateMaxInvalidCount(updateGroupSettingCriteria.getGroupSetting().getOperationCombineUuid(), updateGroupSettingCriteria.getGroupSetting().getMaxInvalidCount());
        }
    }

    public void updateOperationCombineUpdateTime (Long operationCombineUuid) {
        OperationCombine operationCombine = operationCombineDao.selectById(operationCombineUuid);
        operationCombine.setUpdateTime(new Date());
        operationCombineDao.updateById(operationCombine);
    }

    public OperationCombine getOperationCombine (String groupName, String terminalType) {
        return operationCombineDao.getOperationCombine(groupName, terminalType);
    }

    public void updateMaxInvalidCount (long uuid, int maxInvalidCount) {
        operationCombineDao.updateMaxInvalidCount(uuid, maxInvalidCount);
    }
}
