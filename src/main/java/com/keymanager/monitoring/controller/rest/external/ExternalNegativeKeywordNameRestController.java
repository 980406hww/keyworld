package com.keymanager.monitoring.controller.rest.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.NegativeKeywordNameCriteria;
import com.keymanager.monitoring.entity.NegativeExcludeKeyword;
import com.keymanager.monitoring.entity.NegativeKeyword;
import com.keymanager.monitoring.entity.NegativeKeywordName;
import com.keymanager.monitoring.entity.NegativeKeywordNamePositionInfo;
import com.keymanager.monitoring.service.*;
import com.keymanager.monitoring.vo.KeywordSimpleVO;
import com.keymanager.monitoring.vo.NegativeInfoVO;
import com.keymanager.monitoring.vo.NegativeSupportingDataVO;
import com.keymanager.util.Utils;
import org.apache.ibatis.annotations.ResultMap;
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

    @RequestMapping(value = "/getNegativeKeywordName" , method = RequestMethod.POST)
    public ResponseEntity<?> getNegativeKeywordName(@RequestBody Map<String, Object> requestMap) {
        try {
            String type = (String) requestMap.get("type");
            String group = (String) requestMap.get("group");
            NegativeKeywordName negativeKeywordName = negativeKeywordNameService.getNegativeKeywordName(type, group);
            negativeKeywordNameService.updateNegativeQueryStatus(type, negativeKeywordName.getUuid());
            KeywordSimpleVO keywordSimpleVO = new KeywordSimpleVO();
            keywordSimpleVO.setUuid(negativeKeywordName.getUuid());
            keywordSimpleVO.setKeyword(negativeKeywordName.getName());
            ObjectMapper mapper = new ObjectMapper();
            return new ResponseEntity<Object>(mapper.writeValueAsString(keywordSimpleVO), HttpStatus.OK);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>("",HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateNegativeInfo" , method = RequestMethod.POST)
    public ResponseEntity<?> updateNegativeInfo(@RequestBody Map<String, Object> requestMap) {
        try {
            String type = (String) requestMap.get("type");
            ObjectMapper mapper = new ObjectMapper();
            NegativeInfoVO negativeInfoVO = (NegativeInfoVO)mapper.readValue((String)requestMap.get("data"), NegativeInfoVO.class);
            negativeKeywordNamePositionInfoService.insertPositionInfo(type, negativeInfoVO);
            negativeKeywordNameService.updateOfficialUrlAndEmail(negativeInfoVO);
            negativeKeywordNameService.updateNegativeKeywordNameByType(type, negativeInfoVO);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>("",HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getNegativeSupportingData" , method = RequestMethod.GET)
    public ResponseEntity<?> getNegativeSupportingData() {
        try {
            NegativeSupportingDataVO negativeSupportingDataVO = new NegativeSupportingDataVO();
            negativeSupportingDataVO.setContactKeywords(negativeSiteContactKeywordService.getContactKeyword());
            negativeSupportingDataVO.setExcludeKeywords(negativeExcludeKeywordService.getNegativeExcludeKeyword());
            negativeSupportingDataVO.setNegativeGroups(negativeKeywordNameService.getNegativeGroup());
            negativeSupportingDataVO.setNegativeKeywords(negativeKeywordService.getNegativeKeyword());
            ObjectMapper mapper = new ObjectMapper();
            return new ResponseEntity<Object>(mapper.writeValueAsString(negativeSupportingDataVO), HttpStatus.OK);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>("",HttpStatus.BAD_REQUEST);
    }


}
