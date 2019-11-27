package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CaptureRankJobCriteria;
import com.keymanager.monitoring.criteria.ExternalCaptureJobCriteria;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.service.CaptureRankJobService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

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
    public ResponseEntity<?> getCaptureRankJob(@RequestBody ExternalCaptureJobCriteria captureJobCriteria) {
        String userName = captureJobCriteria.getUserName();
        String password = captureJobCriteria.getPassword();
        try {
            if (validUser(userName, password)) {
                CaptureRankJob captureRankJob = captureRankJobService.provideCaptureRankJob(captureJobCriteria);
                if (captureRankJob == null) {
                    captureRankJob = new CaptureRankJob();
                    captureRankJob.setGroupNames("end");
                }
                return new ResponseEntity<Object>(captureRankJob, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCaptureRankJobTemp2", method = RequestMethod.POST)
    public synchronized ResponseEntity<?> getCaptureRankJobTemp(@RequestBody ExternalCaptureJobCriteria captureJobCriteria) {
        String userName = captureJobCriteria.getUserName();
        String password = captureJobCriteria.getPassword();
        try {
            if (validUser(userName, password)) {
                captureRankJobService.searchFiveMiniSetCheckingJobs();
                CaptureRankJob captureRankJob = captureRankJobService.provideCaptureRankJob(captureJobCriteria);
                if (captureRankJob == null) {
                    captureRankJob = new CaptureRankJob();
                    captureRankJob.setGroupNames("end");
                }
                return new ResponseEntity<Object>(captureRankJob, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
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
                captureRankJobService.completeCaptureRankJob(captureRankJob);
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCaptureRankJobTemp", method = RequestMethod.POST)
    public ResponseEntity<?> updateCaptureRankJobTemp(@RequestBody CaptureRankJobCriteria captureRankJobCriteria) {
        String userName = captureRankJobCriteria.getUserName();
        String password = captureRankJobCriteria.getPassword();
        CaptureRankJob captureRankJob = captureRankJobCriteria.getCaptureRankJob();
        try {
            if (validUser(userName, password)) {
                captureRankJobService.completeCaptureRankJobTemp(captureRankJob);
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCaptureRankJobTempTwo2", method = RequestMethod.POST)
    public ResponseEntity<?> updateCaptureRankJobTempTwo(@RequestBody CaptureRankJobCriteria captureRankJobCriteria) {
        String userName = captureRankJobCriteria.getUserName();
        String password = captureRankJobCriteria.getPassword();
        CaptureRankJob captureRankJob = captureRankJobCriteria.getCaptureRankJob();
        try {
            if (validUser(userName, password)) {
                captureRankJobService.completeCaptureRankJobTempTwo(captureRankJob);
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCrawlRankKeyword2", method = RequestMethod.POST)
    public ResponseEntity<?> getCrawlRankKeyword(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                return new ResponseEntity<Object>(customerKeywordService.getCrawlRankKeyword(), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getNoEnteredKeyword:  " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
