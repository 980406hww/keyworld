package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CustomerKeywordCriteria;
import com.keymanager.monitoring.criteria.NegativeStandardSettingCriteria;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.NegativeStandardSetting;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.NegativeStandardSettingService;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by ljj on 2018/6/28.
 */

@RestController
@RequestMapping(value = "/internal/negativeStandardSetting")
public class NegativeStandardSettingController  extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(NegativeStandardSettingController.class);

    @Autowired
    private NegativeStandardSettingService negativeStandardSettingService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @RequiresPermissions("/internal/negativeStandardSetting/searchNegativeStandardSetting")
    @RequestMapping(value="/searchNegativeStandardSetting/{customerUuid}" , method=RequestMethod.GET)
    public ModelAndView searchNegativeStandardSetting(@PathVariable("customerUuid") Long customerUuid,@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request){
         NegativeStandardSettingCriteria negativeStandardSettingCriteria = new NegativeStandardSettingCriteria();
        negativeStandardSettingCriteria.setCustomerUuid(customerUuid);
        return  constructNegativeStandardSetting(request,negativeStandardSettingCriteria ,currentPageNumber+"",pageSize+"");
    }

    @RequiresPermissions("/internal/negativeStandardSetting/searchNegativeStandardSetting")
    @RequestMapping(value = "/searchNegativeStandardSetting")
    public ModelAndView searchNegativeStandardSetting(HttpServletRequest request,NegativeStandardSettingCriteria negativeStandardSettingCriteria){
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == currentPageNumber){
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructNegativeStandardSetting(request , negativeStandardSettingCriteria , currentPageNumber , pageSize);
    }

    @RequiresPermissions("/internal/negativeStandardSetting/allNegativeStandardSetting")
    @RequestMapping(value = "/allNegativeStandardSetting")
    public ModelAndView allNegativeStandardSetting(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request){
        NegativeStandardSettingCriteria negativeStandardSettingCriteria = new NegativeStandardSettingCriteria();
        return  constructNegativeStandardSetting(request,negativeStandardSettingCriteria ,currentPageNumber+"",pageSize+"");
    }

    public  ModelAndView constructNegativeStandardSetting(HttpServletRequest request,NegativeStandardSettingCriteria negativeStandardSettingCriteria, String currentPageNumber, String pageSize){
        ModelAndView modelAndView = new ModelAndView("negativeStandardSetting/negativeStandardSetting");
        CustomerKeywordCriteria customerKeywordCriteria = new CustomerKeywordCriteria();
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        String entryType = (String) request.getSession().getAttribute("entryType");
        customerKeywordCriteria.setTerminalType(terminalType);
        customerKeywordCriteria.setEntryType(entryType);
        customerKeywordCriteria.setCustomerUuid(negativeStandardSettingCriteria.getCustomerUuid());
        Page<NegativeStandardSetting>  page = null;
        Set<String> setCustomerKeywords = null;
        if(negativeStandardSettingCriteria.getCustomerUuid()!=null){
            setCustomerKeywords = customerKeywordService.getCustomerKeywordInfo(customerKeywordCriteria);
            page = negativeStandardSettingService.searchNegaStandardSetting(new Page<NegativeStandardSetting>(Integer.parseInt(currentPageNumber),Integer.parseInt(pageSize)),negativeStandardSettingCriteria);
        }else {
            page = negativeStandardSettingService.allNegativeStandardSetting(new Page<NegativeStandardSetting>(Integer.parseInt(currentPageNumber),Integer.parseInt(pageSize)),negativeStandardSettingCriteria);
        }
        modelAndView.addObject("page",page);
        modelAndView.addObject("setCustomerKeywords",setCustomerKeywords);
        modelAndView.addObject("negativeStandardSettingCriteria",negativeStandardSettingCriteria);
        return modelAndView;
    }

    @RequiresPermissions("/internal/negativeStandardSetting/deleteNegativeStandardSetting")
    @RequestMapping(value = "/deleteNegativeStandardSetting/{uuid}")
    public ResponseEntity<?> deleteNegativeStandardSetting(@PathVariable("uuid") Long uuid){
        try {
            negativeStandardSettingService.deleteNegativeStandardSetting(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/negativeStandardSetting/addNegativeStandardSetting")
    @RequestMapping(value = "/addNegativeStandardSetting")
    public ResponseEntity<?> addNegativeStandardSetting(@RequestBody NegativeStandardSetting  negativeStandardSetting){
        try {
            Integer negativeStandardSettingCount = negativeStandardSettingService.findNegativeStandardSetting(negativeStandardSetting.getCustomerUuid(),negativeStandardSetting.getKeyword(),negativeStandardSetting.getSearchEngine());
            if(negativeStandardSettingCount == 0){
                negativeStandardSettingService.saveNegativeStandardSetting(negativeStandardSetting);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }else {
                return new ResponseEntity<Object>(false, HttpStatus.OK);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/negativeStandardSetting/updateNegativeStandardSetting")
    @RequestMapping(value = "/updateNegativeStandardSetting")
    public ResponseEntity<?> updateNegativeStandardSetting(@RequestBody NegativeStandardSetting  negativeStandardSetting){
        try {
            negativeStandardSettingService.saveNegativeStandardSetting(negativeStandardSetting);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/negativeStandardSetting/deleteNegativeStandardSettings")
    @RequestMapping(value = "/deleteNegativeStandardSettings")
    public ResponseEntity<?> deleteNegativeStandardSettings(@RequestBody Map<String,Object> requestMap){
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            negativeStandardSettingService.deleteNegativeStandardSettings(uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
