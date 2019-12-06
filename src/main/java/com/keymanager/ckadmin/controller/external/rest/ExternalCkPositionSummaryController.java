package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.service.CustomerKeywordPositionSummaryService;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shunshikj40
 */
@RestController
@RequestMapping("/external/ckPositionSummary")
public class ExternalCkPositionSummaryController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalCkPositionSummaryController.class);

    @Resource(name = "ckPositionSummaryService2")
    private CustomerKeywordPositionSummaryService customerKeywordPositionSummaryService;

    @PostMapping(value = "/handleCkPositionHistoryData")
    public ResponseEntity<?> handleCkPositionHistoryData(@RequestBody Map<String, String> requestMap) {
        try {
            String userName = requestMap.get("userName");
            String password = requestMap.get("password");
            if (validUser(userName, password)) {
                customerKeywordPositionSummaryService.handleCkPositionHistoryData();
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
