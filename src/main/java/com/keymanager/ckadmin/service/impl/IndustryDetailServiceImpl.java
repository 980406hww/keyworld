package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.IndustryDetailCriteria;
import com.keymanager.ckadmin.dao.IndustryDetailDao;
import com.keymanager.ckadmin.entity.IndustryDetail;
import com.keymanager.ckadmin.service.IndustryDetailService;
import com.keymanager.ckadmin.service.QZSettingService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("industryDetailService2")
public class IndustryDetailServiceImpl extends ServiceImpl<IndustryDetailDao, IndustryDetail> implements IndustryDetailService {

    @Resource(name = "industryDetailDao2")
    private IndustryDetailDao industryDetailDao;

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @Override
    public void delIndustryDetailsByIndustryID(long industryID) {
        industryDetailDao.delIndustryDetailsByIndustryID(industryID);
    }

    @Override
    public Page<IndustryDetail> searchIndustryDetails(Page<IndustryDetail> page, IndustryDetailCriteria industryDetailCriteria) {
        page.setRecords(industryDetailDao.searchIndustryDetails(page, industryDetailCriteria));
        this.dealWithIndustryDetail(page);
        return page;
    }

    private void dealWithIndustryDetail(Page<IndustryDetail> page) {
        for (IndustryDetail industryDetail : page.getRecords()) {
            if (null != industryDetail.getQq() && !"".equals(industryDetail.getQq())) {
                industryDetail.setQq(industryDetail.getQq().replaceAll(",", "<br>"));
            }
            if (null != industryDetail.getTelephone() && !"".equals(industryDetail.getTelephone())) {
                industryDetail.setTelephone(industryDetail.getTelephone().replaceAll(",", "<br>"));
            }
        }
    }

    @Override
    public IndustryDetail getIndustryDetail(long uuid) {
        IndustryDetail industryDetail = industryDetailDao.selectById(uuid);
        if (null != industryDetail.getQq() && !"".equals(industryDetail.getQq())) {
            industryDetail.setQq(industryDetail.getQq().replaceAll(",", "\r\n"));
        }
        if (null != industryDetail.getTelephone() && !"".equals(industryDetail.getTelephone())) {
            industryDetail.setTelephone(industryDetail.getTelephone().replaceAll(",", "\r\n"));
        }
        return industryDetail;
    }

    @Override
    public void saveIndustryDetail(IndustryDetail industryDetail) {
        if (null == industryDetail.getUuid()) {
            IndustryDetail existingIndustryDetail = industryDetailDao.findExistingIndustryDetail(industryDetail.getIndustryID(), industryDetail.getWebsite());
            if (null == existingIndustryDetail) {
                industryDetailDao.insert(industryDetail);
            }
        } else {
            industryDetail.setUpdateTime(new Date());
            industryDetailDao.updateById(industryDetail);
        }
    }

    @Override
    public void updRemarkByUuids(List<Long> uuids, String remark) {
        industryDetailDao.updRemarkByUuids(uuids, remark);
    }

    @Override
    public void delIndustryDetail(long uuid) {
        industryDetailDao.deleteById(uuid);
    }

    @Override
    public void deleteIndustryDetails(List<String> uuids) {
        industryDetailDao.deleteIndustryDetails(uuids);
    }

    @Override
    public void updateIndustryDetailRemark(long uuid, String remark) {
        industryDetailDao.updateIndustryDetailRemark(uuid, remark);
    }

    @Override
    public void updateIndustryInfoDetail(IndustryDetailCriteria criteria) {
        IndustryDetail existingIndustryDetail = industryDetailDao.findExistingIndustryDetail(criteria.getIndustryID(), criteria.getWebsite());
        boolean updateFlag = false;
        if (null == existingIndustryDetail) {
            existingIndustryDetail = new IndustryDetail();
            existingIndustryDetail.setIndustryID(criteria.getIndustryID());
            existingIndustryDetail.setWebsite(criteria.getWebsite());
        } else {
            updateFlag = true;
            existingIndustryDetail.setUpdateTime(new Date());
        }
        existingIndustryDetail.setTelephone(criteria.getPhones().replace("[", "").replace("]", "").replaceAll("'", ""));
        existingIndustryDetail.setQq(criteria.getQqs().replace("[", "").replace("]", "").replaceAll("'", ""));
        existingIndustryDetail.setTitle(criteria.getTitle());
        existingIndustryDetail.setWeight(criteria.getWeight());
        existingIndustryDetail.setLevel(criteria.getLevel());
        String domain = criteria.getWebsite().replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
        String qzCustomerStr = qzSettingService.findQZCustomer(domain);
        if (null != qzCustomerStr) {
            String[] customerStr = qzCustomerStr.split("##");
            existingIndustryDetail.setIdentifyCustomer(customerStr[0] + "已有该网站客户" + customerStr[1]);
        }
        if (updateFlag) {
            industryDetailDao.updateById(existingIndustryDetail);
        } else {
            industryDetailDao.insert(existingIndustryDetail);
        }
    }

    @Override
    public int findIndustryDetailCount(long industryID) {
        return industryDetailDao.findIndustryDetailCount(industryID);
    }

    @Override
    public void removeUselessIndustryDetail(long industryID) {
        industryDetailDao.removeUselessIndustryDetail(industryID);
    }

    @Override
    public List<Map> getIndustryInfos(List<String> uuids) {
        List<Map> dataMap = new ArrayList<>();
        for (String uuid : uuids) {
            dataMap.addAll(industryDetailDao.getIndustryInfos(Long.valueOf(uuid)));
        }
        return dataMap;
    }
}
