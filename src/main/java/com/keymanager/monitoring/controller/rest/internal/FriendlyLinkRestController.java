package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.criteria.FriendlyLinkCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.FriendlyLink;
import com.keymanager.monitoring.service.CustomerService;
import com.keymanager.monitoring.service.FriendlyLinkService;
import com.keymanager.util.GetIpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/friendlyLink")
public class FriendlyLinkRestController {

    private static Logger logger = LoggerFactory.getLogger(FriendlyLinkRestController.class);

    @Autowired
    private FriendlyLinkService friendlyLinkService;
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/searchFriendlyLinkLists/{websiteUuid}", method = RequestMethod.GET)
    public ModelAndView searchFriendlyLinkLists(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, @PathVariable("websiteUuid") Long websiteUuid) {
        FriendlyLinkCriteria friendlyLinkCriteria = new FriendlyLinkCriteria();
        friendlyLinkCriteria.setWebsiteUuid(websiteUuid);
        return friendlyLinkService.constructSearchFriendlyLinkListsModelAndView(currentPageNumber, pageSize, friendlyLinkCriteria);
    }

    @RequestMapping(value = "/searchFriendlyLinkLists", method = RequestMethod.POST)
    public ModelAndView searchFriendlyLinkLists(FriendlyLinkCriteria friendlyLinkCriteria, HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if(StringUtils.isEmpty(currentPageNumber)){
            currentPageNumber = "1";
        }
        if(StringUtils.isEmpty(pageSize)){
            pageSize = "50";
        }
        return friendlyLinkService.constructSearchFriendlyLinkListsModelAndView(Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize), friendlyLinkCriteria);
    }

    @RequestMapping(value = "/saveFriendlyLink", method = RequestMethod.POST)
    public ResponseEntity<?> saveFriendlyLink(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
        try{
            FriendlyLink friendlyLink = friendlyLinkService.initFriendlyLink(request);
            friendlyLinkService.saveFriendlyLink(file, friendlyLink, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateFriendlyLink", method = RequestMethod.POST)
    public ResponseEntity<?> updateFriendlyLink(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
        try{
            FriendlyLink friendlyLink = friendlyLinkService.initFriendlyLink(request);
            int originalSortRank = Integer.valueOf(request.getParameter("originalSortRank"));
            friendlyLinkService.updateFriendlyLink(file, friendlyLink, GetIpUtil.getIP(request), originalSortRank);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getFriendlyLink/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getFriendlyLink(@PathVariable Long uuid, HttpServletRequest request){
        try{
            FriendlyLink friendlyLink = friendlyLinkService.getFriendlyLink(uuid);
            return new ResponseEntity<Object>(friendlyLink, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delFriendlyLink/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> delFriendlyLink(@PathVariable Long uuid, HttpServletRequest request){
        try{
            friendlyLinkService.delFriendlyLink(uuid, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delFriendlyLinks", method = RequestMethod.POST)
    public ResponseEntity<?> delFriendlyLinks(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            friendlyLinkService.delFriendlyLinks(requestMap, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/searchFriendlyLinkTypeList/{websiteUuid}", method = RequestMethod.GET)
    public ResponseEntity<?>  searchFriendlyLinkTypeList(@PathVariable Long websiteUuid, HttpServletRequest request) {
        try {
            List<Map> friendlyLinkTypeList = friendlyLinkService.searchFriendlyLinkTypeList(websiteUuid, GetIpUtil.getIP(request));
            return new ResponseEntity<Object>(friendlyLinkTypeList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
