package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.IndustryCriteria;
import com.keymanager.monitoring.entity.IndustryInfo;
import com.keymanager.monitoring.dao.IndustryInfoDao;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 行业表 服务实现类
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
@Service
public class IndustryInfoService extends ServiceImpl<IndustryInfoDao, IndustryInfo> {

    @Autowired
    private IndustryInfoDao industryInfoDao;

    @Autowired
    private IndustryDetailService industryDetailService;

    public Page<IndustryInfo> searchIndustries (Page<IndustryInfo> page, IndustryCriteria industryCriteria) {
        return page.setRecords(industryInfoDao.searchIndustries(page, industryCriteria));
    }

    public IndustryInfo getIndustry (long uuid) {
        return industryInfoDao.selectById(uuid);
    }

    public void saveIndustryInfo (IndustryInfo industryInfo, String loginName) {
        if(null == industryInfo.getUuid()) {
            IndustryInfo existingIndustryInfo = industryInfoDao.findExistingIndustryInfo(industryInfo.getIndustryName());
            if(null == existingIndustryInfo) {
                industryInfo.setUserID(loginName);
                industryInfoDao.insert(industryInfo);
            }
        } else {
            industryInfo.setUpdateTime(new Date());
            industryInfoDao.updateById(industryInfo);
        }
    }

    public void delIndustryInfo (long uuid) {
        industryInfoDao.deleteById(uuid);
        industryDetailService.delIndustryDetailsByIndustryID(uuid);
    }

    public void updateIndustryUserID (List<String> uuids, String userID) {
        industryInfoDao.updateIndustryUserID(uuids, userID);
    }

    public void deleteIndustries(String uuids) {
        industryInfoDao.deleteIndustries(Arrays.asList(uuids.split(",")));
    }
}
