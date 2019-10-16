package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.WebsiteCriteria;
import com.keymanager.ckadmin.entity.Website;
import com.keymanager.ckadmin.enums.IndustryTypeEnum;
import com.keymanager.ckadmin.enums.PutSalesInfoSignEnum;
import com.keymanager.ckadmin.enums.WebsiteSynchronousSignEnum;
import com.keymanager.ckadmin.enums.WebsiteTypeEnum;
import com.keymanager.ckadmin.service.WebsiteService;
import com.keymanager.ckadmin.vo.WebsiteVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/websites")
public class WebsiteController {
    private static final Logger logger = LoggerFactory.getLogger(WebsiteController.class);

    @Resource(name = "websiteService2")
    private WebsiteService websiteService;

    @RequiresPermissions("/internal/website/saveWebsite")
    @GetMapping("/toAddWebsite")
    public ModelAndView toAddWebsite(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("WebSiteList/AddWebsite");
        return mv;
    }

    @GetMapping("/toAdvertising/{webSiteUuid}")
    public ModelAndView toAdvertising(@PathVariable("webSiteUuid") long webSiteUuid){
        ModelAndView mv = new ModelAndView();
        mv.addObject("webSiteUuid", webSiteUuid);
        mv.setViewName("advertisingList/AdvertisingList");
        return mv;
    }

    @GetMapping("/toFriendlyLink/{webSiteUuid}")
    public ModelAndView toFriendlyLink(@PathVariable("webSiteUuid") long webSiteUuid){
        ModelAndView mv = new ModelAndView();
        mv.addObject("webSiteUuid", webSiteUuid);
        mv.setViewName("friendlyLinkList/FriendlyLink");
        return mv;
    }

    @RequiresPermissions("/internal/website/searchWebsites")
    @GetMapping("/toWebSiteList")
    public ModelAndView toWebSiteList(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("WebSiteList/WebSiteList");
        return mv;
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
    public ResultBean returnSelectData(){
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

}