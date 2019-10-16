package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.SalesInfoCriteria;
import com.keymanager.ckadmin.dao.SalesManageDao;
import com.keymanager.ckadmin.entity.SalesManage;
import com.keymanager.ckadmin.service.SalesManageService;
import com.keymanager.ckadmin.vo.SalesManageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "salesManageService2")
public class SalesManageServiceImpl extends ServiceImpl<SalesManageDao, SalesManage> implements SalesManageService {

    @Resource(name = "salesManageDao2")
    private SalesManageDao salesManageDao;

    public List<SalesManageVO> getAllSalesInfo(String websiteType) {
        return salesManageDao.selectAllSalesInfo(websiteType);
    }

    public void deleteSalesManage(Long uuid) {
        salesManageDao.deleteById(uuid);
    }

    public void deleteBatchSalesManage(List uuids) {
        salesManageDao.deleteBatchIds(uuids);
    }

    public SalesManage getSalesManageByUuid(Long uuid) {
        return salesManageDao.selectById(uuid);
    }

    public Page<SalesManage> SearchSalesManages(SalesInfoCriteria salesInfoCriteria, Page<SalesManage> page) {
        page.setRecords(salesManageDao.getSalesManages(page, salesInfoCriteria));
        return page;
    }

}
