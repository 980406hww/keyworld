package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.ServerAddressCriteria;
import com.keymanager.monitoring.dao.ServerAddressDao;
import com.keymanager.monitoring.entity.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@Service
public class ServerAddressService extends ServiceImpl<ServerAddressDao, ServerAddress> {
    private static Logger logger = LoggerFactory.getLogger(ServerAddressService.class);
    @Autowired
    private ServerAddressDao serverAddressDao;

    public Page<ServerAddress> searchServerAddressList(Page<ServerAddress> page, ServerAddressCriteria serverAddressCriteria) {
        page.setRecords(serverAddressDao.searchServerAddressList(page,serverAddressCriteria));
        return page;
    }

    public void deleteServerAddress(Long uuid) {
        serverAddressDao.deleteById(uuid);
    }

    public void deleteServerAddressList(List<String> uuids) {
        List<Long> uuidList = new ArrayList<Long>();
        for (String uuid : uuids){
            uuidList.add(Long.valueOf(uuid));
        }
        serverAddressDao.deleteBatchIds(uuidList);
    }

    public void saveServerAddress(ServerAddress serverAddress) {
        if (null != serverAddress.getUuid()) {
            serverAddressDao.updateById(serverAddress);
        } else {
            serverAddressDao.insert(serverAddress);
        }
    }

    public ServerAddress getServerAddress(Long uuid) {
        ServerAddress serverAddress = serverAddressDao.selectById(uuid);
        return serverAddress;
    }
}
