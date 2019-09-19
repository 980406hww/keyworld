package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.entity.QZCaptureTitleLog;
import com.keymanager.ckadmin.dao.QZCaptureTitleLogDao;
import com.keymanager.ckadmin.service.QZCaptureTitleLogService;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("qzCaptureTitleLogService2")
public class QZCaptureTitleLogServiceImpl extends
    ServiceImpl<QZCaptureTitleLogDao, QZCaptureTitleLog> implements QZCaptureTitleLogService {

    @Resource(name = "qzCaptureTitleLogDao2")
    private QZCaptureTitleLogDao qzCaptureTitleLogDao;

    @Override
    public void addQZCaptureTitleLog(QZCaptureTitleLog qzCaptureTitleLog) {
        qzCaptureTitleLog.setUpdateTime(new Date());
        qzCaptureTitleLogDao.insert(qzCaptureTitleLog);
    }
}
