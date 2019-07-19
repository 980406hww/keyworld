package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.IndustryDetailCriteria;
import com.keymanager.monitoring.dao.IndustryDetailDao;
import com.keymanager.monitoring.entity.IndustryDetail;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * 行业详情表 服务实现类
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
@Service
public class IndustryDetailService extends ServiceImpl<IndustryDetailDao, IndustryDetail>  {

    @Autowired
    private IndustryDetailDao industryDetailDao;

    public void delIndustryDetailsByIndustryID (long industryID) {
        industryDetailDao.delIndustryDetailsByIndustryID(industryID);
    }

    public Page<IndustryDetail> searchIndustryDetails(Page<IndustryDetail> page, IndustryDetailCriteria industryDetailCriteria) {
        page.setRecords(industryDetailDao.searchIndustryDetails(page, industryDetailCriteria));
        this.wrapperForIndustryDetail(page);
        return page;
    }

    private void wrapperForIndustryDetail(Page<IndustryDetail> page) {
        for (IndustryDetail industryDetail : page.getRecords()) {
            if (null != industryDetail.getQq() && !"".equals(industryDetail.getQq())) {
                industryDetail.setQq(industryDetail.getQq().replaceAll(",", "<br>"));
            }
            if (null != industryDetail.getTelephone() && !"".equals(industryDetail.getTelephone())) {
                industryDetail.setTelephone(industryDetail.getTelephone().replaceAll(",", "<br>"));
            }
        }
    }

    public IndustryDetail getIndustryDetail(long uuid) {
        return industryDetailDao.selectById(uuid);
    }

    public void saveIndustryDetail(IndustryDetail industryDetail) {
        if (null == industryDetail.getUuid()) {
            IndustryDetail existingIndustryDetail = industryDetailDao.findExistingIndustryDetail(industryDetail.getIndustryID(),
                    industryDetail.getWebsite());
            if (null == existingIndustryDetail) {
                industryDetailDao.insert(industryDetail);
            }
        } else {
            industryDetail.setUpdateTime(new Date());
            industryDetailDao.updateById(industryDetail);
        }
    }

    public void delIndustryDetail(long uuid) {
        industryDetailDao.deleteById(uuid);
    }

    public void deleteIndustryDetails(String uuids) {
        industryDetailDao.deleteIndustryDetails(Arrays.asList(uuids.split(",")));
    }

    public void updateIndustryDetailRemark(long uuid, String remark) {
        industryDetailDao.updateIndustryDetailRemark(uuid, remark);
    }

    public void updateIndustryInfoDetail(IndustryDetailCriteria criteria) {
        IndustryDetail existingIndustryDetail = industryDetailDao.findExistingIndustryDetail(criteria.getIndustryID(),
                criteria.getWebsite());
        boolean updateFlag = false;
        if (null == existingIndustryDetail) {
            existingIndustryDetail = new IndustryDetail();
            existingIndustryDetail.setIndustryID(criteria.getIndustryID());
            existingIndustryDetail.setWebsite(criteria.getWebsite());
        } else {
            updateFlag = true;
            existingIndustryDetail.setUpdateTime(new Date());
        }
        existingIndustryDetail.setTelephone(criteria.getPhones().replace("[", "")
                .replace("]", "").replaceAll("'", ""));
        existingIndustryDetail.setQq(criteria.getQqs().replace("[", "")
                .replace("]", "").replaceAll("'", ""));
        existingIndustryDetail.setTitle(criteria.getTitle());
        existingIndustryDetail.setWeight(criteria.getWeight());
        existingIndustryDetail.setLevel(criteria.getLevel());
        if (updateFlag) {
            industryDetailDao.updateById(existingIndustryDetail);
        } else {
            industryDetailDao.insert(existingIndustryDetail);
        }
    }

    public int findIndustryDetailCount(long industryID) {
        return industryDetailDao.findIndustryDetailCount(industryID);
    }

    public void removeUselessIndustryDetail(long industryID) {
        industryDetailDao.removeUselessIndustryDetail(industryID);
    }
}
