package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.criteria.AdvertisingAllTypeAndCustomerListCriteria;
import com.keymanager.monitoring.criteria.AdvertisingCriteria;
import com.keymanager.monitoring.dao.AdvertisingDao;
import com.keymanager.monitoring.dao.FriendlyLinkDao;
import com.keymanager.monitoring.entity.Advertising;
import com.keymanager.monitoring.entity.FriendlyLink;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.monitoring.vo.AdvertisingVO;
import com.keymanager.monitoring.vo.FriendlyLinkVO;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public void saveAdvertising(Advertising advertising, String ip){
        saveOrUpdateConnectionCMS(advertising, ip, "add");
        advertisingDao.insert(advertising);
    }

    public Advertising getAdvertising(Long uuid) {
        return advertisingDao.getAdvertising(uuid);
    }

    public void delAdvertisings(Map map, String ip){
        Long websiteUuid = Long.valueOf((Integer)map.get("websiteUuid"));
        List<String> uuids = (List<String>) map.get("uuids");
        List<String> AdvertisingIds = advertisingDao.searchAdvertisingIds(websiteUuid, uuids);
        String[] uuidArrays = new String[AdvertisingIds.size()];
        AdvertisingIds.toArray(uuidArrays);
        deleteConnectionCMS(websiteUuid, uuidArrays, ip);
        advertisingDao.deleteBatchIds(uuids);
    }

    public void delAdvertising(Long uuid, String ip){
        Advertising advertising = advertisingDao.selectById(uuid);
        String[] uuidArrays = {String.valueOf(advertising.getAdvertisingId())};
        deleteConnectionCMS(Long.valueOf(advertising.getWebsiteUuid()), uuidArrays, ip);
        advertisingDao.deleteById(uuid);
    }

    public void updateAdvertising(Advertising advertising, String ip){
        advertising.setUpdateTime(new Date());
        saveOrUpdateConnectionCMS(advertising, ip, "saveedit");
        advertisingDao.updateById(advertising);
    }

    public void saveOrUpdateConnectionCMS(Advertising advertising, String ip, String type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Website website = websiteService.getWebsite(Long.valueOf(advertising.getWebsiteUuid()));
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("username", website.getBackgroundUserName());
        requestMap.add("password", website.getBackgroundPassword());
        requestMap.add("ip", ip);
        requestMap.add("adname", advertising.getAdvertisingAdName());
        requestMap.add("timeset", advertising.getAdvertisingTimeSet());
        requestMap.add("starttime", sdf.format(advertising.getAdvertisingStarttime()));
        requestMap.add("endtime", sdf.format(advertising.getAdvertisingEndtime()));
        requestMap.add("expbody", advertising.getAdvertisingExpbody());
        requestMap.add("typeid", advertising.getAdvertisingArcTypeId());
        requestMap.add("clsid", advertising.getAdvertisingTypeId());
        if ("add".equals(type)){
            requestMap.add("dopost", "save");
            requestMap.add("normbody", advertising.getNormbody());
            JSONObject jsonObject = JSONObject.fromObject(connectionAdvertisingCMS(requestMap, "add", website.getBackgroundDomain()));
            if (!"error".equals(jsonObject.get("status"))){
                advertising.setAdvertisingId((Integer) jsonObject.get("id"));
                advertising.setAdvertisingNormbody((String) jsonObject.get("normbody"));
            }
        }else {
            requestMap.add("dopost", "saveedit");
            requestMap.add("aid", advertising.getAdvertisingId());
            requestMap.add("normbody", advertising.getAdvertisingNormbody());
            JSONObject jsonObject = JSONObject.fromObject(connectionAdvertisingCMS(requestMap, "saveedit", website.getBackgroundDomain()));
            if (!"error".equals(jsonObject.get("status"))){
                advertising.setAdvertisingNormbody((String) jsonObject.get("normbody"));
            }
        }
    }

    public void deleteConnectionCMS(Long websiteUuid, String[] uuids, String ip){
        Website website = websiteService.getWebsite(websiteUuid);
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("username", website.getBackgroundUserName());
        requestMap.add("password", website.getBackgroundPassword());
        requestMap.add("ip", ip);
        requestMap.add("aid", StringUtils.join(uuids, ","));
        requestMap.add("dopost", "delete");
        connectionAdvertisingCMS(requestMap, "delete", website.getBackgroundDomain());
    }

    public List<AdvertisingVO> selectConnectionCMS(Long websiteUuid, String ip){
        Website website = websiteService.getWebsite(websiteUuid);
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("username", website.getBackgroundUserName());
        requestMap.add("password", website.getBackgroundPassword());
        requestMap.add("ip", ip);
        requestMap.add("dopost", "select");
        String resultJsonString = connectionAdvertisingCMS(requestMap,"select", website.getBackgroundDomain());
        JSONArray jsonArray = JSONArray.fromObject(resultJsonString);
        List<AdvertisingVO> advertisingVOS = JSONArray.toList(jsonArray, new AdvertisingVO(), new JsonConfig());
        return advertisingVOS;
    }

    public String connectionAdvertisingCMS(MultiValueMap requestMap, String type, String backgroundDomain){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        backgroundDomain = "http://".equals(backgroundDomain.substring(0,7).toLowerCase()) ? backgroundDomain : "http://" + backgroundDomain;
        if ("add".equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "ad_m_add.php",  requestMap, String.class);
        }else if ("select".equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "ad_m_main.php",  requestMap, String.class);
        }else {
            resultJsonString = restTemplate.postForObject(backgroundDomain + "ad_m_edit.php",  requestMap, String.class);
        }
        return resultJsonString;
    }

    public String connectionAdvertisingTypeCMS(MultiValueMap requestMap, String type, String backgroundDomain){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        backgroundDomain = "http://".equals(backgroundDomain.substring(0,7).toLowerCase()) ? backgroundDomain : "http://" + backgroundDomain;
        if ("add".equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "adtype_m_add.php",  requestMap, String.class);
        }else if ("select".equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "adtype_m_main.php",  requestMap, String.class);
        }else {
            resultJsonString = restTemplate.postForObject(backgroundDomain + "adtype_m_edit.php",  requestMap, String.class);
        }
        return resultJsonString;
    }

    public String connectionAdvertisingArcTypeCMS(MultiValueMap requestMap, String backgroundDomain){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        backgroundDomain = "http://".equals(backgroundDomain.substring(0,7).toLowerCase()) ? backgroundDomain : "http://" + backgroundDomain;
        resultJsonString = restTemplate.postForObject(backgroundDomain + "ad_m_arctype.php",  requestMap, String.class);
        return resultJsonString;
    }

    public AdvertisingAllTypeAndCustomerListCriteria searchAdvertisingAllTypeList(Long websiteUuid, String ip){
        Website website = websiteService.getWebsite(websiteUuid);
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("username", website.getBackgroundUserName());
        requestMap.add("password", website.getBackgroundPassword());
        requestMap.add("ip", ip);
        requestMap.add("dopost", "select");
        String advertisingTypeStr = connectionAdvertisingTypeCMS(requestMap, "select", website.getBackgroundDomain());
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
        advertising.setAdvertisingArcType("" + advertisingVO.getClsid());
        advertising.setAdvertisingType("" + advertisingVO.getTypeid());
        advertising.setAdvertisingAdName(advertisingVO.getAdname());
        advertising.setAdvertisingTimeSet(advertisingVO.getTimeset());
        advertising.setAdvertisingStarttime(advertisingVO.getStarttime());
        advertising.setAdvertisingEndtime(advertisingVO.getEndtime());
        advertising.setAdvertisingNormbody(advertisingVO.getNormbody());
        advertising.setAdvertisingExpbody(advertisingVO.getExpbody());
    }
}
