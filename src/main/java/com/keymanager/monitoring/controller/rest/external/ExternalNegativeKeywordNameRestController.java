package com.keymanager.monitoring.controller.rest.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.NegativeKeywordName;
import com.keymanager.monitoring.service.*;
import com.keymanager.monitoring.vo.KeywordSimpleVO;
import com.keymanager.monitoring.vo.NegativeInfoVO;
import com.keymanager.monitoring.vo.NegativeSupportingDataVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/external/negativeKeywordName")
public class ExternalNegativeKeywordNameRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalNegativeKeywordNameRestController.class);

    @Autowired
    private NegativeKeywordNameService negativeKeywordNameService;

    @Autowired
    private NegativeKeywordNamePositionInfoService negativeKeywordNamePositionInfoService;

    @Autowired
    private NegativeSiteContactKeywordService negativeSiteContactKeywordService;

    @Autowired
    private NegativeKeywordService negativeKeywordService;

    @Autowired
    private NegativeExcludeKeywordService negativeExcludeKeywordService;

    @Autowired
    private PerformanceService performanceService;

    @RequestMapping(value = "/getNegativeKeywordName" , method = RequestMethod.POST)
    public ResponseEntity<?> getNegativeKeywordName(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            String type = (String) requestMap.get("type");
            String group = (String) requestMap.get("group");
            if(validUser(userName, password)) {
                NegativeKeywordName negativeKeywordName = negativeKeywordNameService.getNegativeKeywordName(type, group);
                if(null != negativeKeywordName) {
                    negativeKeywordNameService.updateNegativeQueryStatus(type, negativeKeywordName.getUuid());
                    KeywordSimpleVO keywordSimpleVO = new KeywordSimpleVO();
                    keywordSimpleVO.setUuid(negativeKeywordName.getUuid());
                    keywordSimpleVO.setKeyword(negativeKeywordName.getName());
                    return new ResponseEntity<Object>(keywordSimpleVO, HttpStatus.OK);
                }
            }
            return new ResponseEntity<Object>(null, HttpStatus.OK);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getHasOfficialUrlNegativeKeywordName" , method = RequestMethod.POST)
    public ResponseEntity<?> getHasOfficialUrlNegativeKeywordName(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            String group = (String) requestMap.get("group");
            if(validUser(userName, password)) {
                NegativeKeywordName negativeKeywordName = negativeKeywordNameService.getHasOfficialUrlNegativeKeywordName(group);
                if(null != negativeKeywordName) {
                    negativeKeywordNameService.updateNegativeOfficialUrlCaptured(negativeKeywordName.getUuid());
                    KeywordSimpleVO keywordSimpleVO = new KeywordSimpleVO();
                    keywordSimpleVO.setUuid(negativeKeywordName.getUuid());
                    keywordSimpleVO.setKeyword(negativeKeywordName.getName());
                    keywordSimpleVO.setOfficialUrl(negativeKeywordName.getOfficialUrl());
                    return new ResponseEntity<Object>(keywordSimpleVO, HttpStatus.OK);
                }
            }
            return new ResponseEntity<Object>(null, HttpStatus.OK);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateNegativeInfo" , method = RequestMethod.POST)
    public ResponseEntity<?> updateNegativeInfo(@RequestBody NegativeInfoVO negativeInfoVO) {
        try {
            if(validUser(negativeInfoVO.getUserName(), negativeInfoVO.getPassword())) {
                negativeKeywordNamePositionInfoService.insertPositionInfo(negativeInfoVO);
                negativeKeywordNameService.updateNegativeKeywordName(negativeInfoVO);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/addNegativeEmail" , method = RequestMethod.POST)
    public ResponseEntity<?> addNegativeEmail(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            Integer uuid = (Integer) requestMap.get("uuid");
            String emailAddress = (String) requestMap.get("emailAddress");
            if(validUser(userName, password)) {
                negativeKeywordNameService.updateEmailByUuid((long)uuid, emailAddress);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getNegativeSupportingData" , method = RequestMethod.POST)
    public ResponseEntity<?> getNegativeSupportingData(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if(validUser(userName, password)) {
                long startMilleSeconds = System.currentTimeMillis();
                NegativeSupportingDataVO negativeSupportingDataVO = new NegativeSupportingDataVO();
                negativeSupportingDataVO.setContactKeywords(negativeSiteContactKeywordService.getContactKeyword());
                negativeSupportingDataVO.setExcludeKeywords(negativeExcludeKeywordService.getNegativeExcludeKeyword());
                negativeSupportingDataVO.setNegativeGroups(negativeKeywordNameService.getNegativeGroup());
                negativeSupportingDataVO.setNegativeKeywords(negativeKeywordService.getNegativeKeyword());
                performanceService.addPerformanceLog("getNegativeSupportingData", System.currentTimeMillis() - startMilleSeconds, "");
                return new ResponseEntity<Object>(negativeSupportingDataVO, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getNegativeSupportingDataZip" , method = RequestMethod.POST)
    public ResponseEntity<?> getNegativeSupportingDataZip(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if(validUser(userName, password)) {
                long startMilleSeconds = System.currentTimeMillis();
                NegativeSupportingDataVO negativeSupportingDataVO = new NegativeSupportingDataVO();
                negativeSupportingDataVO.setContactKeywords(negativeSiteContactKeywordService.getContactKeyword());
                negativeSupportingDataVO.setExcludeKeywords(negativeExcludeKeywordService.getNegativeExcludeKeyword());
                negativeSupportingDataVO.setNegativeGroups(negativeKeywordNameService.getNegativeGroup());
                negativeSupportingDataVO.setNegativeKeywords(negativeKeywordService.getNegativeKeyword());
                ObjectMapper mapper = new ObjectMapper();
                String result = mapper.writeValueAsString(negativeSupportingDataVO);
                performanceService.addPerformanceLog("getNegativeSupportingData", System.currentTimeMillis() - startMilleSeconds, "");
                return new ResponseEntity<Object>(result, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/saveNeativeKeyword" , method = RequestMethod.POST)
    public ResponseEntity<?> saveNeativeKeyword(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            String neativeKeyword = (String) requestMap.get("neativeKeyword");
            Boolean isOk = true;
            if(validUser(userName, password)) {
                List<String> neativeKeywordList = negativeKeywordService.getNegativeKeyword();
                for (String neativeKeywordName: neativeKeywordList) {
                    if(neativeKeywordName.equals(neativeKeyword)){
                        isOk = false;
                        return new ResponseEntity<Object>(false, HttpStatus.OK);
                    }
                }
                if(isOk){
                    negativeKeywordService.saveNeativeKeyword(neativeKeyword);
                    return new ResponseEntity<Object>(true, HttpStatus.OK);
                }
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }
    }

}
