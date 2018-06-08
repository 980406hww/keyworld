package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplyInfoCriteria;
import com.keymanager.monitoring.criteria.VpnInfoCriteria;
import com.keymanager.monitoring.entity.ApplyInfo;
import com.keymanager.monitoring.entity.VpnInfo;
import com.keymanager.monitoring.service.ApplyInfoService;
import com.keymanager.monitoring.service.VpnInfoService;
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
@RequestMapping(value = "/internal/vpnInfo")
public class VpnInfoController {
    private static Logger logger = LoggerFactory.getLogger(VpnInfoController.class);

    @Autowired
    private VpnInfoService vpnInfoService;

    @RequestMapping(value = "/searchVpnInfo", method = RequestMethod.GET)
    public ModelAndView searchVpnInfo(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize,
                                        HttpServletRequest request) {
        return constructApplicationMarketModelAndView(request,new VpnInfoCriteria(),currentPageNumber, pageSize);
    }

    @RequestMapping(value = "/searchVpnInfo", method = RequestMethod.POST)
    public ModelAndView searchVpnInfo(VpnInfoCriteria vpnInfoCriteria, HttpServletRequest request) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "30";
            }
            return constructApplicationMarketModelAndView(request, vpnInfoCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private ModelAndView constructApplicationMarketModelAndView(HttpServletRequest request, VpnInfoCriteria vpnInfoCriteria , int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/vpnInfo/vpnInfoList");
        Page<VpnInfo> page = vpnInfoService.searchVpnInfoList(new Page<VpnInfo>(currentPageNumber,pageSize),vpnInfoCriteria);
        modelAndView.addObject("vpnInfoCriteria",vpnInfoCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }
}
