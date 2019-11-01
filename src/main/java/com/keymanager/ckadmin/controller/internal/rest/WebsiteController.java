package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.WebsiteCriteria;
import com.keymanager.ckadmin.entity.Advertising;
import com.keymanager.ckadmin.entity.FriendlyLink;
import com.keymanager.ckadmin.entity.Website;
import com.keymanager.ckadmin.enums.IndustryTypeEnum;
import com.keymanager.ckadmin.enums.PutSalesInfoSignEnum;
import com.keymanager.ckadmin.enums.WebsiteSynchronousSignEnum;
import com.keymanager.ckadmin.enums.WebsiteTypeEnum;
import com.keymanager.ckadmin.service.AdvertisingService;
import com.keymanager.ckadmin.service.FriendlyLinkService;
import com.keymanager.ckadmin.service.WebsiteService;
import com.keymanager.ckadmin.vo.WebsiteVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/websites")
public class WebsiteController {

    private static final Logger logger = LoggerFactory.getLogger(WebsiteController.class);

    @Resource(name = "websiteService2")
    private WebsiteService websiteService;

    @Resource(name = "friendlyLinkService2")
    private FriendlyLinkService friendlyLinkService;

    @Resource(name = "advertisingService2")
    private AdvertisingService advertisingService;

    @RequiresPermissions("/internal/friendlyLink/synchronousFriendlyLink")
    @GetMapping("/toSynchronousFriendlyLink")
    public ModelAndView toSynchronousFriendlyLink() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("webSiteList/SynchronousFriendlyLink");
        return mv;
    }

    @RequiresPermissions("/internal/website/saveWebsite")
    @GetMapping("/toAddWebsite")
    public ModelAndView toAddWebsite() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("webSiteList/AddWebsite");
        return mv;
    }

    @GetMapping("/toAdvertising/{webSiteUuid}")
    public ModelAndView toAdvertising(@PathVariable("webSiteUuid") long webSiteUuid) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("webSiteUuid", webSiteUuid);
        mv.setViewName("advertisingList/AdvertisingList");
        return mv;
    }

    @GetMapping("/toFriendlyLink/{webSiteUuid}")
    public ModelAndView toFriendlyLink(@PathVariable("webSiteUuid") long webSiteUuid) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("webSiteUuid", webSiteUuid);
        mv.setViewName("friendlyLinkList/FriendlyLink");
        return mv;
    }

    @RequiresPermissions("/internal/website/searchWebsites")
    @GetMapping("/toWebSiteList")
    public ModelAndView toWebSiteList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("webSiteList/WebSiteList");
        return mv;
    }

    @RequiresPermissions("/internal/advertising/synchronousAdvertising")
    @GetMapping("/toSynchronousAdvertising")
    public ModelAndView toSynchronousAdvertising() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("webSiteList/SynchronousAdvertising");
        return mv;
    }

    @RequiresPermissions("/internal/friendlyLink/saveFriendlyLinks")
    @GetMapping("/showBatchAddFriendlyLinkDialog")
    public ModelAndView showBatchAddFriendlyLinkDialog() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("webSiteList/BatchAddFriendlyLinkDialog");
        return mv;
    }

    @RequiresPermissions("/internal/advertising/saveAdvertising")
    @GetMapping("/showBatchAddAdvertisingDialog")
    public ModelAndView showBatchAddAdvertisingDialog() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("webSiteList/BatchAddAdvertisingDialog");
        return mv;
    }

    @RequiresPermissions("/internal/friendlyLink/saveFriendlyLink")
    @RequestMapping(value = "/getFriendlyLinkByUrl", method = RequestMethod.POST)
    public ResultBean getFriendlyLinkByUrl(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            Integer uuid = (Integer) requestMap.get("uuid");
            String friendlyLinkUrl = (String) requestMap.get("friendlyLinkUrl");
            FriendlyLink friendlyLink = friendlyLinkService.getFriendlyLinkByUrl(uuid, friendlyLinkUrl);
            resultBean.setData(friendlyLink);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/deleteFriendlyLinks")
    @RequestMapping(value = "/batchDelFriendlyLink", method = RequestMethod.POST)
    public ResultBean batchDelFriendlyLink(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            websiteService.batchDelFriendlyLink(requestMap);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/advertising/deleteAdvertisings")
    @RequestMapping(value = "/batchDelAdvertising", method = RequestMethod.POST)
    public ResultBean batchDelAdvertising(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            websiteService.batchDelAdvertising(requestMap);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/saveFriendlyLinks")
    @RequestMapping(value = "/batchSaveFriendlyLink", method = RequestMethod.POST)
    public ResultBean batchSaveFriendlyLink(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            FriendlyLink friendlyLink = websiteService.initFriendlyLink(request);
            List<String> uuids = Arrays.asList(request.getParameter("uuids").split(","));
            websiteService.batchSaveFriendlyLink(file, friendlyLink, uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/saveFriendlyLink")
    @RequestMapping(value = "/batchUpdateFriendlyLink", method = RequestMethod.POST)
    public ResultBean batchUpdateFriendlyLink(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            FriendlyLink friendlyLink = websiteService.initFriendlyLink(request);
            String[] uuids = request.getParameter("uuids").split(",");
            String originalFriendlyLinkUrl = request.getParameter("originalFriendlyLinkUrl");
            websiteService.batchUpdateFriendlyLink(file, friendlyLink, uuids, originalFriendlyLinkUrl);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/website/putSalesInfoToWebsite")
    @PostMapping(value = "/putSalesInfoToWebsite")
    public ResultBean putSalesInfoToWebsite(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List uuids = (List) requestMap.get("uuids");
            websiteService.putSalesInfoToWebsite(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/synchronousFriendlyLink")
    @RequestMapping(value = "/synchronousFriendlyLink", method = RequestMethod.POST)
    public ResultBean synchronousFriendlyLink(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            websiteService.synchronousFriendlyLink(requestMap);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/advertising/synchronousAdvertising")
    @RequestMapping(value = "/synchronousAdvertising", method = RequestMethod.POST)
    public ResultBean synchronousAdvertising(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            websiteService.synchronousAdvertising(requestMap);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/getWebsite/{uuid}", method = RequestMethod.GET)
    public ResultBean getWebsite(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            Website website = websiteService.getWebsite(uuid);
            resultBean.setCode(200);
            resultBean.setData(website);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/website/saveWebsite")
    @RequestMapping(value = "/saveWebsite", method = RequestMethod.POST)
    public ResultBean saveWebsite(@RequestBody Website website) {
        ResultBean resultBean = new ResultBean();
        try {
            websiteService.saveWebsite(website);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/website/searchWebsites")
    @PostMapping(value = "/searchWebsites")
    public ResultBean searchWebsitesPost(@RequestBody WebsiteCriteria websiteCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<WebsiteVO> page = new Page<>(websiteCriteria.getPage(), websiteCriteria.getLimit());
            page = websiteService.searchWebsites(page, websiteCriteria);
            List<WebsiteVO> websiteVOList = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(websiteVOList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/website/deleteWebsite")
    @GetMapping(value = "/delWebSite/{uuid}")
    public ResultBean delWebSite(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            websiteService.deleteWebsite(uuid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/website/deleteWebsites")
    @PostMapping("/deleteWebsites")
    public ResultBean deleteWebsites(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            websiteService.deleteWebsites(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping("/returnSelectData")
    public ResultBean returnSelectData() {
        ResultBean resultBean = new ResultBean();
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("websiteTypeMap", WebsiteTypeEnum.changeToMap());
            data.put("industryTypeMap", IndustryTypeEnum.changeToMap());
            data.put("putSalesInfoSignMap", PutSalesInfoSignEnum.changeToMap());
            data.put("websiteSynchronousSignMap", WebsiteSynchronousSignEnum.changeToMap());
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/advertising/saveAdvertising")
    @RequestMapping(value = "/batchSaveAdvertising", method = RequestMethod.POST)
    public ResultBean batchSaveAdvertising(@RequestBody Advertising advertising) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            websiteService.batchSaveAdvertising(advertising);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/advertising/saveAdvertising")
    @RequestMapping(value = "/getAdvertisingByAdvertisingTagname", method = RequestMethod.POST)
    public ResultBean getAdvertisingByAdvertisingTagname(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            Integer uuid = (Integer) requestMap.get("uuid");
            String advertisingTagname = (String) requestMap.get("advertisingTagname");
            Advertising advertising = advertisingService.getAdvertisingByAdvertisingTagname(uuid, advertisingTagname);
            resultBean.setData(advertising);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/advertising/saveAdvertising")
    @RequestMapping(value = "/batchUpdateAdvertising", method = RequestMethod.POST)
    public ResultBean batchUpdateAdvertising(@RequestBody Advertising advertising) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            websiteService.batchUpdateAdvertising(advertising);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
        }
        return resultBean;
    }
}
