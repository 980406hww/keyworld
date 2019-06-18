package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.AdvertisingAllTypeAndCustomerListCriteria;
import com.keymanager.monitoring.criteria.AdvertisingCriteria;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.Advertising;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.service.AdvertisingService;
import com.keymanager.monitoring.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
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

@RestController
@RequestMapping(value = "/internal/advertising")
public class AdvertisingRestController {

    private static Logger logger = LoggerFactory.getLogger(AdvertisingRestController.class);

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdvertisingService advertisingService;

    @RequiresPermissions("/internal/advertising/searchAdvertisings")
    @RequestMapping(value = "/searchAdvertisingLists/{websiteUuid}", method = RequestMethod.GET)
    public ModelAndView searchAdvertisingLists(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, @PathVariable("websiteUuid") Long websiteUuid) {
        AdvertisingCriteria advertisingCriteria = new AdvertisingCriteria();
        advertisingCriteria.setWebsiteUuid(websiteUuid);
        return advertisingService.constructSearchAdvertisingListsModelAndView(currentPageNumber, pageSize, advertisingCriteria);
    }

    @RequiresPermissions("/internal/advertising/searchAdvertisings")
    @RequestMapping(value = "/searchAdvertisingLists", method = RequestMethod.POST)
    public ModelAndView searchAdvertisingLists(AdvertisingCriteria advertisingCriteria, HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if(StringUtils.isEmpty(currentPageNumber)){
            currentPageNumber = "1";
        }
        if(StringUtils.isEmpty(pageSize)){
            pageSize = "50";
        }
        return advertisingService.constructSearchAdvertisingListsModelAndView(Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize), advertisingCriteria);
    }

    @RequiresPermissions("/internal/advertising/saveAdvertisings")
    @RequestMapping(value = "/searchAdvertisingAllTypeAndCustomerList/{websiteUuid}", method = RequestMethod.GET)
    public ResponseEntity<?> searchAdvertisingAllTypeAndCustomerList(@PathVariable Long websiteUuid, HttpServletRequest request) {
        try {
            CustomerCriteria customerCriteria = new CustomerCriteria();
            String entryType = (String) request.getSession().getAttribute("entryType");
            customerCriteria.setEntryType(entryType);
            List<Customer> customerList = customerService.getActiveCustomerSimpleInfo(customerCriteria);
            AdvertisingAllTypeAndCustomerListCriteria advertisingAllTypeAndCustomerListCriteria = advertisingService.searchAdvertisingAllTypeList(websiteUuid);
            advertisingAllTypeAndCustomerListCriteria.setCustomerList(customerList);
            return new ResponseEntity<Object>(advertisingAllTypeAndCustomerListCriteria, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/advertising/saveAdvertisings")
    @RequestMapping(value = "/saveAdvertising", method = RequestMethod.POST)
    public ResponseEntity<?> saveAdvertising(@RequestBody Advertising advertising){
        try{
            advertisingService.saveAdvertising(advertising);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/advertising/saveAdvertising")
    @RequestMapping(value = "/updateAdvertising", method = RequestMethod.POST)
    public ResponseEntity<?> updateAdvertising(@RequestBody Advertising advertising){
        try{
            advertisingService.updateAdvertising(advertising);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @RequiresPermissions("/internal/advertising/saveAdvertising")
    @RequestMapping(value = "/getAdvertising/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getAdvertising(@PathVariable Long uuid){
        try{
            Advertising advertising = advertisingService.getAdvertising(uuid);
            return new ResponseEntity<Object>(advertising, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/advertising/deleteAdvertising")
    @RequestMapping(value = "/delAdvertising/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> delAdvertising(@PathVariable Long uuid){
        try{
            advertisingService.delAdvertising(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/advertising/deleteAdvertisings")
    @RequestMapping(value = "/delAdvertisings", method = RequestMethod.POST)
    public ResponseEntity<?> delAdvertisings(@RequestBody Map<String, Object> requestMap) {
        try {
            advertisingService.delAdvertisings(requestMap);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/advertising/pushAdvertising")
    @RequestMapping(value = "/pushAdvertising", method = RequestMethod.POST)
    public ResponseEntity<?> pushAdvertising(@RequestBody Map<String, Object> requestMap) {
        try {
            advertisingService.pushAdvertising(requestMap);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
