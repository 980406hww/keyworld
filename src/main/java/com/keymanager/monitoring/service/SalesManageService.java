package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.SalesManageDao;
import com.keymanager.monitoring.entity.SalesManage;
import com.keymanager.monitoring.enums.WebsiteTypeEnum;
import com.keymanager.monitoring.vo.SalesManageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wjianwu 2019/6/6 15:11
 */
@Service
public class SalesManageService extends ServiceImpl<SalesManageDao, SalesManage> {

    @Autowired
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
        List<SalesManage> manageList = salesManageDao.getSalesManages(page, salesManage);
        List<SalesManage> sms = new ArrayList<>();
        for(SalesManage s : manageList){
            sms.add(parseManagePart(s));
        }
        return sms;
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
