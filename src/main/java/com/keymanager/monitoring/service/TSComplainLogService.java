package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.TSComplainLogDao;
import com.keymanager.monitoring.entity.TSComplainLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shunshikj08 on 2017/8/2.
 */
@Service
public class TSComplainLogService extends ServiceImpl<TSComplainLogDao, TSComplainLog> {

    private static Logger logger = LoggerFactory.getLogger(TSComplainLogService.class);

    @Autowired
    private TSComplainLogDao tsComplainLogDao;
}
