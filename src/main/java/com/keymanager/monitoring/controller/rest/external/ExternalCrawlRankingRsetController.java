package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.criteria.CaptureRankJobCriteria;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.enums.CaptureRankExectionStatus;
import com.keymanager.monitoring.service.CaptureRankJobService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.util.TerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by shunshikj24 on 2017/9/27.
 */
@RestController
@RequestMapping(value = "/external/crawlRanking")
public class ExternalCrawlRankingRsetController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalCrawlRankingRsetController.class);

    @Autowired
    private CaptureRankJobService captureRankJobService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @RequestMapping(value = "/getCaptureRankJob", method = RequestMethod.POST)
    public ResponseEntity<?> getCaptureRankJob(@RequestBody BaseCriteria baseCriteria) {
        String userName = baseCriteria.getUserName();
        String password = baseCriteria.getPassword();
        try {
            if (validUser(userName, password)) {
                CaptureRankJob captureRankJob = captureRankJobService.provideCaptureRankJob();
                return new ResponseEntity<Object>(captureRankJob, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCaptureRankJob", method = RequestMethod.POST)
    public ResponseEntity<?> updateCaptureRankJob(@RequestBody CaptureRankJobCriteria captureRankJobCriteria) {
        String userName = captureRankJobCriteria.getUserName();
        String password = captureRankJobCriteria.getPassword();
        CaptureRankJob captureRankJob = captureRankJobCriteria.getCaptureRankJob();
        try {
            if (validUser(userName, password)) {
                captureRankJob.setExectionStatus(CaptureRankExectionStatus.Complete.name());
                captureRankJob.setEndTime(new Date());
                captureRankJob.setLastExecutionDate(new java.sql.Date(new Date().getTime()));
                captureRankJobService.updateById(captureRankJob);
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
