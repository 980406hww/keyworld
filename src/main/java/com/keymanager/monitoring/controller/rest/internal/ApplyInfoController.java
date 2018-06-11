package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplicationMarketCriteria;
import com.keymanager.monitoring.criteria.ApplyInfoCriteria;
import com.keymanager.monitoring.entity.ApplicationMarket;
import com.keymanager.monitoring.entity.ApplyInfo;
import com.keymanager.monitoring.service.ApplicationMarketService;
import com.keymanager.monitoring.service.ApplyInfoService;
import com.keymanager.monitoring.service.ApplyKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@RestController
@RequestMapping(value = "/internal/applyInfo")
public class ApplyInfoController {
    private static Logger logger = LoggerFactory.getLogger(ApplyInfoController.class);

    @Autowired
    private ApplyInfoService applyInfoService;

    @RequestMapping(value = "/searchApplyInfo", method = RequestMethod.GET)
    public ModelAndView searchApplyInfo(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize,
                                        HttpServletRequest request) {
        return constructApplicationMarketModelAndView(request,new ApplyInfoCriteria(),currentPageNumber, pageSize);
    }

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
        modelAndView.addObject("applyInfoCriteria",applyInfoCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }
}
