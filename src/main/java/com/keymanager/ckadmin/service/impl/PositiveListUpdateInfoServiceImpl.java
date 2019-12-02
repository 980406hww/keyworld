package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.PositiveListUpdateInfoDao;
import com.keymanager.ckadmin.entity.PositiveListUpdateInfo;
import com.keymanager.ckadmin.service.PositiveListUpdateInfoService;
import com.keymanager.ckadmin.vo.PositiveListVO;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "positiveListUpdateInfoService2")
public class PositiveListUpdateInfoServiceImpl extends ServiceImpl<PositiveListUpdateInfoDao, PositiveListUpdateInfo> implements PositiveListUpdateInfoService {

    @Resource(name = "positiveListUpdateInfoDao2")
    private PositiveListUpdateInfoDao positiveListUpdateInfoDao;

    @Override
    public void savePositiveListUpdateInfo (PositiveListVO positiveListVo, String userName) {
        if (positiveListVo.isHasUpdateInfo()) {
            List<PositiveListUpdateInfo> mostRecentPositiveListUpdateInfo = positiveListUpdateInfoDao.findMostRecentPositiveListUpdateInfo(positiveListVo.getPositiveList().getUuid());
            PositiveListUpdateInfo positiveListUpdateInfo = mostRecentPositiveListUpdateInfo.get(0);
            positiveListUpdateInfo.setUpdateTime(new Date());
            positiveListUpdateInfoDao.updateById(positiveListUpdateInfo);
        }
        PositiveListUpdateInfo positiveListUpdateInfo = new PositiveListUpdateInfo();
        positiveListUpdateInfo.setPid(positiveListVo.getPositiveList().getUuid());
        positiveListUpdateInfo.setOptimizeMethod(positiveListVo.getPositiveList().getOptimizeMethod());
        positiveListUpdateInfo.setUserName(userName);
        positiveListUpdateInfo.setCreateTime(new Date());
        positiveListUpdateInfo.setPosition(positiveListVo.getPositiveList().getPosition());
        positiveListUpdateInfoDao.insert(positiveListUpdateInfo);
    }

    @Override
    public List<PositiveListUpdateInfo> findPositiveListUpdateInfos (Long pid) {
        return positiveListUpdateInfoDao.findMostRecentPositiveListUpdateInfo(pid);
    }

    @Override
    public void deleteByPid (long pid) {
        positiveListUpdateInfoDao.deleteByPid(pid);
    }
}
