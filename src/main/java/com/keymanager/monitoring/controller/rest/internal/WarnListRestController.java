package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.WarnListCriteria;
import com.keymanager.monitoring.entity.WarnList;
import com.keymanager.monitoring.service.WarnListService;
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
@RequestMapping(value = "/internal/warnlist")
public class WarnListRestController {

    private static Logger logger = LoggerFactory.getLogger(WarnListRestController.class);

    @Autowired
    private WarnListService warnListService;

    @RequiresPermissions("/internal/warnlist/searchWarnLists")
    @RequestMapping(value = "/searchWarnLists", method = RequestMethod.GET)
    public ModelAndView searchWarnLists(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructWarnListModelAndView(request, new WarnListCriteria(), currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/warnlist/searchWarnLists")
    @RequestMapping(value = "/searchWarnLists", method = RequestMethod.POST)
    public ModelAndView searchWarnListsPost(HttpServletRequest request, WarnListCriteria warnListCriteria) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "50";
            }
            return constructWarnListModelAndView(request, warnListCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/warnlist/list");
        }
    }

    private ModelAndView constructWarnListModelAndView(HttpServletRequest request, WarnListCriteria warnListCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/warnlist/list");
        Page<WarnList> page = warnListService.searchWarnLists(new Page<WarnList>(currentPageNumber, pageSize), warnListCriteria);
        modelAndView.addObject("warnListCriteria", warnListCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/warnlist/saveWarnList")
    @RequestMapping(value = "/saveWarnList", method = RequestMethod.POST)
    public ResponseEntity<?> saveWarnList(@RequestBody WarnList warnList, HttpServletRequest request) {
        try {
            warnListService.saveWarnList(warnList);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getWarnList/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getWarnList(@PathVariable("uuid") Long uuid) {
        try {
            return new ResponseEntity<Object>(warnListService.getWarnList(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/warnlist/deleteWarnList")
    @RequestMapping(value = "/deleteWarnList/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteWarnList(@PathVariable("uuid") Long uuid) {
        try {
            warnListService.deleteWarnList(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/warnlist/deleteWarnLists")
    @RequestMapping(value = "/deleteWarnLists", method = RequestMethod.POST)
    public ResponseEntity<?> deleteWarnLists(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            warnListService.deleteWarnLists(uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
