package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.criteria.CaptureRankJobCriteria;
import com.keymanager.monitoring.criteria.ExternalCaptureJobCriteria;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.service.CaptureRankJobService;
import com.keymanager.monitoring.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/27.
 */
@RestController
@RequestMapping(value = "/external/crawlRanking")
public class ExternalCrawlRankingRsetController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalCrawlRankingRsetController.class);

    @Autowired
    private CaptureRankJobService captureRankJobService;

    @RequestMapping(value = "/getCaptureRankJob", method = RequestMethod.POST)
    public ResponseEntity<?> getCaptureRankJob(@RequestBody ExternalCaptureJobCriteria captureJobCriteria) {
        String userName = captureJobCriteria.getUserName();
        String password = captureJobCriteria.getPassword();
        try {
            if (validUser(userName, password)) {
                CaptureRankJob captureRankJob = captureRankJobService.provideCaptureRankJob(captureJobCriteria);
                if(captureRankJob == null) {
                    captureRankJob = new CaptureRankJob();
                    captureRankJob.setGroupNames("end");
                }
                return new ResponseEntity<Object>(captureRankJob, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

//    @RequestMapping(value = "/getCaptureRankJobForXiala", method = RequestMethod.POST)
//    public ResponseEntity<?> getCaptureRankJobForXiala(@RequestBody BaseCriteria baseCriteria) {
//        String userName = baseCriteria.getUserName();
//        String password = baseCriteria.getPassword();
//        try {
//            if (validUser(userName, password)) {
//                // 取任务的时候先检查checking状态的任务
//                // captureRankJobService.searchFiveMiniSetCheckingJobs();
//                List<String> captureXialaRankingGroups = configService.getCaptureXialaRankingGroups();
//                CaptureRankJob captureRankJob = captureRankJobService.provideCaptureRankJob(captureXialaRankingGroups, null);
//                if(captureRankJob == null) {
//                    captureRankJob = new CaptureRankJob();
//                    captureRankJob.setGroupNames("end");
//                }
//                return new ResponseEntity<Object>(captureRankJob, HttpStatus.OK);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
//    }

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
}
