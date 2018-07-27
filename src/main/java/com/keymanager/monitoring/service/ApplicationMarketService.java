package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.ApplicationMarketCriteria;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.dao.ApplicationMarketDao;
import com.keymanager.monitoring.dao.SupplierDao;
import com.keymanager.monitoring.entity.ApplicationMarket;
import com.keymanager.monitoring.entity.Supplier;
import com.keymanager.monitoring.entity.SupplierServiceType;
import com.keymanager.monitoring.entity.SupplierServiceTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@Service
public class ApplicationMarketService extends ServiceImpl<ApplicationMarketDao, ApplicationMarket>{
    private static Logger logger = LoggerFactory.getLogger(ApplicationMarketService.class);
    @Autowired
    private ApplicationMarketDao applicationMarketDao;

    public List<ApplicationMarket> getmarketInfo() {
        return applicationMarketDao.getmarketInfo();
    }

    public Page<ApplicationMarket> searchapplicationMarket(Page<ApplicationMarket> page, ApplicationMarketCriteria applicationMarketCriteria) {
        page.setRecords(applicationMarketDao.searchpplicationMarket(page,applicationMarketCriteria));
        return page;
    }

    public void deleteApplicationMarket(Long uuid) {
        applicationMarketDao.deleteById(uuid);
    }

    public void deleteApplicationMarketList(List<String> uuids) {
        List<Long> uuidList = new ArrayList<Long>();
        for (String uuid : uuids){
            uuidList.add(Long.valueOf(uuid));
        }
        applicationMarketDao.deleteBatchIds(uuidList);
    }

    public void saveApplicationMarket(ApplicationMarket applicationMarket) {
        if (null != applicationMarket.getUuid()) {
            applicationMarketDao.updateById(applicationMarket);
        } else {
            applicationMarketDao.insert(applicationMarket);
        }
    }

    public ApplicationMarket getApplicationMarket(Long uuid) {
        ApplicationMarket applicationMarket = applicationMarketDao.selectById(uuid);
        return applicationMarket;
    }

    public List<ApplicationMarket> selectApplicationMarket() {
        return applicationMarketDao.selectApplicationMarket();
    }
}
