package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.PositiveListUpdateInfoDao;
import com.keymanager.monitoring.entity.PositiveListUpdateInfo;
import com.keymanager.monitoring.vo.PositiveListVO;
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

    public void savePositiveListUpdateInfo (PositiveListVO positiveListVO, String userName) {
        if (positiveListVO.isHasUpdateInfo()) {
            List<PositiveListUpdateInfo> mostRecentPositiveListUpdateInfo = positiveListUpdateInfoDao.findMostRecentPositiveListUpdateInfo(positiveListVO.getPositiveList().getUuid());
            PositiveListUpdateInfo positiveListUpdateInfo = mostRecentPositiveListUpdateInfo.get(0);
            positiveListUpdateInfo.setUpdateTime(new Date());
            positiveListUpdateInfoDao.updateById(positiveListUpdateInfo);
        }
        PositiveListUpdateInfo positiveListUpdateInfo = new PositiveListUpdateInfo();
        positiveListUpdateInfo.setPid(positiveListVO.getPositiveList().getUuid());
        positiveListUpdateInfo.setOptimizeMethod(positiveListVO.getPositiveList().getOptimizeMethod());
        positiveListUpdateInfo.setUserName(userName);
        positiveListUpdateInfo.setCreateTime(new Date());
        positiveListUpdateInfo.setPosition(positiveListVO.getPositiveList().getPosition());
        positiveListUpdateInfoDao.insert(positiveListUpdateInfo);
    }

    public List<PositiveListUpdateInfo> findPositiveListUpdateInfos (Long pid) {
        return positiveListUpdateInfoDao.findMostRecentPositiveListUpdateInfo(pid);
    }

    public void deleteByPid (long pid) {
        positiveListUpdateInfoDao.deleteByPid(pid);
    }
}
