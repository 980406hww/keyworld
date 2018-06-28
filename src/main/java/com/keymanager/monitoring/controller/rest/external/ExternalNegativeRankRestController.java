package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.NegativeRankCriteria;
import com.keymanager.monitoring.entity.NegativeRank;
import com.keymanager.monitoring.service.NegativeRankService;
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
import java.util.Map;

@RestController
@RequestMapping(value = "/external/negativeRank")
public class ExternalNegativeRankRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalNegativeRankRestController.class);

    @Autowired
    private NegativeRankService negativeRankService;

    @RequestMapping(value = "/saveNegativeRanks", method = RequestMethod.POST)
    public ResponseEntity<?> saveNegativeRanks(@RequestBody NegativeRankCriteria negativeRankCriteria) {
        try {
            String userName = negativeRankCriteria.getUserName();
            String password = negativeRankCriteria.getPassword();
            if (validUser(userName, password)) {
                negativeRankService.saveNegativeRanks(negativeRankCriteria.getNegativeRanks());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/findNegativeRanks", method = RequestMethod.POST)
    public ResponseEntity<?> findNegativeRanks(@RequestBody NegativeRankCriteria negativeRankCriteria) {
        try {
            String userName = negativeRankCriteria.getUserName();
            String password = negativeRankCriteria.getPassword();
            if (validUser(userName, password)) {
                List<NegativeRank> negativeRanks = negativeRankService.findNegativeRanks(negativeRankCriteria);
                return new ResponseEntity<Object>(negativeRanks, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/findInitialNegativeRanks", method = RequestMethod.POST)
    public ResponseEntity<?> findInitialNegativeRanks(@RequestBody NegativeRankCriteria negativeRankCriteria) {
        try {
            String userName = negativeRankCriteria.getUserName();
            String password = negativeRankCriteria.getPassword();
            if (validUser(userName, password)) {
                Map<String, Object> initialNegativeRanks = negativeRankService.findInitialNegativeRanks(negativeRankCriteria);
                return new ResponseEntity<Object>(initialNegativeRanks, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
