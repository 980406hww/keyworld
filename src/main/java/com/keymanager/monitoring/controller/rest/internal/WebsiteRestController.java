package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.WebsiteCriteria;
import com.keymanager.monitoring.entity.Advertising;
import com.keymanager.monitoring.entity.FriendlyLink;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.monitoring.service.AdvertisingService;
import com.keymanager.monitoring.service.FriendlyLinkService;
import com.keymanager.monitoring.enums.PutSalesInfoSignEnum;
import com.keymanager.monitoring.enums.QZSettingOperationTypeEnum;
import com.keymanager.monitoring.enums.WebsiteTypeEnum;
import com.keymanager.monitoring.service.SalesManageService;
import com.keymanager.monitoring.service.WebsiteService;
import com.keymanager.monitoring.vo.WebsiteVO;
import com.keymanager.util.FileUtil;
import com.keymanager.util.GetIpUtil;
import com.keymanager.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj08 on 2017/12/14.
 */
@RestController
@RequestMapping(value = "/internal/website")
public class WebsiteRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(WebsiteRestController.class);

    @Autowired
    private WebsiteService websiteService;
    @Autowired
    private FriendlyLinkService friendlyLinkService;
    @Autowired
    private AdvertisingService advertisingService;

    @Autowired
    private SalesManageService salesManageService;

    @RequiresPermissions("/internal/website/searchWebsites")
    @RequestMapping(value = "/searchWebsites", method = RequestMethod.GET)
    public ModelAndView searchWebsites(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructWebsiteModelAndView(new WebsiteCriteria(), currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/website/searchWebsites")
    @RequestMapping(value = "/searchWebsites", method = RequestMethod.POST)
    public ModelAndView searchWebsitesPost(HttpServletRequest request, WebsiteCriteria websiteCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructWebsiteModelAndView(websiteCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
    }

    private ModelAndView constructWebsiteModelAndView(WebsiteCriteria websiteCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/website/website");
        Page<WebsiteVO> page = websiteService.searchWebsites(new Page<WebsiteVO>(currentPageNumber, pageSize), websiteCriteria);
        modelAndView.addObject("websiteCriteria", websiteCriteria);
        modelAndView.addObject("websiteTypeMap", WebsiteTypeEnum.changeToMap());
        modelAndView.addObject("putSalesInfoSignMap", PutSalesInfoSignEnum.changeToMap());
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/website/saveWebsite")
    @RequestMapping(value = "/saveWebsite", method = RequestMethod.POST)
    public ResponseEntity<?> saveWebsite(@RequestBody Website website, HttpServletRequest request) {
        try {
            websiteService.saveWebsite(website);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getWebsite/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getWebsite(@PathVariable("uuid") Long uuid) {
        try {
            return new ResponseEntity<Object>(websiteService.getWebsite(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/website/deleteWebsite")
    @RequestMapping(value = "/deleteWebsite/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteWebsite(@PathVariable("uuid") Long uuid) {
        try {
            websiteService.deleteWebsite(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/website/deleteWebsites")
    @RequestMapping(value = "/deleteWebsites", method = RequestMethod.POST)
    public ResponseEntity<?> deleteWebsites(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            websiteService.deleteWebsites(uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/batchSaveFriendlyLink", method = RequestMethod.POST)
    public ResponseEntity<?> batchSaveFriendlyLink(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        try {
            FriendlyLink friendlyLink = websiteService.initFriendlyLink(request);
            List<String> uuids = Arrays.asList(request.getParameter("uuids").split(","));
            websiteService.batchSaveFriendlyLink(file, friendlyLink, GetIpUtil.getIP(request), uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getFriendlyLinkByUrl", method = RequestMethod.POST)
    public ResponseEntity<?> getFriendlyLinkByUrl(@RequestBody Map<String, Object> requestMap){
        try{
            FriendlyLink friendlyLink = friendlyLinkService.getFriendlyLinkByUrl(Integer.valueOf((String) requestMap.get("uuid")), (String) requestMap.get("friendlyLinkUrl"));
            return new ResponseEntity<Object>(friendlyLink, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/batchUpdateFriendlyLink", method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateFriendlyLink(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        try {
            FriendlyLink friendlyLink = websiteService.initFriendlyLink(request);
            List<String> uuids = Arrays.asList(request.getParameter("uuids").split(","));
            String originalFriendlyLinkUrl = request.getParameter("originalFriendlyLinkUrl");
            websiteService.batchUpdateFriendlyLink(file, friendlyLink, GetIpUtil.getIP(request), uuids, originalFriendlyLinkUrl);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/batchDelFriendlyLink", method = RequestMethod.POST)
    public ResponseEntity<?> batchDelFriendlyLink(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            websiteService.batchDelFriendlyLink(requestMap, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/batchSaveAdvertising", method = RequestMethod.POST)
    public ResponseEntity<?> batchSaveAdvertising(@RequestBody Advertising advertising, HttpServletRequest request){
        try{
            websiteService.batchSaveAdvertising(advertising, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/batchUpdateAdvertising", method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateAdvertising(@RequestBody Advertising advertising, HttpServletRequest request){
        try{
            websiteService.batchUpdateAdvertising(advertising, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getAdvertisingByAdvertisingTagname", method = RequestMethod.POST)
    public ResponseEntity<?> getAdvertisingByAdvertisingTagname(@RequestBody Map<String, Object> requestMap){
        try{
            Advertising advertising = advertisingService.getAdvertisingByAdvertisingTagname(Integer.valueOf((String) requestMap.get("uuid")), (String) requestMap.get("advertisingTagname"));
            return new ResponseEntity<Object>(advertising, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/batchDelAdvertising", method = RequestMethod.POST)
    public ResponseEntity<?> batchDelAdvertising(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            websiteService.batchDelAdvertising(requestMap, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/synchronousFriendlyLink", method = RequestMethod.POST)
    public ResponseEntity<?> synchronousFriendlyLink(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            websiteService.synchronousFriendlyLink(requestMap, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/synchronousAdvertising", method = RequestMethod.POST)
    public ResponseEntity<?> synchronousAdvertising(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            websiteService.synchronousAdvertising(requestMap, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/website/putSalesInfoToWebsite")
    @RequestMapping(value = "/putSalesInfoToWebsite", method = RequestMethod.POST)
    public ResponseEntity<?> putSalesInfoToWebsite(@RequestBody Map<String, Object> requestMap) {
        try {
            List uuids = (List) requestMap.get("uuids");
            websiteService.putSalesInfoToWebsite(uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
