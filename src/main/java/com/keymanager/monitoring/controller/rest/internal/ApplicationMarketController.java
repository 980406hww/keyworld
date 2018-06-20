package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplicationMarketCriteria;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.entity.ApplicationMarket;
import com.keymanager.monitoring.entity.ApplyInfo;
import com.keymanager.monitoring.service.ApplicationMarketService;
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
@RequestMapping(value = "/internal/applicationMarket")
public class ApplicationMarketController {
    private static Logger logger = LoggerFactory.getLogger(ApplicationMarketController.class);

    @Autowired
    private ApplicationMarketService applicationMarketService;

    @RequiresPermissions("/internal/applicationMarket/searchApplicationMarket")
    @RequestMapping(value = "/searchApplicationMarket", method = RequestMethod.GET)
    public ModelAndView searchApplicationMarket(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize,
                                        HttpServletRequest request) {
        return constructApplicationMarketModelAndView(request,new ApplicationMarketCriteria(),currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/applicationMarket/searchApplicationMarket")
    @RequestMapping(value = "/searchApplicationMarket", method = RequestMethod.POST)
    public ModelAndView searchApplicationMarket(ApplicationMarketCriteria applicationMarketCriteria,HttpServletRequest request) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "30";
            }
            return constructApplicationMarketModelAndView(request, applicationMarketCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private ModelAndView constructApplicationMarketModelAndView(HttpServletRequest request, ApplicationMarketCriteria applicationMarketCriteria , int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/applicationMarket/applicationMarketList");
        Page<ApplicationMarket> page = applicationMarketService.searchapplicationMarket(new Page<ApplicationMarket>(currentPageNumber,pageSize),applicationMarketCriteria);
        modelAndView.addObject("applicationMarketCriteria",applicationMarketCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/applicationMarket/deleteApplicationMarket")
    @RequestMapping(value = "/deleteApplicationMarket/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteApplicationMarket(@PathVariable("uuid") Long uuid) {
        try {
            applicationMarketService.deleteApplicationMarket(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/applicationMarket/deleteApplicationMarketList")
    @RequestMapping(value = "/deleteApplicationMarketList", method = RequestMethod.POST)
    public ResponseEntity<?> deleteApplicationMarketList(@RequestBody Map<String, Object> requestMap){
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            applicationMarketService.deleteApplicationMarketList(uuids);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/applicationMarket/saveApplicationMarket")
    @RequestMapping(value = "/saveApplicationMarket", method = RequestMethod.POST)
    public ResponseEntity<?> saveApplicationMarket(@RequestBody ApplicationMarket applicationMarket) {
        try {
            applicationMarketService.saveApplicationMarket(applicationMarket);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getApplicationMarket/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getApplyInfo(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(applicationMarketService.getApplicationMarket(uuid), HttpStatus.OK);
    }
}
