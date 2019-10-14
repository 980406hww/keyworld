package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.OperationTypeDao;
import com.keymanager.ckadmin.entity.OperationType;
import com.keymanager.ckadmin.service.OperationTypeService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("operationTypeService2")
public class OperationTypeServiceImpl extends ServiceImpl<OperationTypeDao, OperationType> implements OperationTypeService {

    private static Logger logger = LoggerFactory.getLogger(OperationTypeServiceImpl.class);

    @Resource(name = "operationTypeDao2")
    private OperationTypeDao operationTypeDao;

    @Override
    public List<OperationType> getOperationTypes(OperationType operationType, Page<OperationType> page) {
        return operationTypeDao.searchOperationTypeListsPage(page, operationType);
    }

    @Override
    public OperationType getOperationType(Long uuid) {
        return operationTypeDao.selectById(uuid);
    }

    @Override
    @CacheEvict(value = "operationTypeList", allEntries = true)
    public Boolean deleteOperationType(Map<String, Object> map) {
        if (map.get("deleteType") != null) {
            String uuid = String.valueOf(map.get("uuid"));
            operationTypeDao.deleteById(Long.valueOf(uuid));
        } else {
            List uuids = (List) map.get("uuids");
            operationTypeDao.deleteBatchIds(uuids);
        }
        return true;
    }

    @Override
    @Cacheable(value = "operationTypeList", key = "#terminalType")
    public List getOperationTypeValues(String terminalType) {
        List<String> list = operationTypeDao.getOperationTypeByTerminalType(terminalType);
        list.add("");
        Collections.sort(list);
        return list;
    }

    @Override
    public List<String> getOperationTypeValuesByRole(String terminalType) {
        Subject subject = SecurityUtils.getSubject();
        int flag = 0;
        if (subject.hasRole("AlgorithmGroup")) {
            flag = 1;
        }
        return operationTypeDao.getOperationTypeByTerminalTypeAndRole(terminalType, flag);
    }

    @Override
    @CacheEvict(value = "operationTypeList", key = "#terminalType")
    public void clearOperationTypeCache(String terminalType) {
        logger.info("OperationTypeList Cache is Clear!");
    }
}
