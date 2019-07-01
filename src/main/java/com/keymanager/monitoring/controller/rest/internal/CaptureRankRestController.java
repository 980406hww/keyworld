package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.enums.RankJobAreaEnum;
import com.keymanager.monitoring.service.CaptureRankJobService;
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

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@RestController
@RequestMapping(value = "/internal/captureRank")
public class CaptureRankRestController {

    private static Logger logger = LoggerFactory.getLogger(CaptureRankRestController.class);

    @Autowired
    private CaptureRankJobService captureRankJobService;

    @RequiresPermissions("/internal/captureRank/saveCaptureRankJob")
    @RequestMapping(value = "/saveCaptureRankJob", method = RequestMethod.POST)
    public ResponseEntity<?> saveCaptureRankJob(@RequestBody Map map, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String loginName = request.getSession().getAttribute("username").toString();
            captureRankJobService.saveCaptureRankJob(map, terminalType, loginName);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/captureRank/searchCaptureRankJobs")
    @RequestMapping(value = "/searchCaptureRankJobs", method = RequestMethod.POST)
    public ModelAndView searchCaptureRankingJobs(CaptureRankJobSearchCriteria captureRankJobSearchCriteria, HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructCaptureRankJobModelAndView(request, captureRankJobSearchCriteria, currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/captureRank/searchCaptureRankJobs")
    @RequestMapping(value = "/searchCaptureRankJobs", method = RequestMethod.GET)
    public ModelAndView toSetCrawlRanking(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "50") int displaysRecords, HttpServletRequest request) {
        return constructCaptureRankJobModelAndView(request, new CaptureRankJobSearchCriteria(), currentPage + "", displaysRecords + "");
    }

    private ModelAndView constructCaptureRankJobModelAndView(HttpServletRequest request, CaptureRankJobSearchCriteria captureRankJobSearchCriteria, String currentPage, String pageSize) {
        ModelAndView modelAndView = new ModelAndView("captureRank/captureRank");
        captureRankJobSearchCriteria.setOperationType(TerminalTypeMapping.getTerminalType(request));
        captureRankJobSearchCriteria.setRankJobType(captureRankJobSearchCriteria.getRankJobType() == null ? "Common" : captureRankJobSearchCriteria.getRankJobType());
        Page<CaptureRankJob> page = captureRankJobService.searchCaptureRankJob(new Page<CaptureRankJob>(Integer.parseInt(currentPage), Integer.parseInt(pageSize)), captureRankJobSearchCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("rankJobAreaMap", RankJobAreaEnum.changeToMap());
        modelAndView.addObject("captureRankJobSearchCriteria", captureRankJobSearchCriteria);
        return modelAndView;
    }

    @RequiresPermissions("/internal/captureRank/saveCaptureRankJob")
    @RequestMapping(value = "/getCaptureRankJob", method = RequestMethod.POST)
    public CaptureRankJob getCaptureRankJob(Long uuid) {
        return captureRankJobService.getCaptureRankJobAndCustomerName(uuid);
    }

    @RequiresPermissions("/internal/captureRank/deleteCaptureRankJob")
    @RequestMapping(value = "/deleteCaptureRankJob", method = RequestMethod.POST)
    public ResponseEntity<?> deleteCaptureRankJob(Long uuid) {
        try {
            captureRankJobService.deleteById(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/captureRank/deleteCaptureRankJobs")
    @RequestMapping(value = "/deleteCaptureRankJobs", method = RequestMethod.POST)
    public ResponseEntity<?> deleteCaptureRankJobs(@RequestBody Map<String, Object> requestMap) {
        try {
            captureRankJobService.deleteBatchIds((List<Long>) requestMap.get("uuids"));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/captureRank/changeCaptureRankJobStatus")
    @RequestMapping(value = "/changeCaptureRankJobStatus", method = RequestMethod.POST)
    public ResponseEntity<?> changeCaptureRankJobStatus(@RequestBody Map<String, Object> requestMap) {
        try {
            Long uuid = ((Integer) requestMap.get("uuid")).longValue();
            String status = (String) requestMap.get("status");
            captureRankJobService.changeCaptureRankJobStatus(uuid, status);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/resetCaptureRankJobs", method = RequestMethod.POST)
    public ResponseEntity<?> resetCaptureRankJobs(@RequestBody Map<String, Object> requestMap) {
        try {
            captureRankJobService.resetCaptureRankJobs((List) requestMap.get("uuids"));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
