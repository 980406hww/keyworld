package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplyInfoCriteria;
import com.keymanager.monitoring.criteria.VpnInfoCriteria;
import com.keymanager.monitoring.entity.ApplyInfo;
import com.keymanager.monitoring.entity.VpnInfo;
import com.keymanager.monitoring.service.ApplyInfoService;
import com.keymanager.monitoring.service.VpnInfoService;
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
@RequestMapping(value = "/internal/vpnInfo")
public class VpnInfoController {
    private static Logger logger = LoggerFactory.getLogger(VpnInfoController.class);

    @Autowired
    private VpnInfoService vpnInfoService;

    @RequiresPermissions("/internal/vpnInfo/searchVpnInfo")
    @RequestMapping(value = "/searchVpnInfo", method = RequestMethod.GET)
    public ModelAndView searchVpnInfo(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize,
                                        HttpServletRequest request) {
        return constructApplicationMarketModelAndView(request,new VpnInfoCriteria(),currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/vpnInfo/searchVpnInfo")
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

    @RequiresPermissions("/internal/vpnInfo/deleteVpnInfo")
    @RequestMapping(value = "/deleteVpnInfo/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteVpnInfo(@PathVariable("uuid") Long uuid) {
        try {
            vpnInfoService.deleteVpnInfo(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/vpnInfo/deleteVpnInfoList")
    @RequestMapping(value = "/deleteVpnInfoList", method = RequestMethod.POST)
    public ResponseEntity<?> deleteApplyInfoList(@RequestBody Map<String, Object> requestMap){
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            vpnInfoService.deleteVpnInfoList(uuids);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/vpnInfo/saveVpnInfo")
    @RequestMapping(value = "/saveVpnInfo", method = RequestMethod.POST)
    public ResponseEntity<?> saveVpnInfo(@RequestBody VpnInfo vpnInfo) {
        try {
            vpnInfoService.saveApplyInfo(vpnInfo);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getVpnInfo/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getVpnInfo(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(vpnInfoService.getVpnInfo(uuid), HttpStatus.OK);
    }
}
