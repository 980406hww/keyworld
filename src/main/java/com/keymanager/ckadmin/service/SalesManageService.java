package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.SalesManage;
import com.keymanager.ckadmin.vo.SalesManageVO;

import java.util.List;

public interface SalesManageService extends IService<SalesManage> {

    List<SalesManageVO> getAllSalesInfo(String websiteType);

    void deleteSalesManage(Long uuid);

    void deleteBeachSalesManage(List uuids);

    SalesManage getSalesManageByUuid(Long uuid);

    List<SalesManage> SearchSalesManages(SalesManage salesManage, Page<SalesManage> page);
}
