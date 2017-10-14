package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.entity.NegativeExcludeKeyword;
import com.keymanager.monitoring.entity.NegativeKeyword;
import com.keymanager.monitoring.service.NegativeExcludeKeywordService;
import com.keymanager.monitoring.service.NegativeKeywordService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj24 on 2017/10/13.
 */
@RestController
@RequestMapping(value = "/external/negativeKeyword")
public class ExternalNegativeKeyword {
    private static Logger logger = LoggerFactory.getLogger(ExternalNegativeKeyword.class);

    @Autowired
    private NegativeKeywordService negativeKeywordService;

    @Autowired
    private NegativeExcludeKeywordService negativeExcludeKeywordService;


    @RequestMapping(value = "/searchNegativeKeywords", method = RequestMethod.GET)
    public ResponseEntity<?> getCaptureRankJob() {
        try {
            Map<String,Object> NegativeKeyword=new HashMap<String,Object>();
            List<NegativeKeyword> negativeKeywords = negativeKeywordService.selectList(null);
            NegativeKeyword.put("negativeKeywords",negativeKeywords);
            List<NegativeExcludeKeyword> negativeExcludeKeywords = negativeExcludeKeywordService.selectList(null);
            NegativeKeyword.put("negativeExcludeKeywords",negativeExcludeKeywords);
            return new ResponseEntity<Object>(NegativeKeyword,HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

}
