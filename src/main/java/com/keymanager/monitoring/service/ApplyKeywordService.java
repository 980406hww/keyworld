package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.criteria.ApplyKeywordCriteria;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.dao.ApplyKeywordDao;
import com.keymanager.monitoring.dao.SupplierDao;
import com.keymanager.monitoring.entity.ApplyKeyword;
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
public class ApplyKeywordService extends ServiceImpl<ApplyKeywordDao, ApplyKeyword>{
    private static Logger logger = LoggerFactory.getLogger(ApplyKeywordService.class);
    @Autowired
    private ApplyKeywordDao applyKeywordDao;

    public String[] getKeywordApplyUuid(Long applyUuid) {
        return applyKeywordDao.getKeywordApplyUuid(applyUuid);
    }

    public ApplyKeyword selectApplyKeyword(Long applyUuid, String keyword) {
        return applyKeywordDao.selectApplyKeyword(applyUuid,keyword);
    }

    public Page<ApplyKeyword> searchApplyKeyword(Page<ApplyKeyword> page, ApplyKeywordCriteria applyKeywordCriteria) {
        page.setRecords(applyKeywordDao.searchApplyKeyword(page,applyKeywordCriteria));
        return page;
    }

    public void deleteApplyKeyword(Long uuid) {
        applyKeywordDao.deleteById(uuid);
    }

    public void deleteApplyKeywordList(List<String> uuids) {
        List<Long> uuidList = new ArrayList<Long>();
        for (String uuid : uuids){
            uuidList.add(Long.valueOf(uuid));
        }
        applyKeywordDao.deleteBatchIds(uuidList);
    }

    public void saveApplyKeyword(String uuidStr,String keywords, Long applyUuid,String applyName) {
        if(StringUtils.isNotBlank(uuidStr)){
            Long uuid = (Integer.valueOf(uuidStr)).longValue();
            ApplyKeyword applyKeyword = applyKeywordDao.selectById(uuid);
            applyKeyword.setKeyword(keywords);
            applyKeyword.setApplyUuid(applyUuid);
            applyKeyword.setApplyName(applyName);
            applyKeywordDao.updateById(applyKeyword);
        }else {
            String [] keywordList = keywords.split(",");
            List<String> keywordStrList = new ArrayList<String>();
            for(String keyword:keywordList){
                if(StringUtils.isNotBlank(keyword)){
                    ApplyKeyword applyKeyword = new ApplyKeyword();
                    applyKeyword.setApplyUuid(applyUuid);
                    applyKeyword.setKeyword(keyword);
                    applyKeyword.setApplyName(applyName);
                    applyKeyword.setBrushNumber(0);
                    applyKeywordDao.insert(applyKeyword);
                }
            }
        }
    }

}
