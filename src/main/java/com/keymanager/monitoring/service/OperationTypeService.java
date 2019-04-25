package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.OperationTypeDao;
import com.keymanager.monitoring.entity.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

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

    public ModelAndView constructSearchOperationTypeListsModelAndView(int currentPageNumber, int pageSize, OperationType operationType) {
        ModelAndView modelAndView = new ModelAndView("/operationType/operationType");
        Page<OperationType> page = new Page<>(currentPageNumber, pageSize);
        List<OperationType> list = operationTypeDao.searchOperationTypeListsPage(page, operationType);
        page.setRecords(list);
        modelAndView.addObject("operationType", operationType);
        modelAndView.addObject("page", page);
        return modelAndView;
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

    @CacheEvict(value = "operationTypeList", key = "#terminalType")
    public void clearOperationTypeCache(String terminalType) {
        logger.info("OperationTypeList Cache is Clear!");
    }
}
