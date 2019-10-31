package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.IndustryDetailCriteria;
import com.keymanager.ckadmin.entity.IndustryDetail;
import java.util.List;
import java.util.Map;

public interface IndustryDetailService extends IService<IndustryDetail> {

    void delIndustryDetailsByIndustryID (long industryID);

    Page<IndustryDetail> searchIndustryDetails(Page<IndustryDetail> page, IndustryDetailCriteria industryDetailCriteria);

    IndustryDetail getIndustryDetail(long uuid);

    void saveIndustryDetail(IndustryDetail industryDetail);

    void updRemarkByUuids(List<Integer> uuids,String remark);

    void delIndustryDetail(long uuid);

    void deleteIndustryDetails(List<String> uuids);

    void updateIndustryDetailRemark(long uuid, String remark);

    void updateIndustryInfoDetail(IndustryDetailCriteria criteria);

    int findIndustryDetailCount(long industryID);

    void removeUselessIndustryDetail(long industryID);

    List<Map> getIndustryInfos(List<String> uuids);
}
