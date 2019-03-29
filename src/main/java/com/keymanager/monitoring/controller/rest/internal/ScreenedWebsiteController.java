package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.ScreenedWebsite;
import com.keymanager.monitoring.service.ScreenedWebsiteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/screenedWebsite")
public class ScreenedWebsiteController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ScreenedWebsiteController.class);

    @Autowired
    private ScreenedWebsiteService screenedWebsiteService;

    @RequestMapping(value = "/searchScreenedWebsiteLists", method = RequestMethod.GET)
    public ModelAndView searchscreenedWebsiteLists(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize) {
        ScreenedWebsite screenedWebsite = new ScreenedWebsite();
        return screenedWebsiteService.constructSearchScreenedWebsiteListsModelAndView(currentPageNumber, pageSize, screenedWebsite);
    }

    @RequestMapping(value = "/searchScreenedWebsiteLists", method = RequestMethod.POST)
    public ModelAndView searchscreenedWebsiteLists(ScreenedWebsite screenedWebsite, HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if(StringUtils.isEmpty(currentPageNumber)){
            currentPageNumber = "1";
        }
        if(StringUtils.isEmpty(pageSize)){
            pageSize = "50";
        }
        return screenedWebsiteService.constructSearchScreenedWebsiteListsModelAndView(Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize), screenedWebsite);
    }

    @RequestMapping(value = "/saveScreenedWebsite", method = RequestMethod.POST)
    public ResponseEntity<?> saveScreenedWebsite(@RequestBody ScreenedWebsite screenedWebsite, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("username");
            String password = (String) session.getAttribute("password");
            return new ResponseEntity<Object>(screenedWebsiteService.saveScreenedWebsite(screenedWebsite, userName, password), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getScreenedWebsite/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getScreenedWebsite(HttpServletRequest request, @PathVariable("uuid")Long uuid){
        try {
            return new ResponseEntity<Object>(screenedWebsiteService.getScreenedWebsite(uuid), HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delScreenedWebsite", method = RequestMethod.POST)
    public ResponseEntity<?> delScreenedWebsite(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("username");
            String password = (String) session.getAttribute("password");
            return new ResponseEntity<Object>(screenedWebsiteService.delScreenedWebsite(requestMap, userName, password) , HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
