package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.CaptureRankJobCriteria;
import com.keymanager.ckadmin.criteria.ExternalCaptureJobCriteria;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import com.keymanager.ckadmin.service.CaptureRankJobService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/external/crawlRanking")
public class ExternalCrawlRankingController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalCrawlRankingController.class);

    @Resource(name = "captureRankJobService2")
    private CaptureRankJobService captureRankJobService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

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
