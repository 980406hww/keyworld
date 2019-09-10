package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.QZOperationTypeDao;
import com.keymanager.ckadmin.entity.QZOperationType;
import com.keymanager.ckadmin.service.QZOperationTypeService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("QZOperationTypeService2")
public class QZOperationTypeServiceImpl extends
    ServiceImpl<QZOperationTypeDao, QZOperationType> implements QZOperationTypeService {

    @Resource(name = "QZOperationTypeDao2")
    private QZOperationTypeDao qzOperationTypeDao;

    @Override
    public Date getStandardTime(long qzSettingUuid, String terminalType) {
        return qzOperationTypeDao.getStandardTime(qzSettingUuid, terminalType);
    
    @Override
    public List<QZOperationType> searchQZOperationTypesByQZSettingUuid(Long uuid) {
        return qzOperationTypeDao.searchQZOperationTypesByQZSettingUuid(uuid);
    }
}
