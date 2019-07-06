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
import java.util.List;

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
            industryDetailDao.insert(industryDetail);
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
}
