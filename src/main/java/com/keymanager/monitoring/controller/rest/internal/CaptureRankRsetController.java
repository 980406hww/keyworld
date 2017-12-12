package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.enums.CaptureRankExectionStatus;
import com.keymanager.monitoring.enums.CaptureRankExectionType;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.service.*;
import com.keymanager.monitoring.vo.CodeNameVo;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import jxl.write.DateTime;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@RestController
@RequestMapping(value = "/internal/captureRank")
public class CaptureRankRsetController {

    private static Logger logger = LoggerFactory.getLogger(CaptureRankRsetController.class);

    @Autowired
    private CaptureRankJobService captureRankJobService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private CustomerService customerService;

    @RequiresPermissions("/internal/captureRank/saveCaptureRankJob")
    @RequestMapping(value = "/saveCaptureRankJob", method = RequestMethod.POST)
    public ResponseEntity<?> saveCaptureRankJob(@RequestBody CaptureRankJob captureRankJob, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String loginName = request.getSession().getAttribute("username").toString();
            captureRankJobService.saveCaptureRankJob(captureRankJob, terminalType, loginName);
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
        if (null == currentPageNumber && null == currentPageNumber) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructCaptureRankJobModelAndView(request, captureRankJobSearchCriteria, currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/captureRank/searchCaptureRankJobs")
    @RequestMapping(value = "/searchCaptureRankJobs", method = RequestMethod.GET)
    public ModelAndView toSetCrawlRanking(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "50") int displaysRecords,
                                          HttpServletRequest request) {
        return constructCaptureRankJobModelAndView(request, new CaptureRankJobSearchCriteria(), currentPage + "", displaysRecords + "");
    }

    private ModelAndView constructCaptureRankJobModelAndView(HttpServletRequest request, CaptureRankJobSearchCriteria captureRankJobSearchCriteria, String currentPage, String pageSize) {
        ModelAndView modelAndView = new ModelAndView("captureRank/captureRank");
        captureRankJobSearchCriteria.setOperationType(TerminalTypeMapping.getTerminalType(request));
        Page<CaptureRankJob> page = captureRankJobService.searchCaptureRankJob(new Page<CaptureRankJob>(Integer.parseInt(currentPage), Integer.parseInt(pageSize)), captureRankJobSearchCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("captureRankJobSearchCriteria", captureRankJobSearchCriteria);
        return modelAndView;
    }

    @RequiresPermissions("/internal/captureRank/saveCaptureRankJob")
    @RequestMapping(value = "/getCaptureRankJob", method = RequestMethod.POST)
    public CaptureRankJob getCaptureRankJob(Long uuid) {
        return captureRankJobService.selectById(uuid);
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
    public ResponseEntity<?> deleteCaptureRankJobs(@RequestBody Map<String,Object> requestMap) {
        try {
            captureRankJobService.deleteBatchIds((List<Long>)requestMap.get("uuids"));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    //@RequiresPermissions("/internal/captureRank/deleteCaptureRankJob")
    @RequestMapping(value = "/captureRankJobStatus", method = RequestMethod.POST)
    public ResponseEntity<?> captureRankJobStatus(@RequestBody Map<String,Object> requestMap) {
        try {
            Long uuid = ((Integer) requestMap.get("uuid")).longValue();
            String status = (String) requestMap.get("status");
            CaptureRankJob captureRankJob = captureRankJobService.selectById(uuid);
            if(captureRankJob.getExectionStatus().equals(CaptureRankExectionStatus.Processing.name())){
                captureRankJob.setCaptureRankJobStatus(status.equals("true")?true:false);
                captureRankJobService.updateById(captureRankJob);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
            Boolean isNewStatus = captureRankJob.getExectionStatus().equals(CaptureRankExectionStatus.New.name());
            Boolean isComplete = captureRankJob.getExectionStatus().equals(CaptureRankExectionStatus.Complete.name());
            Boolean isEveryday = captureRankJob.getExectionType().equals(CaptureRankExectionType.Everyday.name());
            Boolean isLastExecutionDate = (Utils.getIntervalDays(captureRankJob.getLastExecutionDate(),new Date()) > 0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            //Boolean isExectionTime = ((captureRankJob.getExectionTime() <= dateFormat.format(new DateTime()));
            Boolean isExectionTime = true;
            if((isNewStatus|| (isComplete && isEveryday && isLastExecutionDate)) && isExectionTime){
                captureRankJob.setCaptureRankJobStatus(status.equals("true")?true:false);
                captureRankJobService.updateById(captureRankJob);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
