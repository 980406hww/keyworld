package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.criteria.AdvertisingAllTypeAndCustomerListCriteria;
import com.keymanager.monitoring.criteria.AdvertisingCriteria;
import com.keymanager.monitoring.dao.AdvertisingDao;
import com.keymanager.monitoring.entity.Advertising;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.monitoring.enums.WebsiteRemoteConnectionEnum;
import com.keymanager.monitoring.vo.AdvertisingVO;
import com.keymanager.util.AESUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdvertisingService extends ServiceImpl<AdvertisingDao, Advertising> {

    @Autowired
    private WebsiteService websiteService;
    @Autowired
    private AdvertisingDao advertisingDao;

    public ModelAndView constructSearchAdvertisingListsModelAndView(int currentPageNumber, int pageSize, AdvertisingCriteria advertisingCriteria) {
        ModelAndView modelAndView = new ModelAndView("/advertising/advertising");
        Page<Advertising> page = new Page<>();
        page.setRecords(advertisingDao.searchAdvertisingListsPage(new Page<Advertising>(currentPageNumber, pageSize), advertisingCriteria));
        modelAndView.addObject("advertisingCriteria", advertisingCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    public Boolean saveAdvertising(Advertising advertising){
        if (saveOrUpdateConnectionCMS(advertising, WebsiteRemoteConnectionEnum.add.name())){
            advertisingDao.insert(advertising);
            return true;
        }else {
            return false;
        }
    }

    public Advertising getAdvertising(Long uuid) {
        return advertisingDao.getAdvertising(uuid);
    }

    public void delAdvertisings(Map map){
        Long websiteUuid = Long.valueOf((Integer)map.get("websiteUuid"));
        List<String> uuids = (List<String>) map.get("uuids");
        List<String> AdvertisingIds = advertisingDao.searchAdvertisingIds(websiteUuid, uuids);
        String[] uuidArrays = new String[AdvertisingIds.size()];
        AdvertisingIds.toArray(uuidArrays);
        deleteConnectionCMS(websiteUuid, uuidArrays);
        advertisingDao.deleteBatchIds(uuids);
    }

    public void delAdvertising(Long uuid){
        Advertising advertising = advertisingDao.selectById(uuid);
        String[] uuidArrays = {String.valueOf(advertising.getAdvertisingId())};
        deleteConnectionCMS(Long.valueOf(advertising.getWebsiteUuid()), uuidArrays);
        advertisingDao.deleteById(uuid);
    }

    public void updateAdvertising(Advertising advertising){
        advertising.setUpdateTime(new Date());
        saveOrUpdateConnectionCMS(advertising, WebsiteRemoteConnectionEnum.saveedit.name());
        advertisingDao.updateById(advertising);
    }

    public Boolean saveOrUpdateConnectionCMS(Advertising advertising, String type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Website website = websiteService.getWebsite(Long.valueOf(advertising.getWebsiteUuid()));
        Map requestMap = new HashedMap();
        requestMap.put("username", website.getBackgroundUserName());
        requestMap.put("password", website.getBackgroundPassword());
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
            JSONObject jsonObject = JSONObject.fromObject(connectionAdvertisingCMS(requestMap, WebsiteRemoteConnectionEnum.add.name(), website.getBackgroundDomain()));
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
            JSONObject jsonObject = JSONObject.fromObject(connectionAdvertisingCMS(requestMap, WebsiteRemoteConnectionEnum.saveedit.name(), website.getBackgroundDomain()));
            if (!"error".equals(jsonObject.get("status"))){
                advertising.setAdvertisingNormbody((String) jsonObject.get("normbody"));
            }
        }
        return true;
    }

    public void deleteConnectionCMS(Long websiteUuid, String[] uuids){
        Website website = websiteService.getWebsite(websiteUuid);
        Map requestMap = new HashMap();
        requestMap.put("username", website.getBackgroundUserName());
        requestMap.put("password", website.getBackgroundPassword());
        requestMap.put("aid", StringUtils.join(uuids, ","));
        requestMap.put("dopost", WebsiteRemoteConnectionEnum.delete.name());
        connectionAdvertisingCMS(requestMap, WebsiteRemoteConnectionEnum.delete.name(), website.getBackgroundDomain());
    }

    public List<AdvertisingVO> selectConnectionCMS(Long websiteUuid){
        Website website = websiteService.getWebsite(websiteUuid);
        Map requestMap = new HashMap();
        requestMap.put("username", website.getBackgroundUserName());
        requestMap.put("password", website.getBackgroundPassword());
        requestMap.put("dopost", WebsiteRemoteConnectionEnum.select.name());
        String resultJsonString = connectionAdvertisingCMS(requestMap,WebsiteRemoteConnectionEnum.select.name(), website.getBackgroundDomain());
        List<AdvertisingVO> advertisingVOS = new ArrayList<>();
        if (!"null".equals(resultJsonString)){
            JSONArray jsonArray = JSONArray.fromObject(resultJsonString);
            advertisingVOS = JSONArray.toList(jsonArray, new AdvertisingVO(), new JsonConfig());
        }
        return advertisingVOS;
    }

    public String connectionAdvertisingCMS(Map map, String type, String backgroundDomain){
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("data",  AESUtils.encrypt(JSONObject.fromObject(map)));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        if (backgroundDomain.length() > 7){
            backgroundDomain = "http://".equals(backgroundDomain.substring(0,7).toLowerCase()) ? backgroundDomain : "http://" + backgroundDomain;
        }else {
            backgroundDomain = "http://" + backgroundDomain;
        }
        if (WebsiteRemoteConnectionEnum.add.name().equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "ad_m_add.php",  requestMap, String.class);
        }else if (WebsiteRemoteConnectionEnum.select.name().equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "ad_m_main.php",  requestMap, String.class);
        }else {
            resultJsonString = restTemplate.postForObject(backgroundDomain + "ad_m_edit.php",  requestMap, String.class);
        }
        return resultJsonString;
    }

    public String connectionAdvertisingTypeCMS(Map map, String type, String backgroundDomain){
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("data", AESUtils.encrypt(JSONObject.fromObject(map)));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        if (backgroundDomain.length() > 7){
            backgroundDomain = "http://".equals(backgroundDomain.substring(0,7).toLowerCase()) ? backgroundDomain : "http://" + backgroundDomain;
        }else {
            backgroundDomain = "http://" + backgroundDomain;
        }
        if (WebsiteRemoteConnectionEnum.add.name().equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "adtype_m_add.php",  requestMap, String.class);
        }else if (WebsiteRemoteConnectionEnum.select.name().equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "adtype_m_main.php",  requestMap, String.class);
        }else {
            resultJsonString = restTemplate.postForObject(backgroundDomain + "adtype_m_edit.php",  requestMap, String.class);
        }
        return resultJsonString;
    }

    public String connectionAdvertisingArcTypeCMS(Map map, String backgroundDomain){
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("data", AESUtils.encrypt(JSONObject.fromObject(map)));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        if (backgroundDomain.length() > 7){
            backgroundDomain = "http://".equals(backgroundDomain.substring(0,7).toLowerCase()) ? backgroundDomain : "http://" + backgroundDomain;
        }else {
            backgroundDomain = "http://" + backgroundDomain;
        }
        resultJsonString = restTemplate.postForObject(backgroundDomain + "ad_m_arctype.php",  requestMap, String.class);
        return resultJsonString;
    }

    public AdvertisingAllTypeAndCustomerListCriteria searchAdvertisingAllTypeList(Long websiteUuid){
        Website website = websiteService.getWebsite(websiteUuid);
        Map requestMap = new HashMap();
        requestMap.put("username", website.getBackgroundUserName());
        requestMap.put("password", website.getBackgroundPassword());
        requestMap.put("dopost", WebsiteRemoteConnectionEnum.select.name());
        String advertisingTypeStr = connectionAdvertisingTypeCMS(requestMap, WebsiteRemoteConnectionEnum.select.name(), website.getBackgroundDomain());
        JSONArray advertisingTypeJsonArray = JSONArray.fromObject(advertisingTypeStr);
        List<Map> advertisingType = JSONArray.toList(advertisingTypeJsonArray, new HashedMap(), new JsonConfig());
        String advertisingArcTypeStr = connectionAdvertisingArcTypeCMS(requestMap, website.getBackgroundDomain());
        JSONArray advertisingArcTypeJsonArray = JSONArray.fromObject(advertisingArcTypeStr);
        List<Map> advertisingArcType = JSONArray.toList(advertisingArcTypeJsonArray, new HashedMap(), new JsonConfig());
        AdvertisingAllTypeAndCustomerListCriteria advertisingAllTypeAndCustomerListCriteria = new AdvertisingAllTypeAndCustomerListCriteria();
        advertisingAllTypeAndCustomerListCriteria.setAdvertisingType(advertisingType);
        advertisingAllTypeAndCustomerListCriteria.setAdvertisingArcType(advertisingArcType);
        return advertisingAllTypeAndCustomerListCriteria;
    }

    public void insertAdvertising(Advertising advertising){
        advertisingDao.insert(advertising);
    }

    public List<Map> searchIdByOriginalAdvertisingTagname(int websiteUuid, String originalAdvertisingTagname){
        return advertisingDao.searchIdByOriginalAdvertisingTagname(websiteUuid, originalAdvertisingTagname);
    }

    public Advertising getAdvertisingByAdvertisingTagname(int websiteUuid, String advertisingTagname ){
        return advertisingDao.getAdvertisingByAdvertisingTagname(websiteUuid, advertisingTagname);
    }

    public List<String> searchAdvertisingidsByAdvertisingTagname(Long websiteUuid, String advertisingTagname){
        return advertisingDao.searchAdvertisingidsByAdvertisingTagname(websiteUuid, advertisingTagname);
    }

    public void batchDeleteAdvertisingByAdvertisingTagname(String advertisingTagname, List<String> websiteUuids){
        advertisingDao.batchDeleteAdvertisingByAdvertisingTagname(advertisingTagname, websiteUuids);
    }

    public List<Integer> selectByWebsiteId(Long websiteUuid){
        return advertisingDao.selectByWebsiteId(websiteUuid);
    }

    public Long selectIdByAdvertisingId(Long websiteUuid, int advertisingId){
        return advertisingDao.selectIdByAdvertisingId(websiteUuid, advertisingId);
    }

    public void initSynchronousAdvertising(Advertising advertising, AdvertisingVO advertisingVO){
        advertising.setAdvertisingArcType(advertisingVO.getR_typename() + advertisingVO.getClsid());
        advertising.setAdvertisingType(advertisingVO.getTypename() + advertisingVO.getTypeid());
        advertising.setAdvertisingAdName(advertisingVO.getAdname());
        advertising.setAdvertisingTimeSet(advertisingVO.getTimeset());
        advertising.setAdvertisingStarttime(advertisingVO.getStarttime());
        advertising.setAdvertisingEndtime(advertisingVO.getEndtime());
        advertising.setAdvertisingNormbody(advertisingVO.getNormbody());
        advertising.setAdvertisingExpbody(advertisingVO.getExpbody());
    }

    public int searchAdvertisingCount(Long websiteUuid){
        return advertisingDao.searchAdvertisingCount(websiteUuid);
    }

    public void pushAdvertising(Map map){
        List<String> uuids = (List<String>) map.get("uuids");
        for (String uuid: uuids ) {
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
