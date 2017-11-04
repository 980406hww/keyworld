package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.NegativeKeywordName;
import com.keymanager.monitoring.service.*;
import com.keymanager.monitoring.vo.KeywordSimpleVO;
import com.keymanager.monitoring.vo.NegativeInfoVO;
import com.keymanager.monitoring.vo.NegativeSupportingDataVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/getNegativeSupportingData" , method = RequestMethod.POST)
    public ResponseEntity<?> getNegativeSupportingData(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if(validUser(userName, password)) {
                NegativeSupportingDataVO negativeSupportingDataVO = new NegativeSupportingDataVO();
                negativeSupportingDataVO.setContactKeywords(negativeSiteContactKeywordService.getContactKeyword());
                negativeSupportingDataVO.setExcludeKeywords(negativeExcludeKeywordService.getNegativeExcludeKeyword());
                negativeSupportingDataVO.setNegativeGroups(negativeKeywordNameService.getNegativeGroup());
                negativeSupportingDataVO.setNegativeKeywords(negativeKeywordService.getNegativeKeyword());
                return new ResponseEntity<Object>(negativeSupportingDataVO, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
        }
    }


}
