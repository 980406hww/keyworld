package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.dao.CustomerKeywordTerminalRefreshStatRecordDao;
import com.keymanager.monitoring.entity.CustomerKeywordTerminalRefreshStatRecord;
import com.keymanager.monitoring.vo.CustomerKeywordRefreshStatInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/11/28 17:56
 **/
@Service
public class CustomerKeywordTerminalRefreshStatRecordService extends ServiceImpl<CustomerKeywordTerminalRefreshStatRecordDao, CustomerKeywordTerminalRefreshStatRecord> {

    @Autowired
    private CustomerKeywordTerminalRefreshStatRecordDao customerKeywordTerminalRefreshStatRecordDao;

    @Autowired
    private CustomerKeywordRefreshStatInfoService customerKeywordRefreshStatInfoService;

    public List<CustomerKeywordRefreshStatInfoVO> getHistoryTerminalRefreshStatRecord (CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        List<CustomerKeywordRefreshStatInfoVO> historyTerminalRefreshStatRecords = customerKeywordTerminalRefreshStatRecordDao.getHistoryTerminalRefreshStatRecord(customerKeywordRefreshStatInfoCriteria);
        customerKeywordRefreshStatInfoService.SetCountCustomerKeywordRefreshStatInfoVO(historyTerminalRefreshStatRecords);
        return historyTerminalRefreshStatRecords;
    }
}
