package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.NegativeRelatedKeywordCriteria;
import com.keymanager.monitoring.service.NegativeRelatedKeywordService;
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

@RestController
@RequestMapping(value = "/external/negativeRelatedKeyword")
public class ExternalNegativeRelatedKeywordRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalNegativeRelatedKeywordRestController.class);

    @Autowired
    private NegativeRelatedKeywordService negativeRelatedKeywordCriteriaService;

    @RequestMapping(value = "findNegativeRelatedKeyword" , method = RequestMethod.POST)
    public ResponseEntity<?> findNegativeRelatedKeyword(@RequestBody NegativeRelatedKeywordCriteria negativeRelatedKeywordCriteria) {
        try {
            if (validUser(negativeRelatedKeywordCriteria.getUserName(), negativeRelatedKeywordCriteria.getPassword())) {
                List<String> negativeRelatedKeywords = negativeRelatedKeywordCriteriaService.findNegativeRelatedKeyword(negativeRelatedKeywordCriteria.getMainKeyword());
                return new ResponseEntity<Object>(negativeRelatedKeywords, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "saveNegativeRelatedKeyword" , method = RequestMethod.POST)
    public ResponseEntity<?> saveNegativeRelatedKeyword(@RequestBody NegativeRelatedKeywordCriteria negativeRelatedKeywordCriteria) {
        try {
            if (validUser(negativeRelatedKeywordCriteria.getUserName(), negativeRelatedKeywordCriteria.getPassword())) {
                negativeRelatedKeywordCriteriaService.saveNegativeRelatedKeyword(negativeRelatedKeywordCriteria);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "deleteNegativeRelatedKeyword" , method = RequestMethod.POST)
    public ResponseEntity<?> deleteNegativeRelatedKeyword(@RequestBody NegativeRelatedKeywordCriteria negativeRelatedKeywordCriteria) {
        try {
            if (validUser(negativeRelatedKeywordCriteria.getUserName(), negativeRelatedKeywordCriteria.getPassword())) {
                negativeRelatedKeywordCriteriaService.deleteNegativeRelatedKeywords(negativeRelatedKeywordCriteria.getRelatedKeyword());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
