package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.ScreenedWebsiteCriteria;
import com.keymanager.ckadmin.entity.ScreenedWebsite;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public interface ScreenedWebsiteService {

    Page<ScreenedWebsite> searchCustomerKeywordListsPage(Page<ScreenedWebsite> page, ScreenedWebsiteCriteria screenedWebsiteCriteria);

    Boolean saveScreenedWebsite(ScreenedWebsite screenedWebsite, String userName, String password);

    ScreenedWebsite getScreenedWebsite(Long uuid);

    Boolean delScreenedWebsite(Map<String, Object> map, String userName, String password);

    String getScreenedWebsiteByOptimizeGroupName(String optimizeGroupName);

    Boolean postScreenedWebsiteRequest(String optimizeGroupName, String userName, String password);
}
