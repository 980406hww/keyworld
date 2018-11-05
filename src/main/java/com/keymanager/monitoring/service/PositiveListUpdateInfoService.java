package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.PositiveListDao;
import com.keymanager.monitoring.dao.PositiveListUpdateInfoDao;
import com.keymanager.monitoring.entity.PositiveList;
import com.keymanager.monitoring.entity.PositiveListUpdateInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PositiveListUpdateInfoService extends ServiceImpl<PositiveListUpdateInfoDao, PositiveListUpdateInfo> {

    private static Logger logger = LoggerFactory.getLogger(PositiveListUpdateInfoService.class);

    @Autowired
    private PositiveListUpdateInfoDao positiveListUpdateInfoDao;

    public void savePositiveListUpdateInfo (PositiveList positiveList) {
        List<PositiveListUpdateInfo> existingPositiveListUpdateInfos = positiveListUpdateInfoDao.findMostRecentPositiveListUpdateInfo(positiveList.getUuid());
        if (CollectionUtils.isNotEmpty(existingPositiveListUpdateInfos)) {
            PositiveListUpdateInfo positiveListUpdateInfo = existingPositiveListUpdateInfos.get(0);
            positiveListUpdateInfo.setUpdateTime(new Date());
            positiveListUpdateInfoDao.updateById(positiveListUpdateInfo);
        }
        PositiveListUpdateInfo positiveListUpdateInfo = new PositiveListUpdateInfo();
        positiveListUpdateInfo.setPid(positiveList.getUuid());
        positiveListUpdateInfo.setOptimizeMethod(positiveList.getOptimizeMethod());
        positiveListUpdateInfo.setCreateTime(new Date());
        positiveListUpdateInfo.setPosition(positiveList.getPosition());
        positiveListUpdateInfoDao.insert(positiveListUpdateInfo);
    }

    public List<PositiveListUpdateInfo> findPositiveListUpdateInfos (Long pid) {
        return positiveListUpdateInfoDao.findMostRecentPositiveListUpdateInfo(pid);
    }
}
