package com.keymanager.ckadmin.service.impl;

import com.alibaba.dcm.DnsCacheManipulator;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.FriendlyLinkCriteria;
import com.keymanager.ckadmin.dao.FriendlyLinkDao;
import com.keymanager.ckadmin.entity.FriendlyLink;
import com.keymanager.ckadmin.entity.Website;
import com.keymanager.ckadmin.enums.WebsiteRemoteConnectionEnum;
import com.keymanager.ckadmin.service.FriendlyLinkService;
import com.keymanager.ckadmin.service.WebsiteService;
import com.keymanager.ckadmin.util.StringUtils;
import com.keymanager.ckadmin.vo.FriendlyLinkVO;
import com.keymanager.util.AESUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(value = "friendlyLinkService2")
public class FriendlyLinkServiceImpl extends ServiceImpl<FriendlyLinkDao, FriendlyLink> implements FriendlyLinkService {

    @Resource(name = "friendlyLinkDao2")
    private FriendlyLinkDao friendlyLinkDao;

    @Resource(name = "websiteService2")
    private WebsiteService websiteService;

    @Override
    public ModelAndView constructSearchFriendlyLinkListsModelAndView(int currentPageNumber, int pageSize, FriendlyLinkCriteria friendlyLinkCriteria) {
        ModelAndView modelAndView = new ModelAndView("/friendlyLink/friendlyLink");
        Page<FriendlyLink> page = new Page<>();
        page.setRecords(friendlyLinkDao.searchFriendlyLinkListsPage(new Page<FriendlyLink>(currentPageNumber, pageSize), friendlyLinkCriteria));
        modelAndView.addObject("friendlyLinkCriteria", friendlyLinkCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @Override
    public Page<FriendlyLink> searchFriendlyLinkList(Page<FriendlyLink> page, FriendlyLinkCriteria friendlyLinkCriteria) {
        page.setRecords(friendlyLinkDao.searchFriendlyLinkListsPage(page, friendlyLinkCriteria));
        return page;
    }

    @Override
    public void saveFriendlyLink(MultipartFile file, FriendlyLink friendlyLink){
        saveOrUpdateConnectionCMS(friendlyLink, file, WebsiteRemoteConnectionEnum.add.name());
        if (friendlyLink.getFriendlyLinkSortRank() != -1){
            friendlyLinkDao.retreatSortRank(friendlyLink.getWebsiteUuid(), friendlyLink.getFriendlyLinkSortRank());
        } else {
            friendlyLink.setFriendlyLinkSortRank(friendlyLinkDao.selectMaxSortRank(friendlyLink.getWebsiteUuid()) + 1);
        }
        insertFriendlyLink(friendlyLink);
    }

    @Override
    public FriendlyLink getFriendlyLink(Long uuid) {
        return friendlyLinkDao.getFriendlyLink(uuid);
    }

    @Override
    public void delFriendlyLinks(Map map){
        Long websiteUuid = Long.valueOf((String)map.get("websiteUuid"));
        List<String> uuids = (List<String>) map.get("uuids");
        List<String> friendlyLinkids = friendlyLinkDao.searchFriendlyLinkids(websiteUuid, uuids);
        String[] uuidArrays = new String[friendlyLinkids.size()];
        friendlyLinkids.toArray(uuidArrays);
        deleteConnectionCMS(websiteUuid, uuidArrays);
        friendlyLinkDao.deleteBatchIds(uuids);
    }

    @Override
    public void delFriendlyLink(Long uuid){
        FriendlyLink friendlyLink = friendlyLinkDao.selectById(uuid);
        String[] uuidArrays = {String.valueOf(friendlyLink.getFriendlyLinkId())};
        deleteConnectionCMS(Long.valueOf(friendlyLink.getWebsiteUuid()), uuidArrays);
        friendlyLinkDao.deleteById(uuid);
    }

    @Override
    public void updateFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, int originalSortRank){
        friendlyLink.setUpdateTime(new Date());
        saveOrUpdateConnectionCMS(friendlyLink, file, WebsiteRemoteConnectionEnum.saveedit.name());
        if (originalSortRank < friendlyLink.getFriendlyLinkSortRank()){
            friendlyLinkDao.updateCentreSortRank(originalSortRank, friendlyLink.getFriendlyLinkSortRank(), friendlyLink.getWebsiteUuid());
        }else {
            friendlyLinkDao.updateCentreSortRank(friendlyLink.getFriendlyLinkSortRank(),originalSortRank, friendlyLink.getWebsiteUuid());
        }
        friendlyLinkDao.updateById(friendlyLink);
    }

    @Override
    public void saveOrUpdateConnectionCMS(FriendlyLink friendlyLink, MultipartFile file, String type){
        Website website = websiteService.getWebsite((long) friendlyLink.getWebsiteUuid());
        Map requestMap = new HashedMap();
        requestMap.put("username", website.getBackendUserName());
        requestMap.put("password", website.getBackendPassword());
        String url = "";
        if (friendlyLink.getFriendlyLinkUrl().length() > 7){
            url = "http://".equals(friendlyLink.getFriendlyLinkUrl().substring(0,7).toLowerCase()) ? friendlyLink.getFriendlyLinkUrl() : "http://" + friendlyLink.getFriendlyLinkUrl();
        }else {
            url = "http://" + friendlyLink.getFriendlyLinkUrl();
        }
        friendlyLink.setFriendlyLinkUrl(url);
        requestMap.put("url", url);
        requestMap.put("webname", friendlyLink.getFriendlyLinkWebName());
        requestMap.put("sortrank", friendlyLink.getFriendlyLinkSortRank());
        if (file == null){
            if (!StringUtils.isEmpty(friendlyLink.getFriendlyLinkLogo())){
                requestMap.put("logo", friendlyLink.getFriendlyLinkLogo());
            }else {
                requestMap.put("logo", "");
            }
            requestMap.put("logoimg", "(binary)");
        }else {
            File tempFile = new File(file.getOriginalFilename());
            try {
                file.transferTo(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("上传文件失败");
            }
            FileSystemResource fileSystemResource = new FileSystemResource(tempFile);
            requestMap.put("logoimg", fileSystemResource);
        }
        requestMap.put("msg", friendlyLink.getFriendlyLinkMsg());
        requestMap.put("email", friendlyLink.getFriendlyLinkEmail());
        requestMap.put("typeid", friendlyLink.getFriendlyLinkTypeId());
        requestMap.put("ischeck", friendlyLink.getFriendlyLinkIsCheck());
        JSONObject jsonObject = new JSONObject();
        if (WebsiteRemoteConnectionEnum.add.name().equals(type)){
            requestMap.put("dopost", WebsiteRemoteConnectionEnum.add.name());
            if (website.getDnsAnalysisStatus() == 1){
                DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
                jsonObject = JSONObject.fromObject(connectionCMS(requestMap, WebsiteRemoteConnectionEnum.add.name(), website.getBackendDomain()));
                DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
            }else {
                jsonObject = JSONObject.fromObject(connectionCMS(requestMap, WebsiteRemoteConnectionEnum.add.name(), website.getBackendDomain()));
            }
            if (!"error".equals(jsonObject.get("status"))){
                friendlyLink.setFriendlyLinkId((Integer) jsonObject.get("id"));
                friendlyLink.setFriendlyLinkLogo((String) jsonObject.get("logo"));
            }
        }else {
            requestMap.put("dopost", WebsiteRemoteConnectionEnum.saveedit.name());
            requestMap.put("id", friendlyLink.getFriendlyLinkId());
            if (website.getDnsAnalysisStatus() == 1){
                DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
                jsonObject = JSONObject.fromObject(connectionCMS(requestMap, WebsiteRemoteConnectionEnum.saveedit.name(), website.getBackendDomain()));
                DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
            }else {
                jsonObject = JSONObject.fromObject(connectionCMS(requestMap, WebsiteRemoteConnectionEnum.saveedit.name(), website.getBackendDomain()));
            }
            if (!"error".equals(jsonObject.get("status")) && !(jsonObject.get("logo") instanceof JSONNull)){
                friendlyLink.setFriendlyLinkLogo((String) jsonObject.get("logo"));
            }
        }
    }

    @Override
    public void deleteConnectionCMS(Long websiteUuid, String[] uuids){
        Website website = websiteService.getWebsite(websiteUuid);
        Map requestMap = new HashedMap();
        requestMap.put("username", website.getBackendUserName());
        requestMap.put("password", website.getBackendPassword());
        requestMap.put("id", StringUtils.join(uuids, ","));
        requestMap.put("dopost", WebsiteRemoteConnectionEnum.delete.name());
        if (website.getDnsAnalysisStatus() == 1){
            DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
            connectionCMS(requestMap, WebsiteRemoteConnectionEnum.delete.name(), website.getBackendDomain());
            DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
        }else {
            connectionCMS(requestMap, WebsiteRemoteConnectionEnum.delete.name(), website.getBackendDomain());
        }
    }

    @Override
    public List<FriendlyLinkVO> selectConnectionCMS(Long websiteUuid){
        Website website = websiteService.getWebsite(websiteUuid);
        Map requestMap = new HashedMap();
        requestMap.put("username", website.getBackendUserName());
        requestMap.put("password", website.getBackendPassword());
        requestMap.put("dopost", WebsiteRemoteConnectionEnum.select.name());
        String resultJsonString = "";
        if (website.getDnsAnalysisStatus() == 1){
            DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
            resultJsonString = connectionCMS(requestMap,WebsiteRemoteConnectionEnum.select.name(), website.getBackendDomain());
            DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
        }else {
            resultJsonString = connectionCMS(requestMap,WebsiteRemoteConnectionEnum.select.name(), website.getBackendDomain());
        }
        List<FriendlyLinkVO> friendlyLinkVOS = new ArrayList<>();
        if (!"null".equals(resultJsonString)){
            JSONArray jsonArray = JSONArray.fromObject(resultJsonString);
            friendlyLinkVOS = JSONArray.toList(jsonArray, new FriendlyLinkVO(), new JsonConfig());
        }
        return friendlyLinkVOS;
    }

    @Override
    public String connectionCMS(Map map, String type, String backendDomain){
        MultiValueMap requestMap = new LinkedMultiValueMap();
        if (!StringUtils.isEmpty(map.get("logoimg")) && !"(binary)".equals(map.get("logoimg"))){
            requestMap.add("logoimg", map.get("logoimg"));
            map.remove("logoimg");
        }
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
            resultJsonString = restTemplate.postForObject(backendDomain + "friendlink_m_add.php",  requestMap, String.class);
        }else if (WebsiteRemoteConnectionEnum.select.name().equals(type)){
            resultJsonString = restTemplate.postForObject(backendDomain + "friendlink_m_select.php",  requestMap, String.class);
        }else {
            resultJsonString = restTemplate.postForObject(backendDomain + "friendlink_m_edit.php",  requestMap, String.class);
        }
        return resultJsonString;
    }

    @Override
    public FriendlyLink initFriendlyLink(HttpServletRequest request) throws Exception{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        FriendlyLink friendlyLink = new FriendlyLink();
        friendlyLink.setWebsiteUuid(Integer.valueOf(request.getParameter("websiteUuid")));
        friendlyLink.setCustomerUuid(Integer.valueOf(request.getParameter("customerUuid")));
        friendlyLink.setCustomerInfo(request.getParameter("customerInfo"));
        friendlyLink.setFriendlyLinkWebName(request.getParameter("friendlyLinkWebName"));
        friendlyLink.setFriendlyLinkUrl(request.getParameter("friendlyLinkUrl"));
        friendlyLink.setFriendlyLinkIsCheck(Integer.valueOf(request.getParameter("friendlyLinkIsCheck")));
        friendlyLink.setFriendlyLinkSortRank(Integer.valueOf(request.getParameter("friendlyLinkSortRank")));
        friendlyLink.setFriendlyLinkType(request.getParameter("friendlyLinkType"));
        friendlyLink.setFriendlyLinkTypeId(Integer.valueOf(request.getParameter("friendlyLinkTypeId")));
        friendlyLink.setFriendlyLinkMsg(request.getParameter("friendlyLinkMsg"));
        friendlyLink.setExpirationTime(format.parse(request.getParameter("expirationTime")));
        friendlyLink.setRenewTime(format.parse(request.getParameter("renewTime")));
        friendlyLink.setFriendlyLinkEmail(request.getParameter("friendlyLinkEmail"));
        if (!StringUtils.isEmpty(request.getParameter("uuid"))){
            friendlyLink.setUuid(Long.valueOf(request.getParameter("uuid")));
            friendlyLink.setFriendlyLinkId(Integer.valueOf(request.getParameter("friendlyLinkId")));
        }
        return friendlyLink;
    }

    @Override
    public List<Map> searchFriendlyLinkTypeList(Long websiteUuid){
        Website website = websiteService.selectById(websiteUuid);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        String backendDomain;
        if (website.getBackendDomain().length() > 7){
            backendDomain = "http://".equals(website.getBackendDomain().substring(0,7).toLowerCase()) ? website.getBackendDomain() : "http://" + website.getBackendDomain();
        }else {
            backendDomain = "http://" + website.getBackendDomain();
        }
        Map map = new HashedMap();
        map.put("username", website.getBackendUserName());
        map.put("password", website.getBackendPassword());
        map.put("dopost", WebsiteRemoteConnectionEnum.select.name());
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("data", AESUtils.encrypt(JSONObject.fromObject(map)));
        if (website.getDnsAnalysisStatus() == 1){
            DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
            resultJsonString = restTemplate.postForObject(backendDomain + "friendlink_m_type.php",  requestMap, String.class);
            DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
        }else {
            resultJsonString = restTemplate.postForObject(backendDomain + "friendlink_m_type.php",  requestMap, String.class);
        }
        JSONArray jsonArray = JSONArray.fromObject(resultJsonString);
        List<Map> friendlyLinkTypeList = JSONArray.toList(jsonArray, new HashedMap(), new JsonConfig());
        return friendlyLinkTypeList;
    }

    @Override
    public void insertFriendlyLink(FriendlyLink friendlyLink){
        friendlyLinkDao.insert(friendlyLink);
    }

    @Override
    public void retreatSortRank(int websiteUuid, int friendlyLinkSortRank){
        friendlyLinkDao.retreatSortRank(websiteUuid, friendlyLinkSortRank);
    }

    @Override
    public void updateFriendlyLinkById(FriendlyLink friendlyLink){
        friendlyLinkDao.updateById(friendlyLink);
    }

    @Override
    public void updateCentreSortRank(int beginSortRank, int endSortRank, int websiteUuid){
        friendlyLinkDao.updateCentreSortRank(beginSortRank, endSortRank, websiteUuid);
    }

    @Override
    public List<Map> searchOriginalSortRank(int websiteUuid, String originalFriendlyLinkUrl){
        return friendlyLinkDao.searchOriginalSortRank(websiteUuid, originalFriendlyLinkUrl);
    }

    @Override
    public List<String> searchFriendlyLinkidsByUrl(Long websiteUuid, String originalFriendlyLinkUrl){
        return friendlyLinkDao.searchFriendlyLinkidsByUrl(websiteUuid, originalFriendlyLinkUrl);
    }

    @Override
    public void batchDeleteFriendlyLinkByUrl(String friendlyLinkUrl, List<String> websiteUuids){
        friendlyLinkDao.batchDeleteFriendlyLinkByUrl(friendlyLinkUrl, websiteUuids);
    }

    @Override
    public FriendlyLink getFriendlyLinkByUrl(int websiteUuid, String friendlyLinkUrl){
        return friendlyLinkDao.getFriendlyLinkByUrl(websiteUuid, friendlyLinkUrl);
    }

    @Override
    public List<Integer> selectByWebsiteId(Long websiteUuid){
        return friendlyLinkDao.selectByWebsiteId(websiteUuid);
    }

    @Override
    public void initSynchronousFriendlyLink(FriendlyLink friendlyLink, FriendlyLinkVO friendlyLinkVO){
        friendlyLink.setFriendlyLinkSortRank(friendlyLinkVO.getSortrank());
        friendlyLink.setFriendlyLinkUrl(friendlyLinkVO.getUrl());
        friendlyLink.setFriendlyLinkWebName(friendlyLinkVO.getWebname());
        if (friendlyLinkVO.getTypeid() == 0){
            friendlyLink.setFriendlyLinkType("默认分类_" + friendlyLinkVO.getTypeid());
        }else {
            friendlyLink.setFriendlyLinkType(friendlyLinkVO.getTypename() + "_" + friendlyLinkVO.getTypeid());
        }
        friendlyLink.setFriendlyLinkTypeId(friendlyLinkVO.getTypeid());
        friendlyLink.setFriendlyLinkMsg(friendlyLinkVO.getMsg());
        friendlyLink.setFriendlyLinkEmail(friendlyLinkVO.getEmail());
        friendlyLink.setFriendlyLinkLogo(friendlyLinkVO.getLogo());
        friendlyLink.setFriendlyLinkDtime(friendlyLinkVO.getDtime());
        friendlyLink.setFriendlyLinkIsCheck(friendlyLinkVO.getIscheck());
    }

    @Override
    public Long selectIdByFriendlyLinkId(Long websiteUuid, int friendlyLinkId){
        return friendlyLinkDao.selectIdByFriendlyLinkId(websiteUuid, friendlyLinkId);
    }

    @Override
    public int searchFriendlyLinkCount(Long websiteUuid){
        return friendlyLinkDao.searchFriendlyLinkCount(websiteUuid);
    }

    @Override
    public void pushFriendlyLink(Map map){
        List<Integer> uuids = (List<Integer>) map.get("uuids");
        for (Integer uuid: uuids) {
            FriendlyLink friendlyLink = friendlyLinkDao.selectById(Long.valueOf(uuid));
            if (friendlyLink.getFriendlyLinkId() == 0){
                saveOrUpdateConnectionCMS(friendlyLink, null, WebsiteRemoteConnectionEnum.add.name());
            }else {
                saveOrUpdateConnectionCMS(friendlyLink, null, WebsiteRemoteConnectionEnum.saveedit.name());
            }
            friendlyLinkDao.updateById(friendlyLink);
        }
    }

    @Override
    public int selectMaxSortRank(int websiteUuid){
        return friendlyLinkDao.selectMaxSortRank(websiteUuid);
    }
}
