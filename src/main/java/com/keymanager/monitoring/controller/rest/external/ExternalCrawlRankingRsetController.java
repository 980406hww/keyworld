package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.criteria.CaptureRankCriteria;
import com.keymanager.monitoring.entity.CaptureCurrentRankJob;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.enums.CaptureRankExectionStatus;
import com.keymanager.monitoring.service.CaptureCurrentRankJobService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj24 on 2017/9/27.
 */
@RestController
@RequestMapping(value = "/external/crawlRanking")
public class ExternalCrawlRankingRsetController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalCrawlRankingRsetController.class);

    @Autowired
    private CaptureCurrentRankJobService captureCurrentRankJobService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @RequestMapping(value = "/crawlRankingExternalInterface", method = RequestMethod.POST)
    public ResponseEntity<?> doCrawlRanking(@RequestBody BaseCriteria baseCriteria) {
        String userName = baseCriteria.getUserName();
        String password = baseCriteria.getUserName();
        try {
            if (validUser(userName, password)) {
                CaptureCurrentRankJob captureCurrentRankJob = captureCurrentRankJobService.provideCaptureCurrentRankJob();
                return new ResponseEntity<Object>(captureCurrentRankJob, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(value = "/updateJob", method = RequestMethod.POST)
    public ResponseEntity<?> updateJob(@RequestBody  CaptureRankCriteria captureRankCriteria) {
        String userName = captureRankCriteria.getUserName();
        String password = captureRankCriteria.getUserName();
        CaptureCurrentRankJob captureCurrentRankJob=captureRankCriteria.getCaptureRankJob();
        try {
            if (validUser(userName, password)) {
                captureCurrentRankJob.setExectionStatus(CaptureRankExectionStatus.Complete.name());
                captureCurrentRankJob.setEndTime(new Time(new Date().getTime()));
                captureCurrentRankJob.setLastExecutionDate(new java.sql.Date(new Date().getTime()));
                captureCurrentRankJobService.updateById(captureCurrentRankJob);
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordForCapturePosition", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordForCapturePosition(@RequestBody CaptureRankCriteria captureRankCriteria, HttpServletRequest request) throws Exception {
        String userName = captureRankCriteria.getUserName();
        String password = captureRankCriteria.getUserName();
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        try {
            if (validUser(userName, password)) {
                CaptureCurrentRankJob captureCurrentRankJob = captureRankCriteria.getCaptureRankJob();
                String[] groupNames = captureCurrentRankJob.getGroupNames().split(",");
                Long customerID = captureCurrentRankJob.getCustomerID();
                CustomerKeyword customerKeyword = customerKeywordService.searchTitleAndUrl(groupNames, customerID);
                return new ResponseEntity<Object>(customerKeyword, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
