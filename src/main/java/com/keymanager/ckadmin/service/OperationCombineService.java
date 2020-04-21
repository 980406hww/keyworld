package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.OperationCombineCriteria;
import com.keymanager.ckadmin.criteria.UpdateGroupSettingCriteria;
import com.keymanager.ckadmin.entity.OperationCombine;
import com.keymanager.ckadmin.vo.OperationCombineVO;
import java.util.List;
import java.util.Map;

public interface OperationCombineService extends IService<OperationCombine> {

    public List<String> getGroupNames (long uuid);

    public List<OperationCombineVO> searchGroupsBelowOperationCombine (Long uuid, String groupName);

    public List<Map<String, Object>> getOperationCombineNames (String terminalType);

    public void deleteOperationCombine (long uuid);

    public void saveOperationCombine (OperationCombineCriteria operationCombineCriteria);

    public void updateOperationCombineRemainingAccount (Long operationCombineUuid, int remainingAccount);

    public void updateOperationCombine (long operationCombineUuid, UpdateGroupSettingCriteria updateGroupSettingCriteria);

    public void updateOperationCombineName(long uuid, String operationCombineName);

    public void updateOperationCombineUpdateTime (Long operationCombineUuid);

    public OperationCombine getOperationCombine (String groupName, String terminalType);

    public void updateMaxInvalidCount (long uuid, int maxInvalidCount);

    public String getOperationCombineName (String optimizeGroupName);

    public List<OperationCombine> searchOperationCombines(Page<OperationCombine> page, GroupSettingCriteria groupSettingCriteria);
    public List<OperationCombine> getCombinesUser(String terminal);
    void alterDefaultSearchEngine(Long uuid,int status);
}
