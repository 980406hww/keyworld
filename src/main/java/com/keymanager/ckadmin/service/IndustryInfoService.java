package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.IndustryCriteria;
import com.keymanager.ckadmin.criteria.IndustryDetailCriteria;
import com.keymanager.ckadmin.entity.IndustryInfo;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IndustryInfoService extends IService<IndustryInfo> {

    Page<IndustryInfo> searchIndustries (Page<IndustryInfo> page, IndustryCriteria industryCriteria);

    IndustryInfo getIndustry (long uuid);

    void saveIndustryInfo (IndustryInfo industryInfo, String loginName);

    void delIndustryInfo (long uuid);

    void updateIndustryUserID (List<String> uuids, String userID);

    void deleteIndustries(List<String> uuids);

    void updateIndustryInfoDetail(IndustryDetailCriteria criteria);

    void updateIndustryStatus(List<String> uuids);

    void updateIndustryInfoStatus(long uuid);

    boolean handleExcel(InputStream inputStream, String excelType, String terminalType, String userName) throws Exception;

    List<Map> getIndustryInfos(List<String> uuids);

}
