package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.NegativeListCriteria;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.service.NegativeListService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/negativelist")
public class NegativeListRestController {

    private static Logger logger = LoggerFactory.getLogger(NegativeListRestController.class);

    @Autowired
    private NegativeListService negativeListService;

    @RequiresPermissions("/internal/negativelist/searchNegativeLists")
    @RequestMapping(value = "/searchNegativeLists", method = RequestMethod.GET)
    public ModelAndView searchNegativeLists(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructNegativeListModelAndView(request, new NegativeListCriteria(), currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/negativelist/searchNegativeLists")
    @RequestMapping(value = "/searchNegativeLists", method = RequestMethod.POST)
    public ModelAndView searchNegativeListsPost(HttpServletRequest request, NegativeListCriteria negativeListCriteria) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "50";
            }
            return constructNegativeListModelAndView(request, negativeListCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/negativelist/list");
        }
    }

    private ModelAndView constructNegativeListModelAndView(HttpServletRequest request, NegativeListCriteria negativeListCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/negativelist/list");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        negativeListCriteria.setTerminalType(terminalType);
        Page<NegativeList> page = negativeListService.searchNegativeLists(new Page<NegativeList>(currentPageNumber,
                pageSize), negativeListCriteria);
        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("negativeListCriteria", negativeListCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/negativelist/saveNegativeList")
    @RequestMapping(value = "/saveNegativeList", method = RequestMethod.POST)
    public ResponseEntity<?> saveNegativeList(@RequestBody NegativeList negativeList, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            negativeList.setTerminalType(terminalType);
            negativeListService.saveNegativeList(negativeList);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getNegativeList/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getNegativeList(@PathVariable("uuid") Long uuid) {
        try {
            return new ResponseEntity<Object>(negativeListService.getNegativeList(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/negativelist/deleteNegativeList")
    @RequestMapping(value = "/deleteNegativeList/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteNegativeList(@PathVariable("uuid") Long uuid) {
        try {
            negativeListService.deleteNegativeList(uuid , null);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/negativelist/deleteNegativeLists")
    @RequestMapping(value = "/deleteNegativeLists", method = RequestMethod.POST)
    public ResponseEntity<?> deleteNegativeLists(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            negativeListService.deleteAll(uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
