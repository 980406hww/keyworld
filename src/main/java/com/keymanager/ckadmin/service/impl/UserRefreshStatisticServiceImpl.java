package com.keymanager.ckadmin.service.impl;
import com.keymanager.ckadmin.criteria.UserRefreshStatisticCriteria;
import com.keymanager.ckadmin.dao.UserRefreshStatisticDao;
import com.keymanager.ckadmin.entity.UserRefreshStatisticInfo;
import com.keymanager.ckadmin.service.UserRefreshStatisticService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service("userRefreshStatisticService")
public class UserRefreshStatisticServiceImpl implements UserRefreshStatisticService {

    @Resource(name = "userRefreshStatisticDao")
    private UserRefreshStatisticDao userRefreshStatisticDao;

    @Override
    public List<UserRefreshStatisticInfo> generateUserRefreshStatisticInfo(UserRefreshStatisticCriteria criteria) {
       List<UserRefreshStatisticInfo> userRefreshStatisticInfos= userRefreshStatisticDao.getUserRefreshStatisticInfo(criteria);
       setUserRefreshStatisticInfo(userRefreshStatisticInfos);
        return userRefreshStatisticInfos;
    }

    @Override
    public void setUserRefreshStatisticInfo(List<UserRefreshStatisticInfo> userRefreshStatisticInfos) {
        UserRefreshStatisticInfo total=new UserRefreshStatisticInfo();
        total.setUserName("总计");
        for (UserRefreshStatisticInfo userRefreshStatisticInfo : userRefreshStatisticInfos) {
            total.setNeedOptimizeCount(total.getNeedOptimizeCount() + userRefreshStatisticInfo.getNeedOptimizeCount());
            total.setNeedOptimizeKeywordCount(total.getNeedOptimizeKeywordCount() + userRefreshStatisticInfo.getNeedOptimizeKeywordCount());
            total.setQueryCount(total.getQueryCount() + userRefreshStatisticInfo.getQueryCount());
            total.setTotalKeywordCount(total.getTotalKeywordCount() + userRefreshStatisticInfo.getTotalKeywordCount());
            total.setTotalOptimizeCount(total.getTotalOptimizeCount() + userRefreshStatisticInfo.getTotalOptimizeCount());
            total.setTotalOptimizedCount(total.getTotalOptimizedCount() + userRefreshStatisticInfo.getTotalOptimizedCount());
            total.setZeroOptimizedCount(total.getZeroOptimizedCount() + userRefreshStatisticInfo.getZeroOptimizedCount());
            total.setReachStandardKeywordCount(total.getReachStandardKeywordCount() + userRefreshStatisticInfo.getReachStandardKeywordCount());
            total.setTodaySubTotal(total.getTodaySubTotal() + userRefreshStatisticInfo.getTodaySubTotal());
            total.setMaxInvalidCount(userRefreshStatisticInfo.getMaxInvalidCount());
            userRefreshStatisticInfo.setAvgOptimizedCount();
            userRefreshStatisticInfo.setInvalidOptimizePercentage();
            userRefreshStatisticInfo.setReachStandardPercentage();
        }
        total.setAvgOptimizedCount();
        total.setInvalidOptimizePercentage();
        total.setReachStandardPercentage();
        userRefreshStatisticInfos.add(0, total);
    }

    @Override
    public void updateUserRefreshStatisticInfo() {
        List<UserRefreshStatisticInfo> userRefreshStatisticInfos=this.generateUserRefreshStatisticInfo(new UserRefreshStatisticCriteria());
        for(UserRefreshStatisticInfo userRefreshStatisticInfo:userRefreshStatisticInfos){
            userRefreshStatisticInfo.setCreateDate(new Date());
            userRefreshStatisticDao.insert(userRefreshStatisticInfo);
        }
    }

    @Override
    public List<UserRefreshStatisticInfo> getHistoryUserRefreshStatisticInfo(UserRefreshStatisticCriteria criteria) {
        return userRefreshStatisticDao.getHistoryUserRefreshStatisticInfo(criteria);
    }

}
