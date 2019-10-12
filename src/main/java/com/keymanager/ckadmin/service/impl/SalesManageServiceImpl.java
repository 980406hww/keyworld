package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
