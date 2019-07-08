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
        return page.setRecords(industryDetailDao.searchIndustryDetails(page, industryDetailCriteria));
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
        if (criteria.getPhones().size() > 0) {
            for (String telephone : criteria.getPhones()) {
                existingIndustryDetail.setTelephone(telephone + ",");
            }
            existingIndustryDetail.setTelephone(existingIndustryDetail.getTelephone().substring(0, existingIndustryDetail.getTelephone().length() - 1));
        } else {
            existingIndustryDetail.setTelephone("");
        }
        if (criteria.getQqs().size() > 0) {
            for (String qq : criteria.getQqs()) {
                existingIndustryDetail.setQq(qq);
            }
            existingIndustryDetail.setQq(existingIndustryDetail.getQq().substring(0, existingIndustryDetail.getQq().length() - 1));
        } else {
            existingIndustryDetail.setQq("");
        }
        existingIndustryDetail.setWeight(criteria.getWeight());
        existingIndustryDetail.setLevel(criteria.getLevel());
        if (updateFlag) {
            industryDetailDao.updateById(existingIndustryDetail);
        } else {
            industryDetailDao.insert(existingIndustryDetail);
        }
    }
}
