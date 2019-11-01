package com.keymanager.ckadmin.service.impl;

import com.alibaba.dcm.DnsCacheManipulator;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.AdvertisingAllTypeAndCustomerListCriteria;
import com.keymanager.ckadmin.criteria.AdvertisingCriteria;
import com.keymanager.ckadmin.dao.AdvertisingDao;
import com.keymanager.ckadmin.entity.Advertising;
import com.keymanager.ckadmin.entity.Website;
import com.keymanager.ckadmin.enums.WebsiteRemoteConnectionEnum;
import com.keymanager.ckadmin.service.AdvertisingService;
import com.keymanager.ckadmin.service.WebsiteService;
import com.keymanager.ckadmin.util.StringUtils;
import com.keymanager.ckadmin.vo.AdvertisingVO;
import com.keymanager.util.AESUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "advertisingService2")
public class AdvertisingServiceImpl extends ServiceImpl<AdvertisingDao, Advertising> implements AdvertisingService {

    @Resource(name="websiteService2")
    private WebsiteService websiteService;

    @Resource(name="advertisingDao2")
    private AdvertisingDao advertisingDao;

    @Override
    public ModelAndView constructSearchAdvertisingListsModelAndView(int currentPageNumber, int pageSize, AdvertisingCriteria advertisingCriteria) {
        ModelAndView modelAndView = new ModelAndView("/advertising/advertising");
        Page<Advertising> page = new Page<>();
        page.setRecords(advertisingDao.searchAdvertisingListsPage(new Page<Advertising>(currentPageNumber, pageSize), advertisingCriteria));
        modelAndView.addObject("advertisingCriteria", advertisingCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @Override
    public Boolean saveAdvertising(Advertising advertising){
        if (saveOrUpdateConnectionCMS(advertising, WebsiteRemoteConnectionEnum.add.name())){
            advertisingDao.insert(advertising);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Advertising getAdvertising(Long uuid) {
        return advertisingDao.getAdvertising(uuid);
    }

    @Override
    public void delAdvertisings(Map map){
        Long websiteUuid = Long.valueOf((String) map.get("websiteUuid"));
        List<String> uuids = (List<String>) map.get("uuids");
        List<String> AdvertisingIds = advertisingDao.searchAdvertisingIds(websiteUuid, uuids);
        String[] uuidArrays = new String[AdvertisingIds.size()];
        AdvertisingIds.toArray(uuidArrays);
        deleteConnectionCMS(websiteUuid, uuidArrays);
        advertisingDao.deleteBatchIds(uuids);
    }

    @Override
    public void delAdvertising(Long uuid){
        Advertising advertising = advertisingDao.selectById(uuid);
        String[] uuidArrays = {String.valueOf(advertising.getAdvertisingId())};
        deleteConnectionCMS(Long.valueOf(advertising.getWebsiteUuid()), uuidArrays);
        advertisingDao.deleteById(uuid);
    }

    @Override
    public Page<Advertising> searchAdvertisingList(Page<Advertising> page, AdvertisingCriteria advertisingCriteria){
        page.setRecords(advertisingDao.searchAdvertisingListsPage(page, advertisingCriteria));
        return page;
    }

    @Override
    public void updateAdvertising(Advertising advertising){
        advertising.setUpdateTime(new Date());
        saveOrUpdateConnectionCMS(advertising, WebsiteRemoteConnectionEnum.saveedit.name());
        advertisingDao.updateById(advertising);
    }

    @Override
    public Boolean saveOrUpdateConnectionCMS(Advertising advertising, String type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Website website = websiteService.getWebsite(Long.valueOf(advertising.getWebsiteUuid()));
        Map requestMap = new HashedMap();
        requestMap.put("username", website.getBackendUserName());
        requestMap.put("password", website.getBackendPassword());
        requestMap.put("adname", advertising.getAdvertisingAdName());
        requestMap.put("timeset", advertising.getAdvertisingTimeSet());
        requestMap.put("tagname", advertising.getAdvertisingTagname());
        requestMap.put("starttime", sdf.format(advertising.getAdvertisingStarttime()));
        requestMap.put("endtime", sdf.format(advertising.getAdvertisingEndtime()));
        requestMap.put("expbody", advertising.getAdvertisingExpbody());
        requestMap.put("typeid", advertising.getAdvertisingArcTypeId());
        requestMap.put("clsid", advertising.getAdvertisingTypeId());
        if (WebsiteRemoteConnectionEnum.add.name().equals(type)){
            requestMap.put("dopost", WebsiteRemoteConnectionEnum.save.name());
            requestMap.put("normbody", advertising.getNormbody());
            JSONObject jsonObject = new JSONObject();
            if (website.getDnsAnalysisStatus() == 1){
                DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
                jsonObject = JSONObject.fromObject(connectionAdvertisingCMS(requestMap, WebsiteRemoteConnectionEnum.add.name(), website.getBackendDomain()));
                DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
            }else {
                jsonObject = JSONObject.fromObject(connectionAdvertisingCMS(requestMap, WebsiteRemoteConnectionEnum.add.name(), website.getBackendDomain()));
            }
            if (!"error".equals(jsonObject.get("status"))){
                advertising.setAdvertisingId((Integer) jsonObject.get("id"));
                advertising.setAdvertisingNormbody((String) jsonObject.get("normbody"));
            }else {
                return false;
            }
        }else {
            requestMap.put("dopost", WebsiteRemoteConnectionEnum.saveedit.name());
            requestMap.put("aid", advertising.getAdvertisingId());
            requestMap.put("normbody", advertising.getAdvertisingNormbody());
            JSONObject jsonObject = new JSONObject();
            if (website.getDnsAnalysisStatus() == 1){
                DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
                jsonObject = JSONObject.fromObject(connectionAdvertisingCMS(requestMap, WebsiteRemoteConnectionEnum.saveedit.name(), website.getBackendDomain()));
                DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
            }else {
                jsonObject = JSONObject.fromObject(connectionAdvertisingCMS(requestMap, WebsiteRemoteConnectionEnum.saveedit.name(), website.getBackendDomain()));
            }
            if (!"error".equals(jsonObject.get("status"))){
                advertising.setAdvertisingNormbody((String) jsonObject.get("normbody"));
            }
        }
        return true;
    }

    @Override
    public void deleteConnectionCMS(Long websiteUuid, String[] uuids){
        Website website = websiteService.getWebsite(websiteUuid);
        Map requestMap = new HashMap();
        requestMap.put("username", website.getBackendUserName());
        requestMap.put("password", website.getBackendPassword());
        requestMap.put("aid", StringUtils.join(uuids, ","));
        requestMap.put("dopost", WebsiteRemoteConnectionEnum.delete.name());
        if (website.getDnsAnalysisStatus() == 1){
            DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
            connectionAdvertisingCMS(requestMap, WebsiteRemoteConnectionEnum.delete.name(), website.getBackendDomain());
            DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
        }else {
            connectionAdvertisingCMS(requestMap, WebsiteRemoteConnectionEnum.delete.name(), website.getBackendDomain());
        }
    }

    @Override
    public List<AdvertisingVO> selectConnectionCMS(Long websiteUuid){
        Website website = websiteService.getWebsite(websiteUuid);
        Map requestMap = new HashMap();
        requestMap.put("username", website.getBackendUserName());
        requestMap.put("password", website.getBackendPassword());
        requestMap.put("dopost", WebsiteRemoteConnectionEnum.select.name());
        String resultJsonString = "";
        if (website.getDnsAnalysisStatus() == 1){
            DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
            resultJsonString = connectionAdvertisingCMS(requestMap,WebsiteRemoteConnectionEnum.select.name(), website.getBackendDomain());
            DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
        }else {
            resultJsonString = connectionAdvertisingCMS(requestMap,WebsiteRemoteConnectionEnum.select.name(), website.getBackendDomain());
        }
        List<AdvertisingVO> advertisingVOS = new ArrayList<>();
        if (!"null".equals(resultJsonString)){
            JSONArray jsonArray = JSONArray.fromObject(resultJsonString);
            advertisingVOS = JSONArray.toList(jsonArray, new AdvertisingVO(), new JsonConfig());
        }
        return advertisingVOS;
    }

    @Override
    public String connectionAdvertisingCMS(Map map, String type, String backendDomain){
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("data",  AESUtils.encrypt(JSONObject.fromObject(map)));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        if (backendDomain.length() > 7){
            backendDomain = "http://".equals(backendDomain.substring(0,7).toLowerCase()) ? backendDomain : "http://" + backendDomain;
        }else {
            backendDomain = "http://" + backendDomain;
        }
        if (WebsiteRemoteConnectionEnum.add.name().equals(type)){
            resultJsonString = restTemplate.postForObject(backendDomain + "ad_m_add.php",  requestMap, String.class);
        }else if (WebsiteRemoteConnectionEnum.select.name().equals(type)){
            resultJsonString = restTemplate.postForObject(backendDomain + "ad_m_main.php",  requestMap, String.class);
        }else {
            resultJsonString = restTemplate.postForObject(backendDomain + "ad_m_edit.php",  requestMap, String.class);
        }
        return resultJsonString;
    }

    @Override
    public String connectionAdvertisingTypeCMS(Map map, String type, String backendDomain){
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("data", AESUtils.encrypt(JSONObject.fromObject(map)));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        if (backendDomain.length() > 7){
            backendDomain = "http://".equals(backendDomain.substring(0,7).toLowerCase()) ? backendDomain : "http://" + backendDomain;
        }else {
            backendDomain = "http://" + backendDomain;
        }
        if (WebsiteRemoteConnectionEnum.add.name().equals(type)){
            resultJsonString = restTemplate.postForObject(backendDomain + "adtype_m_add.php",  requestMap, String.class);
        }else if (WebsiteRemoteConnectionEnum.select.name().equals(type)){
            resultJsonString = restTemplate.postForObject(backendDomain + "adtype_m_main.php",  requestMap, String.class);
        }else {
            resultJsonString = restTemplate.postForObject(backendDomain + "adtype_m_edit.php",  requestMap, String.class);
        }
        return resultJsonString;
    }

    @Override
    public String connectionAdvertisingArcTypeCMS(Map map, String backendDomain){
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("data", AESUtils.encrypt(JSONObject.fromObject(map)));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        if (backendDomain.length() > 7){
            backendDomain = "http://".equals(backendDomain.substring(0,7).toLowerCase()) ? backendDomain : "http://" + backendDomain;
        }else {
            backendDomain = "http://" + backendDomain;
        }
        resultJsonString = restTemplate.postForObject(backendDomain + "ad_m_arctype.php",  requestMap, String.class);
        return resultJsonString;
    }

    @Override
    public AdvertisingAllTypeAndCustomerListCriteria searchAdvertisingAllTypeList(Long websiteUuid){
        Website website = websiteService.getWebsite(websiteUuid);
        Map requestMap = new HashMap();
        requestMap.put("username", website.getBackendUserName());
        requestMap.put("password", website.getBackendPassword());
        requestMap.put("dopost", WebsiteRemoteConnectionEnum.select.name());
        String advertisingTypeStr = "";
        if (website.getDnsAnalysisStatus() == 1){
            DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
            advertisingTypeStr = connectionAdvertisingTypeCMS(requestMap, WebsiteRemoteConnectionEnum.select.name(), website.getBackendDomain());
            DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
        }else {
            advertisingTypeStr = connectionAdvertisingTypeCMS(requestMap, WebsiteRemoteConnectionEnum.select.name(), website.getBackendDomain());
        }
        JSONArray advertisingTypeJsonArray = JSONArray.fromObject(advertisingTypeStr);
        List<Map> advertisingType = JSONArray.toList(advertisingTypeJsonArray, new HashedMap(), new JsonConfig());
        String advertisingArcTypeStr = connectionAdvertisingArcTypeCMS(requestMap, website.getBackendDomain());
        JSONArray advertisingArcTypeJsonArray = JSONArray.fromObject(advertisingArcTypeStr);
        List<Map> advertisingArcType = JSONArray.toList(advertisingArcTypeJsonArray, new HashedMap(), new JsonConfig());
        AdvertisingAllTypeAndCustomerListCriteria advertisingAllTypeAndCustomerListCriteria = new AdvertisingAllTypeAndCustomerListCriteria();
        advertisingAllTypeAndCustomerListCriteria.setAdvertisingType(advertisingType);
        advertisingAllTypeAndCustomerListCriteria.setAdvertisingArcType(advertisingArcType);
        return advertisingAllTypeAndCustomerListCriteria;
    }

    @Override
    public void insertAdvertising(Advertising advertising){
        advertisingDao.insert(advertising);
    }

    @Override
    public List<Map> searchIdByOriginalAdvertisingTagname(int websiteUuid, String originalAdvertisingTagname){
        return advertisingDao.searchIdByOriginalAdvertisingTagname(websiteUuid, originalAdvertisingTagname);
    }

    @Override
    public Advertising getAdvertisingByAdvertisingTagname(int websiteUuid, String advertisingTagname ){
        return advertisingDao.getAdvertisingByAdvertisingTagname(websiteUuid, advertisingTagname);
    }

    @Override
    public List<String> searchAdvertisingidsByAdvertisingTagname(Long websiteUuid, String advertisingTagname){
        return advertisingDao.searchAdvertisingidsByAdvertisingTagname(websiteUuid, advertisingTagname);
    }

    @Override
    public void batchDeleteAdvertisingByAdvertisingTagname(String advertisingTagname, List<String> websiteUuids){
        advertisingDao.batchDeleteAdvertisingByAdvertisingTagname(advertisingTagname, websiteUuids);
    }

    @Override
    public List<Integer> selectByWebsiteId(Long websiteUuid){
        return advertisingDao.selectByWebsiteId(websiteUuid);
    }

    @Override
    public Long selectIdByAdvertisingId(Long websiteUuid, int advertisingId){
        return advertisingDao.selectIdByAdvertisingId(websiteUuid, advertisingId);
    }

    @Override
    public void initSynchronousAdvertising(Advertising advertising, AdvertisingVO advertisingVO){
        advertising.setAdvertisingTagname(advertisingVO.getTagname());
        if (0 == advertisingVO.getClsid()){
            advertising.setAdvertisingArcType("默认分类_0");
        } else {
            advertising.setAdvertisingArcType(advertisingVO.getR_typename() + advertisingVO.getClsid());
        }
        if (0 == advertisingVO.getTypeid()){
            advertising.setAdvertisingType("没有同名标识所有栏目_0");
        } else {
            advertising.setAdvertisingType(advertisingVO.getTypename() + advertisingVO.getTypeid());
        }
        advertising.setAdvertisingAdName(advertisingVO.getAdname());
        advertising.setAdvertisingTimeSet(advertisingVO.getTimeset());
        advertising.setAdvertisingStarttime(advertisingVO.getStarttime());
        advertising.setAdvertisingEndtime(advertisingVO.getEndtime());
        advertising.setAdvertisingNormbody(advertisingVO.getNormbody());
        advertising.setAdvertisingExpbody(advertisingVO.getExpbody());
    }

    @Override
    public int searchAdvertisingCount(Long websiteUuid){
        return advertisingDao.searchAdvertisingCount(websiteUuid);
    }

    @Override
    public void pushAdvertising(Map map){
        List<Integer> uuids = (List<Integer>) map.get("uuids");
        for (Integer uuid: uuids ) {
            Advertising advertising = advertisingDao.selectById(Long.valueOf(uuid));
            if (advertising.getAdvertisingId() == 0){
                Map normBodyMap = new HashMap();
                normBodyMap.put("style", "code");
                normBodyMap.put("htmlcode", advertising.getAdvertisingNormbody());
                advertising.setNormbody(normBodyMap);
                saveOrUpdateConnectionCMS(advertising, WebsiteRemoteConnectionEnum.add.name());
            }else {
                saveOrUpdateConnectionCMS(advertising, WebsiteRemoteConnectionEnum.saveedit.name());
            }
            advertisingDao.updateById(advertising);
        }
    }
}
