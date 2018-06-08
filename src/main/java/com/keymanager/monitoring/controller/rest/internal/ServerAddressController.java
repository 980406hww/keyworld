package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplyInfoCriteria;
import com.keymanager.monitoring.criteria.ServerAddressCriteria;
import com.keymanager.monitoring.entity.ApplyInfo;
import com.keymanager.monitoring.entity.ServerAddress;
import com.keymanager.monitoring.service.ApplyInfoService;
import com.keymanager.monitoring.service.ServerAddressService;
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
@RequestMapping(value = "/internal/serverAddress")
public class ServerAddressController {
    private static Logger logger = LoggerFactory.getLogger(ServerAddressController.class);

    @Autowired
    private ServerAddressService serverAddressService;

    @RequestMapping(value = "/searchServerAddress", method = RequestMethod.GET)
    public ModelAndView searchServerAddress(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize,
                                        HttpServletRequest request) {
        return constructApplicationMarketModelAndView(request,new ServerAddressCriteria(),currentPageNumber, pageSize);
    }

    @RequestMapping(value = "/searchServerAddress", method = RequestMethod.POST)
    public ModelAndView searchServerAddress(ServerAddressCriteria serverAddressCriteria, HttpServletRequest request) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "30";
            }
            return constructApplicationMarketModelAndView(request, serverAddressCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private ModelAndView constructApplicationMarketModelAndView(HttpServletRequest request, ServerAddressCriteria serverAddressCriteria , int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/serverAddress/serverAddressList");
        Page<ServerAddress> page = serverAddressService.searchServerAddressList(new Page<ServerAddress>(currentPageNumber,pageSize),serverAddressCriteria);
        modelAndView.addObject("serverAddressCriteria",serverAddressCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }
}
