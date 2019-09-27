package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.NegativeListUpdateInfoDao;
import com.keymanager.ckadmin.entity.NegativeListUpdateInfo;
import com.keymanager.ckadmin.service.NegativeListUpdateInfoService;
import java.util.Date;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("negativeListUpdateInfoService2")
public class NegativeListUpdateInfoServiceImpl extends ServiceImpl<NegativeListUpdateInfoDao, NegativeListUpdateInfo> implements NegativeListUpdateInfoService {
    private static Logger logger = LoggerFactory.getLogger(NegativeListUpdateInfoServiceImpl.class);

    @Resource(name = "negativeListUpdateInfoDao2")
    private NegativeListUpdateInfoDao negativeListUpdateInfoDao;

    @Override
    public NegativeListUpdateInfo getNegativeListUpdateInfo(String keyword) {
        return negativeListUpdateInfoDao.getNegativeListUpdateInfo(keyword);
    }

    @Override
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
