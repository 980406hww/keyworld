package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.SalesInfoCriteria;
import com.keymanager.ckadmin.entity.SalesManage;
import com.keymanager.ckadmin.vo.SalesManageVO;

import java.util.List;

public interface SalesManageService extends IService<SalesManage> {

    List<SalesManageVO> getAllSalesInfo(String websiteType);

    void deleteSalesManage(Long uuid);

    void deleteBatchSalesManage(List uuids);

    SalesManage getSalesManageByUuid(Long uuid);

    Page<SalesManage> SearchSalesManages(SalesInfoCriteria salesInfoCriteria, Page<SalesManage> page);

    List<String> getAllSalesName();
}
