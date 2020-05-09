package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.OperationCombineCriteria;
import com.keymanager.ckadmin.criteria.UpdateGroupSettingCriteria;
import com.keymanager.ckadmin.dao.OperationCombineDao;
import com.keymanager.ckadmin.entity.GroupSetting;
import com.keymanager.ckadmin.entity.OperationCombine;
import com.keymanager.ckadmin.service.GroupService;
import com.keymanager.ckadmin.service.GroupSettingService;
import com.keymanager.ckadmin.service.OperationCombineService;
import com.keymanager.ckadmin.vo.OperationCombineVO;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("operationCombineService2")
public class OperationCombineServiceImpl extends
    ServiceImpl<OperationCombineDao, OperationCombine> implements OperationCombineService {

    @Resource(name = "operationCombineDao2")
    private OperationCombineDao operationCombineDao;

    @Resource(name = "groupService2")
    private GroupService groupService;

    @Resource(name = "groupSettingService2")
    private GroupSettingService groupSettingService;

    @Override
    public List<String> getGroupNames (long uuid) {
        return groupService.getGroupNames(uuid);
    }

    @Override
    public List<OperationCombineVO> searchGroupsBelowOperationCombine (Long uuid, String groupName) {
        return groupService.searchGroupsBelowOperationCombine(uuid, groupName);
    }

    @Override
    public List<Map<String, Object>> getOperationCombineNames (String terminalType) {
        return operationCombineDao.getOperationCombineNames(terminalType);
    }

    @Override
    public void deleteOperationCombine (long uuid) {
        operationCombineDao.deleteById(uuid);
        groupSettingService.deleteGroupSettingByOperationCombineUuid(uuid);
    }

    @Override
    public void saveOperationCombine (OperationCombineCriteria operationCombineCriteria) {
        operationCombineDao.saveOperationCombine(operationCombineCriteria.getOperationCombineName(), operationCombineCriteria.getTerminalType(),
                operationCombineCriteria.getCreator(), operationCombineCriteria.getMaxInvalidCount(), operationCombineCriteria.getRemainingAccount(),operationCombineCriteria.getSearchEngine(),0);
        long lastInsertID = operationCombineDao.lastInsertID();
        operationCombineCriteria.setOperationCombineUuid(lastInsertID);
        groupService.saveGroupsBelowOperationCombine(operationCombineCriteria);
        operationCombineCriteria.getGroupSetting().setOperationCombineUuid(lastInsertID);
        groupSettingService.saveGroupSetting(operationCombineCriteria.getGroupSetting());
    }

    @Override
    public void updateOperationCombineRemainingAccount (Long operationCombineUuid, int remainingAccount) {
        operationCombineDao.updateOperationCombineRemainingAccount(operationCombineUuid, remainingAccount);
    }

    @Override
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
        if ("1".equals(updateGroupSettingCriteria.getGs().getOperationCombineName())) {
            updateOperationCombineName(operationCombineUuid, updateGroupSettingCriteria.getGroupSetting().getOperationCombineName());
        }
    }

    @Override
    public void updateOperationCombineName(long uuid, String operationCombineName) {
        operationCombineDao.updateOperationCombineName(uuid, operationCombineName);
    }

    @Override
    public void updateOperationCombineUpdateTime (Long operationCombineUuid) {
        OperationCombine operationCombine = operationCombineDao.selectById(operationCombineUuid);
        operationCombine.setUpdateTime(new Date());
        operationCombineDao.updateById(operationCombine);
    }

    @Override
    public OperationCombine getOperationCombine (String groupName, String terminalType) {
        return operationCombineDao.getOperationCombine(groupName, terminalType);
    }

    @Override
    public void updateMaxInvalidCount (long uuid, int maxInvalidCount) {
        operationCombineDao.updateMaxInvalidCount(uuid, maxInvalidCount);
    }

    @Override
    public String getOperationCombineName (String optimizeGroupName) {
        return operationCombineDao.getOperationCombineName(optimizeGroupName);
    }

    @Override
    public List<OperationCombine> searchOperationCombines(Page<OperationCombine> page, GroupSettingCriteria groupSettingCriteria) {
        return operationCombineDao.searchOperationCombines(page, groupSettingCriteria);
    }

    @Override
    public List<Map<String ,Object>> getCombinesUser(String terminal) {
        return operationCombineDao.getUserName(terminal);
    }

    @Override
    public void updateSearchEngine(OperationCombine oc) {
        if(oc.getEngineDefault() == 1){
            operationCombineDao.resetEngineDefault(oc.getTerminalType(),oc.getSearchEngine());
        }
        operationCombineDao.updateSearchEngine(oc);
    }

    @Override
    public OperationCombine getOperationCombineForSearchEngineDefaule(String searchEngine, String terminal){
        return operationCombineDao.getOperationCombineForSearchEngineDefaule(searchEngine, terminal);
    }
}
