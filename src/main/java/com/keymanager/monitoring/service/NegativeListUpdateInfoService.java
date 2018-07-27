package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.NegativeListUpdateInfoDao;
import com.keymanager.monitoring.entity.NegativeListUpdateInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NegativeListUpdateInfoService extends ServiceImpl<NegativeListUpdateInfoDao, NegativeListUpdateInfo> {

    private static Logger logger = LoggerFactory.getLogger(NegativeListUpdateInfoService.class);

    @Autowired
    private NegativeListUpdateInfoDao negativeListUpdateInfoDao;

    public NegativeListUpdateInfo getNegativeListUpdateInfo(String keyword) {
        return negativeListUpdateInfoDao.getNegativeListUpdateInfo(keyword);
    }

    public void saveNegativeListUpdateInfo(String keyword) {
        NegativeListUpdateInfo oldNegativeListUpdateInfo = getNegativeListUpdateInfo(keyword);
        if(oldNegativeListUpdateInfo == null) {
            NegativeListUpdateInfo negativeListUpdateInfo = new NegativeListUpdateInfo();
            negativeListUpdateInfo.setKeyword(keyword);
            negativeListUpdateInfo.setNegativeListUpdateTime(new Date());
            negativeListUpdateInfo.setCreateTime(new Date());
            negativeListUpdateInfoDao.insert(negativeListUpdateInfo);
        } else {
            oldNegativeListUpdateInfo.setNegativeListUpdateTime(new Date());
            negativeListUpdateInfoDao.updateById(oldNegativeListUpdateInfo);
        }
    }
}
