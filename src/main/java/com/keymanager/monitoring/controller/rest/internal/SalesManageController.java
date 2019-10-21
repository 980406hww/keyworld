package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.OperationType;
import com.keymanager.monitoring.entity.SalesManage;
import com.keymanager.monitoring.enums.WebsiteTypeEnum;
import com.keymanager.monitoring.service.OperationTypeService;
import com.keymanager.monitoring.service.SalesManageService;
import com.keymanager.util.AESUtils;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wjianwu 2019/4/22 17:15
 */
@RestController
@RequestMapping(value = "/internal/salesManage")
public class SalesManageController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(SalesManage.class);

    @Autowired
    private SalesManageService salesManageService;

    @RequiresPermissions("/internal/salesManage/searchSalesManageLists")
    @RequestMapping(value = "/searchSalesManageLists", method = RequestMethod.GET)
    public ModelAndView searchOperationTypeLists(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize) {
        SalesManage salesManage = new SalesManage();
        return constructSearchSalesManageListsModelAndView(currentPageNumber, pageSize, salesManage);
    }

    @RequiresPermissions("/internal/salesManage/searchSalesManageLists")
    @RequestMapping(value = "/searchSalesManageLists", method = RequestMethod.POST)
    public ModelAndView searchOperationTypeLists(SalesManage salesManage, HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (StringUtils.isEmpty(currentPageNumber)) {
            currentPageNumber = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "50";
        }
        return constructSearchSalesManageListsModelAndView(Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize), salesManage);
    }

    private ModelAndView constructSearchSalesManageListsModelAndView(int currentPageNumber, int pageSize, SalesManage salesManage) {
        ModelAndView modelAndView = new ModelAndView("salesManage/salesManage");
        Page<SalesManage> page = new Page<>(currentPageNumber, pageSize);
        List<SalesManage> list = salesManageService.SearchSalesManages(salesManage, page);
        page.setRecords(list);
        modelAndView.addObject("salesManage", salesManage);
        modelAndView.addObject("websiteTypeMap", WebsiteTypeEnum.changeToMap());
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequestMapping(value = "/returnWebsiteType", method = RequestMethod.GET)
    public ResponseEntity<?> returnWebsiteType(){
        try {
            return new ResponseEntity<Object>(WebsiteTypeEnum.changeToMap(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/salesManage/updateSalesInfo")
    @RequestMapping(value = "/getSalesManage/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getSalesManage(@PathVariable("uuid") Long uuid) {
        try {
            return new ResponseEntity<Object>(salesManageService.getSalesManageByUuid(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/salesManage/saveSalesInfo")
    @RequestMapping(value = "/saveSalesManage", method = RequestMethod.POST)
    public ResponseEntity<?> saveOrUpdateSalesManage(@RequestBody SalesManage salesManage) {
        try {
            if (salesManage.getUuid() == null) {
                salesManage.setCreateTime(new Date());
            }
            salesManage.setUpdateTime(new Date());
            return new ResponseEntity<Object>(salesManageService.insertOrUpdate(salesManage), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/salesManage/deleteSalesInfo")
    @RequestMapping(value = "/deleteSalesManage/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteSalesManage(@PathVariable("uuid") Long uuid) {
        try {
            salesManageService.deleteSalesManage(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/salesManage/deleteSalesInfo")
    @RequestMapping(value = "/deleteBeachSalesManage", method = RequestMethod.POST)
    public ResponseEntity<?> deleteBeachSalesManage(@RequestBody Map requestMap) {
        try {
            List uuids = (List) requestMap.get("uuids");
            salesManageService.deleteBeachSalesManage(uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
