package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZCaptureTitleLog;

public interface QZCaptureTitleLogService extends IService<QZCaptureTitleLog> {

    void addQZCaptureTitleLog(QZCaptureTitleLog qzCaptureTitleLog);
}
