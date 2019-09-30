package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.ClientStatusRestartLogDao;
import com.keymanager.ckadmin.entity.ClientStatusRestartLog;
import com.keymanager.ckadmin.service.ClientStatusRestartLogService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("clientStatusRestartLogService2")
public class ClientStatusRestartLogServiceImpl extends ServiceImpl<ClientStatusRestartLogDao, ClientStatusRestartLog>
        implements ClientStatusRestartLogService {
    private static Logger logger = LoggerFactory.getLogger(ClientStatusRestartLogServiceImpl.class);

    @Resource(name = "clientStatusRestartLogDao2")
    private ClientStatusRestartLogDao clientStatusRestartLogDao;
}
