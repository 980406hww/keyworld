package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.shiro.ShiroUser;
import com.keymanager.monitoring.dao.OperationTypeDao;
import com.keymanager.monitoring.entity.OperationType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author wjianwu 2019/4/22 17:17
 */
@Service
public class OperationTypeService extends ServiceImpl<OperationTypeDao, OperationType> {

    private static Logger logger = LoggerFactory.getLogger(OperationTypeService.class);

    @Autowired
    private OperationTypeDao operationTypeDao;

    public List<OperationType> getOperationTypes(OperationType operationType, Page<OperationType> page) {
        return operationTypeDao.searchOperationTypeListsPage(page, operationType);
    }

    public OperationType getOperationType(Long uuid) {
        return operationTypeDao.selectById(uuid);
    }

    @CacheEvict(value = "operationTypeList", allEntries = true)
    public Boolean deleteOperationType(Map map) {
        if (map.get("deleteType") != null) {
            // 删除单个
            operationTypeDao.deleteById(Long.valueOf((String) map.get("uuid")));
        } else {
            List uuids = (List) map.get("uuids");
            operationTypeDao.deleteBatchIds(uuids);
        }
        return true;
    }

    @Cacheable(value = "operationTypeList", key = "#terminalType")
    public List getOperationTypeValues(String terminalType) {
        List<String> list = operationTypeDao.getOperationTypeByTerminalType(terminalType);
        list.add("");
        Collections.sort(list);
        return list;
    }

    public List getOperationTypeValuesByRole(String terminalType) {
        Subject subject = SecurityUtils.getSubject();
        int flag = 0;
        if (subject.hasRole("AlgorithmGroup")) {
            flag = 1;
        }
        List<String> list = operationTypeDao.getOperationTypeByTerminalTypeAndRole(terminalType, flag);
        list.add("");
        Collections.sort(list);
        return list;
    }

    @CacheEvict(value = "operationTypeList", key = "#terminalType")
    public void clearOperationTypeCache(String terminalType) {
        logger.info("OperationTypeList Cache is Clear!");
    }
}
