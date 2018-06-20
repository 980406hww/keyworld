package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplicationMarketCriteria;
import com.keymanager.monitoring.criteria.ApplyInfoCriteria;
import com.keymanager.monitoring.entity.ApplicationMarket;
import com.keymanager.monitoring.entity.ApplyInfo;
import com.keymanager.monitoring.service.ApplicationMarketService;
import com.keymanager.monitoring.service.ApplyInfoService;
import com.keymanager.monitoring.service.ApplyKeywordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@RestController
@RequestMapping(value = "/internal/applyInfo")
public class ApplyInfoController {
    private static Logger logger = LoggerFactory.getLogger(ApplyInfoController.class);

    @Autowired
    private ApplyInfoService applyInfoService;

    @Autowired
    private ApplicationMarketService applicationMarketService;

    @RequiresPermissions("/internal/applyInfo/searchApplyInfo")
    @RequestMapping(value = "/searchApplyInfo", method = RequestMethod.GET)
    public ModelAndView searchApplyInfo(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize,
                                        HttpServletRequest request) {
        return constructApplicationMarketModelAndView(request,new ApplyInfoCriteria(),currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/applyInfo/searchApplyInfo")
    @RequestMapping(value = "/searchApplyInfo", method = RequestMethod.POST)
    public ModelAndView searchApplyInfo(ApplyInfoCriteria applyInfoCriteria, HttpServletRequest request) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "30";
            }
            return constructApplicationMarketModelAndView(request, applyInfoCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private ModelAndView constructApplicationMarketModelAndView(HttpServletRequest request, ApplyInfoCriteria applyInfoCriteria , int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/applyInfo/applyInfoList");
        Page<ApplyInfo> page = applyInfoService.searchApplyInfoList(new Page<ApplyInfo>(currentPageNumber,pageSize),applyInfoCriteria);
        List<ApplicationMarket> applicationMarkets = applicationMarketService.selectApplicationMarket();
        modelAndView.addObject("applyInfoCriteria",applyInfoCriteria);
        modelAndView.addObject("applicationMarkets",applicationMarkets);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/applyInfo/deleteApplyInfo")
    @RequestMapping(value = "/deleteApplyInfo/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteApplyInfo(@PathVariable("uuid") Long uuid) {
        try {
            applyInfoService.deleteApplyInfo(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/applyInfo/deleteApplyInfoList")
    @RequestMapping(value = "/deleteApplyInfoList", method = RequestMethod.POST)
    public ResponseEntity<?> deleteApplyInfoList(@RequestBody Map<String, Object> requestMap){
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            applyInfoService.deleteApplyInfoList(uuids);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/applyInfo/saveApplyInfo")
    @RequestMapping(value = "/saveApplyInfo", method = RequestMethod.POST)
    public ResponseEntity<?> saveApplyInfo(@RequestBody ApplyInfo applyInfo) {
        try {
            applyInfoService.saveApplyInfo(applyInfo);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getApplyInfo/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getApplyInfo(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(applyInfoService.getApplyInfo(uuid), HttpStatus.OK);
    }
}
