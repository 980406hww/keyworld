package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.entity.TerminalSetting;
import com.keymanager.monitoring.entity.TerminalSettingOld;
import com.keymanager.monitoring.service.TerminalSettingOldService;
import com.keymanager.monitoring.service.TerminalSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/15.
 */
@RestController
@RequestMapping(value = "/internal/terminalSetting")
public class TerminalSettingController {
    private static Logger logger = LoggerFactory.getLogger(TerminalSettingController.class);

    @Autowired
    TerminalSettingService terminalSettingService;

    @Autowired
    TerminalSettingOldService terminalSettingOldService;

    @GetMapping(value = "/saveTerminalSetting")
    public void saveTerminalSetting(HttpServletRequest request) {
        try {
            TerminalSetting terminalSetting = new TerminalSetting();
            terminalSetting.setIP(request.getRemoteAddr());
            terminalSetting.setUA(request.getHeader("User-Agent"));
            terminalSetting.setReferer(request.getHeader("referer"));
            terminalSetting.setHeight(request.getParameter("height").toString());
            terminalSetting.setWidth(request.getParameter("width").toString());
            terminalSetting.setPdr(request.getParameter("pdr").toString());
            terminalSettingService.insert(terminalSetting);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

    @GetMapping(value = "/saveTerminalSettingOld")
    public void saveTerminalSettingOld(HttpServletRequest request) {
        try {
            TerminalSettingOld terminalSetting = new TerminalSettingOld();
            terminalSetting.setIP(request.getRemoteAddr());
            terminalSetting.setUA(request.getHeader("User-Agent"));
            terminalSetting.setReferer(request.getHeader("referer"));
            terminalSetting.setHeight(request.getParameter("height").toString());
            terminalSetting.setWidth(request.getParameter("width").toString());
            terminalSetting.setPdr(request.getParameter("pdr").toString());
            terminalSettingOldService.insert(terminalSetting);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }
}
