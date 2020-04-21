package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.UpdateGroupSettingCriteria;
import com.keymanager.ckadmin.dao.GroupSettingDao;
import com.keymanager.ckadmin.entity.GroupSetting;
import com.keymanager.ckadmin.entity.OperationCombine;
import com.keymanager.ckadmin.service.GroupSettingService;
import com.keymanager.ckadmin.service.OperationCombineService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("groupSettingService2")
public class GroupSettingServiceImpl extends ServiceImpl<GroupSettingDao, GroupSetting> implements GroupSettingService {

    @Resource(name = "groupSettingDao2")
    private GroupSettingDao groupSettingDao;

    @Resource(name = "operationCombineService2")
    private OperationCombineService operationCombineService;

    @Override
    public Page<OperationCombine> searchGroupSettings(Page<OperationCombine> page, GroupSettingCriteria groupSettingCriteria) {
        page.setRecords(operationCombineService.searchOperationCombines(page, groupSettingCriteria));
        for (OperationCombine operationCombine : page.getRecords()) {
            List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettings(operationCombine.getUuid());
            operationCombine.setGroupSettings(groupSettings);
        }
        return page;
    }

    @Override
    public void deleteGroupSetting (long uuid) {
        GroupSetting groupSetting = groupSettingDao.selectById(uuid);
        OperationCombine operationCombine = operationCombineService.selectById(groupSetting.getOperationCombineUuid());
        operationCombine.setRemainingAccount(operationCombine.getRemainingAccount() + groupSetting.getMachineUsedPercent());
        operationCombineService.updateById(operationCombine);
        groupSettingDao.deleteById(uuid);
    }

    /**
     * 根据终端类型来获取搜索引擎类型
     * @param terminalType
     * @return
     */
    public List<String> groupSettingSearchEngine(String terminalType){
        String searchEngineStr=groupSettingDao.getGroupSettingSearchEngine(terminalType);
        String[] searchEngines=searchEngineStr.split(",");
        List<String> lst=new ArrayList<>(Arrays.asList(searchEngines));
        return lst;

    }


    @Override
    public GroupSetting findGroupSetting (long uuid) {
        return groupSettingDao.selectById(uuid);
    }

    @Override
    public void saveGroupSetting (GroupSetting groupSetting) {
        operationCombineService.updateOperationCombineRemainingAccount(groupSetting.getOperationCombineUuid(), groupSetting.getRemainingAccount());
        groupSettingDao.saveGroupSetting(groupSetting);
    }

    @Override
    public void updateGroupSetting (UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        groupSettingDao.updateGroupSetting(updateGroupSettingCriteria.getGs(), updateGroupSettingCriteria.getGroupSetting());
        GroupSetting groupSetting = groupSettingDao.selectById(updateGroupSettingCriteria.getGroupSetting().getUuid());
        operationCombineService.updateOperationCombineUpdateTime(groupSetting.getOperationCombineUuid());
        if (1 == updateGroupSettingCriteria.getGs().getMachineUsedPercent()) {
            operationCombineService.updateOperationCombineRemainingAccount(updateGroupSettingCriteria.getGroupSetting().getOperationCombineUuid(), updateGroupSettingCriteria.getGroupSetting().getRemainingAccount());
        }
        if ("1".equals(updateGroupSettingCriteria.getGs().getOperationCombineName())) {
            operationCombineService.updateOperationCombineName(groupSetting.getOperationCombineUuid(), updateGroupSettingCriteria.getGroupSetting().getOperationCombineName());
        }
    }

    @Override
    public GroupSetting getGroupSettingViaPercentage(String groupName, String terminalType){
        OperationCombine operationCombine = operationCombineService.getOperationCombine(groupName, terminalType);
        if(operationCombine != null) {
            return getGroupSetting(operationCombine);
        }
        return null;
    }

    @Override
    public GroupSetting getGroupSetting(OperationCombine operationCombine) {
        List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettingsSortingPercentage(operationCombine.getUuid());
        if(CollectionUtils.isNotEmpty(groupSettings)){
            Random ra = new Random();
            if(operationCombine.getRemainingAccount() < 100) {
                int randomValue = ra.nextInt(100 - operationCombine.getRemainingAccount()) + 1;
                int totalPercentage = 0;
                for (GroupSetting groupSetting : groupSettings) {
                    totalPercentage = totalPercentage + groupSetting.getMachineUsedPercent();
                    if (randomValue <= totalPercentage) {
                        return groupSetting;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Long> getGroupSettingUuids (long operationCombineUuid) {
        return groupSettingDao.getGroupSettingUuids(operationCombineUuid);
    }

    @Override
    public void updateGroupSetting(GroupSetting gs, GroupSetting groupSetting){
        groupSettingDao.updateGroupSetting(gs, groupSetting);
    }

    @Override
    public void deleteGroupSettingByOperationCombineUuid (long operationCombineUuid) {
        groupSettingDao.deleteGroupSettingByOperationCombineUuid(operationCombineUuid);
    }

    @Override
    public GroupSetting getGroupSettingByUuid(Long uuid) {
        return groupSettingDao.getGroupSettingByUuid(uuid);
    }
}
