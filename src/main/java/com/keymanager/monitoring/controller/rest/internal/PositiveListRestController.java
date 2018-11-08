package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.PositiveListCriteria;
import com.keymanager.monitoring.entity.PositiveList;
import com.keymanager.monitoring.service.PositiveListService;
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
@RequestMapping(value = "/internal/positivelist")
public class PositiveListRestController {

    private static Logger logger = LoggerFactory.getLogger(PositiveListRestController.class);

    @Autowired
    private PositiveListService positiveListService;

    @RequiresPermissions("/internal/positivelist/searchPositiveLists")
    @RequestMapping(value = "/searchPositiveLists", method = RequestMethod.GET)
    public ModelAndView searchPositiveLists(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructPositiveListModelAndView(request, new PositiveListCriteria(), currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/positivelist/searchPositiveLists")
    @RequestMapping(value = "/searchPositiveLists", method = RequestMethod.POST)
    public ModelAndView searchPositiveListsPost(HttpServletRequest request, PositiveListCriteria positiveListCriteria) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "50";
            }
            return constructPositiveListModelAndView(request, positiveListCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/positivelist/list");
        }
    }

    private ModelAndView constructPositiveListModelAndView(HttpServletRequest request, PositiveListCriteria positiveListCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/positivelist/list");
        Page<PositiveList> page = positiveListService.searchPositiveLists(new Page<PositiveList>(currentPageNumber,
                pageSize), positiveListCriteria);
        modelAndView.addObject("positiveListCriteria", positiveListCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/positivelist/savePositiveList")
    @RequestMapping(value = "/savePositiveList", method = RequestMethod.POST)
    public ResponseEntity<?> savePositiveList(@RequestBody PositiveList positiveList, HttpServletRequest request) {
        try {
            positiveListService.savePositiveList(positiveList, null);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getPositiveList/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getPositiveList(@PathVariable("uuid") Long uuid) {
        try {
            return new ResponseEntity<Object>(positiveListService.getPositiveList(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/positivelist/deletePositiveList")
    @RequestMapping(value = "/deletePositiveList/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> deletePositiveList(@PathVariable("uuid") Long uuid) {
        try {
            positiveListService.deletePositiveList(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/positivelist/deletePositiveLists")
    @RequestMapping(value = "/deletePositiveLists", method = RequestMethod.POST)
    public ResponseEntity<?> deletePositiveLists(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            positiveListService.deletePositiveLists(uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
