package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ServiceProviderDao;
import com.keymanager.monitoring.entity.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/1.
 */
@Service
public class ServiceProviderService extends ServiceImpl<ServiceProviderDao, ServiceProvider> {
    @Autowired
    private ServiceProviderDao serviceProviderDao;

    public List<ServiceProvider> searchServiceProviders() {
        return serviceProviderDao.selectList(null);
    }
}
