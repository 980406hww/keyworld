package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.SalesManageCriteria;
import com.keymanager.monitoring.dao.SalesManageDao;
import com.keymanager.monitoring.entity.SalesManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wjianwu 2019/6/6 15:11
 */
@Service
public class SalesManageService extends ServiceImpl<SalesManageDao, SalesManage> {

    @Autowired
    private SalesManageDao salesManageDao;

    public List<SalesManageCriteria> getAllSalesInfo(String websiteType) {
        return salesManageDao.selectAllSalesInfo(websiteType);
    }

    public void deleteSalesManage(Long uuid) {
        salesManageDao.deleteById(uuid);
    }

    public void deleteBeachSalesManage(List uuids) {
        salesManageDao.deleteBatchIds(uuids);
    }

    public SalesManage getSalesManageByUuid(Long uuid) {
        return salesManageDao.selectById(uuid);
    }

    public List<SalesManage> SearchSalesManages(SalesManage salesManage, Page<SalesManage> page) {
        return salesManageDao.getSalesManages(page, salesManage);
    }
}
