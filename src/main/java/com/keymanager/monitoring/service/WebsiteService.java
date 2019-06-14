package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.WebsiteCriteria;
import com.keymanager.monitoring.dao.WebsiteDao;
import com.keymanager.monitoring.entity.Advertising;
import com.keymanager.monitoring.entity.FriendlyLink;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.monitoring.vo.AdvertisingVO;
import com.keymanager.monitoring.vo.FriendlyLinkVO;
import com.keymanager.monitoring.vo.WebsiteVO;
import com.keymanager.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shunshikj08 on 2017/12/14.
 */
@Service
public class WebsiteService  extends ServiceImpl<WebsiteDao, Website> {
    private static Logger logger = LoggerFactory.getLogger(WebsiteService.class);

    @Autowired
    private WebsiteDao websiteDao;

    @Autowired
    private KeywordInfoService keywordInfoService;

    @Autowired
    private FriendlyLinkService friendlyLinkService;

    @Autowired
    private AdvertisingService advertisingService;

    public Page<WebsiteVO> searchWebsites(Page<WebsiteVO> page, WebsiteCriteria websiteCriteria) {
        page.setRecords(websiteDao.searchWebsites(page, websiteCriteria));
        return page;
    }

    public void saveWebsite(Website website) {
        website.setUpdateTime(new Date());
        if (null != website.getUuid()) {
            websiteDao.updateById(website);
        } else {
            websiteDao.insert(website);
        }
    }

    public Website getWebsite(Long uuid) {
        return websiteDao.selectById(uuid);
    }

    public void deleteWebsite(Long uuid) {
        websiteDao.deleteById(uuid);
    }

    public void deleteWebsites(List<String> uuids) {
        for (String uuid : uuids) {
            deleteWebsite(Long.parseLong(uuid));
        }
    }

    public void resetWebsiteAccessFailCount(List<String> uuids) {
        for (String uuid : uuids) {
            Website website = websiteDao.selectById(Long.parseLong(uuid));
            website.setAccessFailCount(0);
            website.setUpdateTime(new Date());
            websiteDao.updateById(website);
        }
    }

    public Map<String, Object> accessURL() {
        Map<String, Object> dataBase = new HashMap<String, Object>();
        List<Website> accessFailWebsites = new ArrayList<Website>();
        List<Website> accessSuccessWebsites = new ArrayList<Website>();
        List<Website> websites = websiteDao.takeWebsitesForAccess();
        for (Website website : websites) {
            try {
                String address = "http://" + website.getDomain() + "?" + System.currentTimeMillis();
                int status = 404;
                URL url = new URL(address);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setConnectTimeout(5000);
                status = httpURLConnection.getResponseCode();
                if (200 == status) {
                    if(website.getAccessFailCount() > 0){
                        accessSuccessWebsites.add(website);
                    }
                    website.setAccessFailTime(null);
                    website.setAccessFailCount(0);
                } else {
                    recordAccessFailInfo(website, accessFailWebsites);
                }
            } catch (IOException e) {
                recordAccessFailInfo(website, accessFailWebsites);
            } finally {
                website.setLastAccessTime(new Date());
                websiteDao.updateById(website);
            }
        }
        dataBase.put("accessFailWebsites",accessFailWebsites);
        dataBase.put("accessSuccessWebsites",accessSuccessWebsites);
        return dataBase;
    }

    private void recordAccessFailInfo(Website website, List<Website> accessFailWebsites) {
        Integer accessFailCount = website.getAccessFailCount();
        if(accessFailCount == 0) {
            website.setAccessFailTime(new Date());
        }
        website.setAccessFailCount(accessFailCount + 1);
        websiteDao.updateById(website);
        if(accessFailCount > 1 && Utils.isPower(accessFailCount)) {
            accessFailWebsites.add(website);
        }
    }

    public List<Website> accessExpireTimeURL() {
        List<Website> websites = websiteDao.searchExpireTime();
        return websites;
    }

    public FriendlyLink initFriendlyLink(HttpServletRequest request) throws Exception{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        FriendlyLink friendlyLink = new FriendlyLink();
        friendlyLink.setCustomerUuid(Integer.valueOf(request.getParameter("customerUuid")));
        friendlyLink.setCustomerInfo(request.getParameter("customerInfo"));
        friendlyLink.setFriendlyLinkWebName(request.getParameter("friendlyLinkWebName"));
        friendlyLink.setFriendlyLinkUrl(request.getParameter("friendlyLinkUrl"));
        friendlyLink.setFriendlyLinkIsCheck(Integer.valueOf(request.getParameter("friendlyLinkIsCheck")));
        friendlyLink.setFriendlyLinkSortRank(Integer.valueOf(request.getParameter("friendlyLinkSortRank")));
        friendlyLink.setFriendlyLinkType(request.getParameter("friendlyLinkType"));
        friendlyLink.setFriendlyLinkMsg(request.getParameter("friendlyLinkMsg"));
        friendlyLink.setExpirationTime(format.parse(request.getParameter("expirationTime")));
        friendlyLink.setFriendlyLinkEmail(request.getParameter("friendlyLinkEmail"));
        return friendlyLink;
    }

    public void batchSaveFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, String ip, List<String> uuids){
        for (String uuidstr : uuids) {
            friendlyLink.setWebsiteUuid(Integer.valueOf(uuidstr));
            friendlyLinkService.saveOrUpdateConnectionCMS(friendlyLink, file, ip, "add");
            if (friendlyLink.getFriendlyLinkSortRank() != -1){
                friendlyLinkService.retreatSortRank(friendlyLink.getWebsiteUuid(), friendlyLink.getFriendlyLinkSortRank());
            }
            friendlyLinkService.insertFriendlyLink(friendlyLink);
        }
    }

    public void batchUpdateFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, String ip, List<String> uuids, String originalFriendlyLinkUrl){
        for (String uuidstr : uuids) {
            List<Map> friendlyLinkInfos = friendlyLinkService.searchOriginalSortRank(Integer.valueOf(uuidstr), originalFriendlyLinkUrl);
            for (Map friendlyLinkInfo: friendlyLinkInfos) {
                friendlyLink.setWebsiteUuid(Integer.valueOf(uuidstr));
                friendlyLink.setUuid(Long.valueOf((Integer)friendlyLinkInfo.get("uuid")));
                friendlyLink.setUpdateTime(new Date());
                friendlyLinkService.saveOrUpdateConnectionCMS(friendlyLink, file, ip, "saveedit");
                int originalSortRank = (Integer) friendlyLinkInfo.get("friendlyLinkSortRank");
                if (originalSortRank < friendlyLink.getFriendlyLinkSortRank()){
                    friendlyLinkService.updateCentreSortRank(originalSortRank, friendlyLink.getFriendlyLinkSortRank(), friendlyLink.getWebsiteUuid());
                }else {
                    friendlyLinkService.updateCentreSortRank(friendlyLink.getFriendlyLinkSortRank(),originalSortRank, friendlyLink.getWebsiteUuid());
                }
                friendlyLinkService.updateFriendlyLinkById(friendlyLink);
            }
        }
    }

    public void batchDelFriendlyLink(Map map, String ip){
        String friendlyLinkUrl = (String) map.get("friendlyLinkUrl");
        List<String> uuids = (List<String>) map.get("uuids");
        for (String uuidStr: uuids) {
            Long uuid = Long.valueOf(uuidStr);
            List<String> friendlyLinkids = friendlyLinkService.searchFriendlyLinkidsByUrl(uuid, friendlyLinkUrl);
            String[] uuidArrays = new String[friendlyLinkids.size()];
            friendlyLinkids.toArray(uuidArrays);
            friendlyLinkService.deleteConnectionCMS(uuid, uuidArrays, ip);
        }
        friendlyLinkService.batchDeleteFriendlyLinkByUrl(friendlyLinkUrl, uuids);
    }

    public void batchSaveAdvertising(Advertising advertising, String ip){
        List<String> uuids = Arrays.asList(advertising.getUuids().split(","));
        for (String uuidstr : uuids) {
            advertising.setWebsiteUuid(Integer.valueOf(uuidstr));
            advertisingService.saveOrUpdateConnectionCMS(advertising, ip, "add");
            advertisingService.insertAdvertising(advertising);
        }
    }

    public void batchUpdateAdvertising(Advertising advertising, String ip){
        List<String> uuids = Arrays.asList(advertising.getUuids().split(","));
        for (String uuidStr : uuids) {
            advertising.setWebsiteUuid(Integer.valueOf(uuidStr));
            List<Map> advertisingIds =  advertisingService.searchIdByOriginalAdvertisingTagname(Integer.valueOf(uuidStr), advertising.getOriginalAdvertisingTagname());
            for (Map advertisingIdMap: advertisingIds) {
                advertising.setAdvertisingId((Integer) advertisingIdMap.get("advertisingId"));
                advertising.setUuid(Long.valueOf((Integer)advertisingIdMap.get("uuid")));
                advertisingService.updateAdvertising(advertising, ip);
            }
        }
    }

    public void batchDelAdvertising(Map map, String ip){
        String advertisingTagname = (String) map.get("advertisingTagname");
        List<String> uuids = (List<String>) map.get("uuids");
        for (String uuidStr: uuids) {
            Long uuid = Long.valueOf(uuidStr);
            List<String> advertisingIds = advertisingService.searchAdvertisingidsByAdvertisingTagname(uuid, advertisingTagname);
            String[] uuidArrays = new String[advertisingIds.size()];
            advertisingIds.toArray(uuidArrays);
            advertisingService.deleteConnectionCMS(uuid, uuidArrays, ip);
        }
        advertisingService.batchDeleteAdvertisingByAdvertisingTagname(advertisingTagname, uuids);
    }

    public void synchronousFriendlyLink(Map<String, Object> requestMap, String ip){
        List<String> uuids = (List) requestMap.get("uuids");
        for (String uuidStr: uuids){
            try {
                List<FriendlyLinkVO> friendlyLinkVOS = friendlyLinkService.selectConnectionCMS(Long.valueOf(uuidStr), ip);
                if(!friendlyLinkVOS.isEmpty()){
                    List<Integer> friendlyLinkIds = friendlyLinkService.selectByWebsiteId(Long.valueOf(uuidStr));
                    for (FriendlyLinkVO friendlyLinkVO: friendlyLinkVOS) {
                        FriendlyLink friendlyLink = new FriendlyLink();
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        friendlyLink.setCustomerUuid(Integer.valueOf((String) requestMap.get("customerUuid")));
                        friendlyLink.setFriendlyLinkId(friendlyLinkVO.getId());
                        friendlyLink.setExpirationTime(format.parse((String) requestMap.get("expirationTime")));
                        friendlyLink.setRenewTime(format.parse((String) requestMap.get("renewTime")));
                        friendlyLink.setWebsiteUuid(Integer.valueOf(uuidStr));
                        friendlyLinkService.initSynchronousFriendlyLink(friendlyLink, friendlyLinkVO);
                        if (friendlyLinkIds.contains(friendlyLinkVO.getId())){
                            friendlyLink.setUuid(friendlyLinkService.selectIdByFriendlyLinkId(Long.valueOf(uuidStr), friendlyLinkVO.getId()));
                            friendlyLinkService.updateById(friendlyLink);
                        }else {
                            friendlyLinkService.insertFriendlyLink(friendlyLink);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public void synchronousAdvertising(Map<String, Object> requestMap, String ip){
        List<String> uuids = (List) requestMap.get("uuids");
        for (String uuidStr: uuids){
            try {
                List<AdvertisingVO> advertisingVOS = advertisingService.selectConnectionCMS(Long.valueOf(uuidStr), ip);
                if(!advertisingVOS.isEmpty()){
                    List<Integer> advertisingIds = advertisingService.selectByWebsiteId(Long.valueOf(uuidStr));
                    for (AdvertisingVO advertisingVO: advertisingVOS) {
                        Advertising advertising = new Advertising();
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        advertising.setRenewTime(format.parse((String) requestMap.get("renewTime")));
                        advertising.setCustomerUuid(Integer.valueOf((String) requestMap.get("customerUuid")));
                        advertising.setAdvertisingId(advertisingVO.getAid());
                        advertising.setWebsiteUuid(Integer.valueOf(uuidStr));
                        advertisingService.initSynchronousAdvertising(advertising, advertisingVO);
                        if (advertisingIds.contains(advertisingVO.getAid())){
                            advertising.setUuid(advertisingService.selectIdByAdvertisingId(Long.valueOf(uuidStr), advertisingVO.getAid()));
                            advertisingService.updateById(advertising);
                        }else {
                            advertisingService.insertAdvertising(advertising);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
