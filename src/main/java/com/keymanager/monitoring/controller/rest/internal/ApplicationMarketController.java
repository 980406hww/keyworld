package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplicationMarketCriteria;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.entity.ApplicationMarket;
import com.keymanager.monitoring.service.ApplicationMarketService;
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
@RequestMapping(value = "/internal/applicationMarket")
public class ApplicationMarketController {
    private static Logger logger = LoggerFactory.getLogger(ApplicationMarketController.class);

    @Autowired
    private ApplicationMarketService applicationMarketService;

    @RequestMapping(value = "/searchApplicationMarket", method = RequestMethod.GET)
    public ModelAndView searchApplicationMarket(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize,
                                        HttpServletRequest request) {
        return constructApplicationMarketModelAndView(request,new ApplicationMarketCriteria(),currentPageNumber, pageSize);
    }

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
}
