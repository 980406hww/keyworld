package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.vo.CustomerKeyWordCrawlRankdVO;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
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
 * Created by lhc on 2019/8/6.
 */
@RestController
@RequestMapping(value = "/external/crawlRanking")
public class ExternalCrawlRankAllKeywordsRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalCrawlRankAllKeywordsRestController.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @RequestMapping(value = "/getCrawlRankKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> getCrawlRankKeywords(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");

            if (validUser(userName, password)) {
                return new ResponseEntity<Object>(customerKeywordService.getCrawlRankKeywords(), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getNoEnteredKeywords:  " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCrawlRankKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> c(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                JSONArray jsonArray = JSONArray.fromObject(requestMap.get("customerKeyWordCrawlRankdVOs"));
                customerKeywordService.updateCrawlRankKeywords(JSONArray.toList(jsonArray, new CustomerKeyWordCrawlRankdVO(), new JsonConfig()));
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("updateNoEnteredKeywords:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
