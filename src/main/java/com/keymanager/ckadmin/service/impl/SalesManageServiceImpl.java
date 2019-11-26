package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.SalesInfoCriteria;
import com.keymanager.ckadmin.dao.SalesManageDao;
import com.keymanager.ckadmin.entity.SalesManage;
import com.keymanager.ckadmin.enums.WebsiteTypeEnum;
import com.keymanager.ckadmin.service.SalesManageService;
import com.keymanager.ckadmin.vo.SalesManageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        SalesManage salesManage = salesManageDao.selectById(uuid);
        return parseManagePart(salesManage);
    }

    public List<String> getAllSalesName(){
        return salesManageDao.selectAllSalesName();
    }

    public Page<SalesManage> SearchSalesManages(SalesInfoCriteria salesInfoCriteria, Page<SalesManage> page) {
        List<SalesManage> salesManages = salesManageDao.getSalesManages(page, salesInfoCriteria);
        List<SalesManage> sms = new ArrayList<>();
        for (SalesManage s : salesManages) {
            sms.add(parseManagePart(s));
        }
        page.setRecords(sms);
        return page;
    }

    private SalesManage parseManagePart(SalesManage salesManage){
        Map<String, String> typeMap = WebsiteTypeEnum.changeToMap();
        String str  = salesManage.getManagePart();
        if(str.indexOf(",") == -1){
            salesManage.setManagePart(typeMap.get(str));
        }else{
            String[] split = str.split(",");
            String str2 = "";
            for(String tmp : split){
                str2 = str2 + typeMap.get(tmp) + ",";
            }
            str2 = str2.substring(0,str2.length() - 1);
            salesManage.setManagePart(str2);
        }
        return salesManage;
    }

}
