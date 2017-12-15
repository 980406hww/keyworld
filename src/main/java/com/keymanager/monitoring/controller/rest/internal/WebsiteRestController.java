package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.WebsiteCriteria;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.monitoring.service.WebsiteService;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj08 on 2017/12/14.
 */
@RestController
@RequestMapping(value = "/internal/website")
public class WebsiteRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(WebsiteRestController.class);

    @Autowired
    private WebsiteService websiteService;

    @RequiresPermissions("/internal/website/searchWebsites")
    @RequestMapping(value = "/searchWebsites", method = RequestMethod.GET)
    public ModelAndView searchWebsites(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructWebsiteModelAndView(new WebsiteCriteria(), currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/website/searchWebsites")
    @RequestMapping(value = "/searchWebsites", method = RequestMethod.POST)
    public ModelAndView searchWebsitesPost(HttpServletRequest request, WebsiteCriteria websiteCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructWebsiteModelAndView(websiteCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
    }

    private ModelAndView constructWebsiteModelAndView(WebsiteCriteria websiteCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/website/website");
        Page<Website> page = websiteService.searchWebsites(new Page<Website>(currentPageNumber,pageSize), websiteCriteria);
        modelAndView.addObject("websiteCriteria", websiteCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/website/saveWebsite")
    @RequestMapping(value = "/saveWebsite", method = RequestMethod.POST)
    public ResponseEntity<?> saveWebsite(@RequestBody Website website, HttpServletRequest request) {
        try {
            websiteService.saveWebsite(website);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getWebsite/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getWebsite(@PathVariable("uuid") Long uuid) {
        try {
            return new ResponseEntity<Object>(websiteService.getWebsite(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/website/deleteWebsite")
    @RequestMapping(value = "/deleteWebsite/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteWebsite(@PathVariable("uuid") Long uuid) {
        try {
            websiteService.deleteWebsite(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/website/deleteWebsites")
    @RequestMapping(value = "/deleteWebsites", method = RequestMethod.POST)
    public ResponseEntity<?> deleteWebsites(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            websiteService.deleteWebsites(uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
