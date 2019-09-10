package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.OperationCombineDao;
import com.keymanager.ckadmin.entity.OperationCombine;
import com.keymanager.ckadmin.service.OperationCombineService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("OperationCombineService2")
public class OperationCombineServiceImpl extends
    ServiceImpl<OperationCombineDao, OperationCombine> implements OperationCombineService {

    @Resource(name = "OperationCombineDao2")
    private OperationCombineDao operationCombineDao;

    @Override
    public String getOperationCombineName(String optimizeGroupName) {
        return operationCombineDao.getOperationCombineName(optimizeGroupName);
    }
}
