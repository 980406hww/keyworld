package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.criteria.UserRefreshStatisticCriteria;
import com.keymanager.ckadmin.dao.UserRefreshStatisticDao;
import com.keymanager.ckadmin.entity.UserRefreshStatisticInfo;

import java.util.List;

public interface UserRefreshStatisticService {

    List<UserRefreshStatisticInfo> generateUserRefreshStatisticInfo(UserRefreshStatisticCriteria criteria);

    void setUserRefreshStatisticInfo(List<UserRefreshStatisticInfo> userRefreshStatisticInfos);

    void updateUserRefreshStatisticInfo();

    List<UserRefreshStatisticInfo> getHistoryUserRefreshStatisticInfo(UserRefreshStatisticCriteria criteria);
}
