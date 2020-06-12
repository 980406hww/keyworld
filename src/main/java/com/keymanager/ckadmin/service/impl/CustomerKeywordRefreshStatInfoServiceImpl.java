package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.dao.CustomerKeywordRefreshStatInfoDao;
import com.keymanager.ckadmin.entity.RefreshStatRecord;
import com.keymanager.ckadmin.service.CustomerKeywordRefreshStatInfoService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("customerKeywordRefreshStatInfoService2")
public class CustomerKeywordRefreshStatInfoServiceImpl extends ServiceImpl<CustomerKeywordRefreshStatInfoDao, RefreshStatRecord>
        implements CustomerKeywordRefreshStatInfoService {

    @Resource(name = "customerKeywordRefreshStatInfoDao2")
    private CustomerKeywordRefreshStatInfoDao refreshStatInfoDao;

    @Override
    public List<RefreshStatRecord> generateCustomerKeywordStatInfo(RefreshStatisticsCriteria criteria) {
        List<RefreshStatRecord> refreshStatRecords = refreshStatInfoDao.searchCustomerKeywordStatInfos(criteria);
        setCountCustomerKeywordRefreshStatInfo(refreshStatRecords);
        return refreshStatRecords;
    }

    @Override
    public List<RefreshStatRecord> getHistoryTerminalRefreshStatRecord(RefreshStatisticsCriteria criteria) {
        List<RefreshStatRecord> refreshStatRecords = refreshStatInfoDao.getHistoryTerminalRefreshStatRecord(criteria);
        setCountCustomerKeywordRefreshStatInfo(refreshStatRecords);
        return refreshStatRecords;
    }

    @Override
    public void setCountCustomerKeywordRefreshStatInfo(List<RefreshStatRecord> refreshStatRecords) {
        RefreshStatRecord total = new RefreshStatRecord();
        total.setGroup("总计");
        for (RefreshStatRecord refreshStatRecord : refreshStatRecords) {
            total.setInvalidKeywordCount(total.getInvalidKeywordCount() + refreshStatRecord.getInvalidKeywordCount());
            total.setNeedOptimizeCount(total.getNeedOptimizeCount() + refreshStatRecord.getNeedOptimizeCount());
            total.setNeedOptimizeKeywordCount(total.getNeedOptimizeKeywordCount() + refreshStatRecord.getNeedOptimizeKeywordCount());
            total.setQueryCount(total.getQueryCount() + refreshStatRecord.getQueryCount());
            total.setTotalKeywordCount(total.getTotalKeywordCount() + refreshStatRecord.getTotalKeywordCount());
            total.setTotalOptimizeCount(total.getTotalOptimizeCount() + refreshStatRecord.getTotalOptimizeCount());
            total.setTotalOptimizedCount(total.getTotalOptimizedCount() + refreshStatRecord.getTotalOptimizedCount());
            total.setZeroOptimizedCount(total.getZeroOptimizedCount() + refreshStatRecord.getZeroOptimizedCount());
            total.setReachStandardKeywordCount(total.getReachStandardKeywordCount() + refreshStatRecord.getReachStandardKeywordCount());
            total.setTodaySubTotal(total.getTodaySubTotal() + refreshStatRecord.getTodaySubTotal());
        }
        total.setMaxInvalidCount(4);
        refreshStatRecords.add(0, total);
    }

    public void updateCustomerKeywordStatInfo (){
        // 只保存一周的数据
        refreshStatInfoDao.deleteOverOneWeekData();

        List<RefreshStatRecord> refreshStatInfoRecords = generateAllCustomerKeywordStatInfo(new RefreshStatisticsCriteria());
        for (RefreshStatRecord refreshStatInfoRecord : refreshStatInfoRecords) {
            refreshStatInfoRecord.setCreateDate(new Date());
            refreshStatInfoDao.insert(refreshStatInfoRecord);
        }
    }

    private List<RefreshStatRecord> generateAllCustomerKeywordStatInfo(RefreshStatisticsCriteria criteria) {
        return refreshStatInfoDao.searchCustomerKeywordStatInfos(criteria);
    }
}
