package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.AdvertisingAllTypeAndCustomerListCriteria;
import com.keymanager.ckadmin.criteria.AdvertisingCriteria;
import com.keymanager.ckadmin.entity.Advertising;
import com.keymanager.ckadmin.vo.AdvertisingVO;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

public interface AdvertisingService extends IService<Advertising> {

    ModelAndView constructSearchAdvertisingListsModelAndView(int currentPageNumber, int pageSize, AdvertisingCriteria advertisingCriteria);

    Boolean saveAdvertising(Advertising advertising);

    Advertising getAdvertising(Long uuid);

    void delAdvertisings(Map map);

    void delAdvertising(Long uuid);

    Page<Advertising> searchAdvertisingList(Page<Advertising> page, AdvertisingCriteria advertisingCriteria);

    void updateAdvertising(Advertising advertising);

    Boolean saveOrUpdateConnectionCMS(Advertising advertising, String type);

    void deleteConnectionCMS(Long websiteUuid, String[] uuids);

    List<AdvertisingVO> selectConnectionCMS(Long websiteUuid);

    String connectionAdvertisingCMS(Map map, String type, String backendDomain);

    String connectionAdvertisingTypeCMS(Map map, String type, String backendDomain);

    String connectionAdvertisingArcTypeCMS(Map map, String backendDomain);

    AdvertisingAllTypeAndCustomerListCriteria searchAdvertisingAllTypeList(Long websiteUuid);

    void insertAdvertising(Advertising advertising);

    List<Map> searchIdByOriginalAdvertisingTagname(int websiteUuid, String originalAdvertisingTagname);

    Advertising getAdvertisingByAdvertisingTagname(int websiteUuid, String advertisingTagname );

    List<String> searchAdvertisingidsByAdvertisingTagname(Long websiteUuid, String advertisingTagname);

    void batchDeleteAdvertisingByAdvertisingTagname(String advertisingTagname, List<String> websiteUuids);

    List<Integer> selectByWebsiteId(Long websiteUuid);

    Long selectIdByAdvertisingId(Long websiteUuid, int advertisingId);

    void initSynchronousAdvertising(Advertising advertising, AdvertisingVO advertisingVO);

    int searchAdvertisingCount(Long websiteUuid);

    void pushAdvertising(Map map);
}
