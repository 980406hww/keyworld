package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.ServerAddressCriteria;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.dao.ServerAddressDao;
import com.keymanager.monitoring.dao.SupplierDao;
import com.keymanager.monitoring.entity.ServerAddress;
import com.keymanager.monitoring.entity.Supplier;
import com.keymanager.monitoring.entity.SupplierServiceType;
import com.keymanager.monitoring.entity.SupplierServiceTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@Service
public class ServerAddressService extends ServiceImpl<ServerAddressDao, ServerAddress>{
    private static Logger logger = LoggerFactory.getLogger(ServerAddressService.class);
    @Autowired
    private ServerAddressDao serverAddressDao;

    public Page<ServerAddress> searchServerAddressList(Page<ServerAddress> page, ServerAddressCriteria serverAddressCriteria) {
        page.setRecords(serverAddressDao.searchServerAddressList(page,serverAddressCriteria));
        return page;
    }
}
