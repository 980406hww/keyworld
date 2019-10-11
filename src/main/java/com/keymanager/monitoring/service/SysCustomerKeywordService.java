package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.SysCustomerKeywordDao;
import com.keymanager.monitoring.entity.SysCustomerKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shunshikj40
 */
@Service
public class SysCustomerKeywordService extends ServiceImpl<SysCustomerKeywordDao, SysCustomerKeyword> {

    @Autowired
    private SysCustomerKeywordDao sysCustomerKeywordDao;

    public void batchInsertCustomerKeywordByCustomerUuid(Long customerUuid, Long qsId) {
        sysCustomerKeywordDao.batchInsertCustomerKeywordByCustomerUuid(customerUuid, qsId);
    }


    public void cleanSysCustomerKeywordCreateOverOneWeek() {
        sysCustomerKeywordDao.cleanSysCustomerKeywordCreateOverOneWeek();
    }
}
