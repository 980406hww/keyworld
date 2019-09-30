package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.dao.CustomerKeywordTerminalRefreshStatRecordDao;
import com.keymanager.ckadmin.entity.RefreshStatRecord;
import com.keymanager.ckadmin.service.CustomerKeywordRefreshStatInfoService;
import com.keymanager.ckadmin.service.CustomerKeywordTerminalRefreshStatRecordService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("customerKeywordTerminalRefreshStatRecordService2")
public class CustomerKeywordTerminalRefreshStatRecordServiceImpl implements CustomerKeywordTerminalRefreshStatRecordService {

    @Resource(name = "customerKeywordTerminalRefreshStatRecordDao2")
    private CustomerKeywordTerminalRefreshStatRecordDao refreshStatRecordDao;

    @Resource(name = "customerKeywordRefreshStatInfoService2")
    private CustomerKeywordRefreshStatInfoService customerKeywordRefreshStatInfoService;

    @Override
    public List<RefreshStatRecord> getHistoryTerminalRefreshStatRecord(RefreshStatisticsCriteria criteria) {
        List<RefreshStatRecord> refreshStatRecords = refreshStatRecordDao.getHistoryTerminalRefreshStatRecord(criteria);
        customerKeywordRefreshStatInfoService.setCountCustomerKeywordRefreshStatInfo(refreshStatRecords);
        return refreshStatRecords;
    }
}
