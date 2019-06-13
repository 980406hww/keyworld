package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.criteria.FriendlyLinkCriteria;
import com.keymanager.monitoring.dao.FriendlyLinkDao;
import com.keymanager.monitoring.entity.FriendlyLink;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.monitoring.vo.FriendlyLinkVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FriendlyLinkService extends ServiceImpl<FriendlyLinkDao, FriendlyLink> {

    @Autowired
    private FriendlyLinkDao friendlyLinkDao;
    @Autowired
    private WebsiteService websiteService;

    public ModelAndView constructSearchFriendlyLinkListsModelAndView(int currentPageNumber, int pageSize, FriendlyLinkCriteria friendlyLinkCriteria) {
        ModelAndView modelAndView = new ModelAndView("/friendlyLink/friendlyLink");
        Page<FriendlyLink> page = new Page<>();
        page.setRecords(friendlyLinkDao.searchFriendlyLinkListsPage(new Page<FriendlyLink>(currentPageNumber, pageSize), friendlyLinkCriteria));
        modelAndView.addObject("friendlyLinkCriteria", friendlyLinkCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    public void saveFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, String ip){
        saveOrUpdateConnectionCMS(friendlyLink, file, ip, "add");
        if (friendlyLink.getFriendlyLinkSortRank() != -1){
            friendlyLinkDao.retreatSortRank(friendlyLink.getWebsiteUuid(), friendlyLink.getFriendlyLinkSortRank());
        }
        friendlyLinkDao.insertFriendlyLink(friendlyLink);
    }

    public FriendlyLink getFriendlyLink(Long uuid) {
        return friendlyLinkDao.getFriendlyLink(uuid);
    }

    public void delFriendlyLinks(Map map, String ip){
        Long websiteUuid = Long.valueOf((Integer)map.get("websiteUuid"));
        List<String> uuids = (List<String>) map.get("uuids");
        List<String> friendlyLinkids = friendlyLinkDao.searchFriendlyLinkids(websiteUuid, uuids);
        String[] uuidArrays = new String[friendlyLinkids.size()];
        friendlyLinkids.toArray(uuidArrays);
        deleteConnectionCMS(websiteUuid, uuidArrays, ip);
        friendlyLinkDao.deleteBatchIds(uuids);
    }

    public void delFriendlyLink(Long uuid, String ip){
        FriendlyLink friendlyLink = friendlyLinkDao.selectById(uuid);
        String[] uuidArrays = {String.valueOf(friendlyLink.getFriendlyLinkId())};
        deleteConnectionCMS(Long.valueOf(friendlyLink.getWebsiteUuid()), uuidArrays, ip);
        friendlyLinkDao.deleteById(uuid);
    }


    public void updateFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, String ip, int originalSortRank){
        friendlyLink.setUpdateTime(new Date());
        saveOrUpdateConnectionCMS(friendlyLink, file, ip, "saveedit");
        if (originalSortRank < friendlyLink.getFriendlyLinkSortRank()){
            friendlyLinkDao.updateCentreSortRank(originalSortRank, friendlyLink.getFriendlyLinkSortRank(), friendlyLink.getWebsiteUuid());
        }else {
            friendlyLinkDao.updateCentreSortRank(friendlyLink.getFriendlyLinkSortRank(),originalSortRank, friendlyLink.getWebsiteUuid());
        }
        friendlyLinkDao.updateById(friendlyLink);
    }

    public void saveOrUpdateConnectionCMS(FriendlyLink friendlyLink, MultipartFile file, String ip, String type){
        Website website = websiteService.getWebsite(Long.valueOf(friendlyLink.getWebsiteUuid()));
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("username", website.getBackgroundUserName());
        requestMap.add("password", website.getBackgroundPassword());
        requestMap.add("ip", ip);
        requestMap.add("url", friendlyLink.getFriendlyLinkUrl());
        requestMap.add("webname", friendlyLink.getFriendlyLinkWebName());
        requestMap.add("sortrank", friendlyLink.getFriendlyLinkSortRank());
        requestMap.add("logo", "");
        if (file == null){
            requestMap.add("logoimg", "(binary)");
        }else {
            File tempFile = new File(file.getOriginalFilename());
            try {
                file.transferTo(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("上传文件失败");
            }
            FileSystemResource fileSystemResource = new FileSystemResource(tempFile);
            requestMap.add("logoimg", fileSystemResource);
        }
        requestMap.add("msg", friendlyLink.getFriendlyLinkMsg());
        requestMap.add("email", friendlyLink.getFriendlyLinkEmail());
        requestMap.add("typeid", friendlyLink.getFriendlyLinkType());
        requestMap.add("ischeck", friendlyLink.getFriendlyLinkIsCheck());
        if ("add".equals(type)){
            requestMap.add("dopost", "add");
            JSONObject jsonObject = JSONObject.fromObject(connectionCMS(requestMap, "add", website.getBackgroundDomain()));
            friendlyLink.setFriendlyLinkId((Integer) jsonObject.get("id"));
            friendlyLink.setFriendlyLinkLogo((String) jsonObject.get("logo"));
        }else {
            requestMap.add("dopost", "saveedit");
            requestMap.add("id", friendlyLink.getFriendlyLinkId());
            JSONObject jsonObject = JSONObject.fromObject(connectionCMS(requestMap, "saveedit", website.getBackgroundDomain()));
            if (!StringUtils.isEmpty(jsonObject.get("logo"))){
                friendlyLink.setFriendlyLinkLogo((String) jsonObject.get("logo"));
            }
        }
    }

    public void deleteConnectionCMS(Long websiteUuid, String[] uuids, String ip){
        Website website = websiteService.getWebsite(websiteUuid);
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("username", website.getBackgroundUserName());
        requestMap.add("password", website.getBackgroundPassword());
        requestMap.add("ip", ip);
        requestMap.add("id", StringUtils.join(uuids, ","));
        requestMap.add("dopost", "delete");
        connectionCMS(requestMap, "delete", website.getBackgroundDomain());
    }

    public List<FriendlyLinkVO> selectConnectionCMS(Long websiteUuid, String ip){
        Website website = websiteService.getWebsite(websiteUuid);
        MultiValueMap requestMap = new LinkedMultiValueMap();
        requestMap.add("username", website.getBackgroundUserName());
        requestMap.add("password", website.getBackgroundPassword());
        requestMap.add("ip", ip);
        requestMap.add("dopost", "select");
        String resultJsonString = connectionCMS(requestMap,"select", website.getBackgroundDomain());
        JSONArray jsonArray = JSONArray.fromObject(resultJsonString);
        List<FriendlyLinkVO> friendlyLinkVOS = JSONArray.toList(jsonArray, new FriendlyLinkVO(), new JsonConfig());
        return friendlyLinkVOS;
    }

    public String connectionCMS(MultiValueMap requestMap, String type, String backgroundDomain){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        String resultJsonString;
        backgroundDomain = "http://".equals(backgroundDomain.substring(0,7).toLowerCase()) ? backgroundDomain : "http://" + backgroundDomain;
        if ("add".equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "friendlink_m_add.php",  requestMap, String.class);
        }else if ("select".equals(type)){
            resultJsonString = restTemplate.postForObject(backgroundDomain + "friendlink_m_select.php",  requestMap, String.class);
        }else {
            resultJsonString = restTemplate.postForObject(backgroundDomain + "friendlink_m_edit.php",  requestMap, String.class);
        }
        return resultJsonString;
    }

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
        friendlyLink.setFriendlyLinkMsg(request.getParameter("friendlyLinkMsg"));
        friendlyLink.setExpirationTime(format.parse(request.getParameter("expirationTime")));
        friendlyLink.setFriendlyLinkEmail(request.getParameter("friendlyLinkEmail"));
        if (!StringUtils.isEmpty(request.getParameter("uuid"))){
            friendlyLink.setUuid(Long.valueOf(request.getParameter("uuid")));
            friendlyLink.setFriendlyLinkId(Integer.valueOf(request.getParameter("friendlyLinkId")));
        }
        return friendlyLink;
    }
}
