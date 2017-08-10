package com.keymanager.monitoring.controller.rest;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.TSMainKeywordCriteria;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.service.TSComplainLogService;
import com.keymanager.monitoring.entity.TSMainKeyword;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.TSMainKeywordService;
import com.keymanager.monitoring.service.TSNegativeKeywordService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.monitoring.vo.PageInfo;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
@RestController
@RequestMapping(value = "/spring/complaints")
public class ComplaintsRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ComplaintsRestController.class);

    @Autowired
    private TSMainKeywordService tsMainKeywordService;

    @Autowired
    private TSNegativeKeywordService tsNegativeKeywordService;

    @Autowired
    private TSComplainLogService tsComplainLogService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveTSMainKeywords(@RequestBody TSMainKeyword tsMainKeyword){
        try {
            tsMainKeywordService.saveTSMainKeyword(tsMainKeyword);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/saveWithAuthentication", method = RequestMethod.POST)
    public ResponseEntity<?> saveTSMainKeywords(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria){
        if(tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null) {
            User user = userService.getUser(tsMainKeywordCriteria.getUserName());
            if (user != null && user.getPassword().equals(tsMainKeywordCriteria.getPassword())) {
                tsMainKeywordService.saveTSMainKeyword(tsMainKeywordCriteria.getTsMainKeyword());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/findTSMainKeywords", method = RequestMethod.GET)
    public ModelAndView findTSMainKeywords(@RequestParam(defaultValue = "1") int currentPage,@RequestParam(defaultValue="25") int displaysRecords,@RequestParam(defaultValue="") String keyword,@RequestParam(defaultValue="") String  group){

        return tsMainKeywordService.findTSMainKeywordsCode(currentPage,displaysRecords,keyword,group);
    }

    @RequestMapping(value = "/findTSMainKeywords", method = RequestMethod.POST)
    public ModelAndView findTSMainKeywords(@RequestParam(defaultValue = "1") int currentPage,@RequestParam(defaultValue="25") int displaysRecords,HttpServletRequest httpServletRequest){
        String keyword = httpServletRequest.getParameter("itemkeywork");
        String group = httpServletRequest.getParameter("itemGroup");
        return tsMainKeywordService.findTSMainKeywordsCode(currentPage,displaysRecords,keyword,group);
    }




    @RequestMapping(value = "/findTSMainKeywordById/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> findTSMainKeyword(@PathVariable("uuid") Long uuid){
        return new ResponseEntity<Object>(tsMainKeywordService.searchTSMainKeyword(uuid), HttpStatus.OK);
    }

    @RequestMapping(value ="/delete/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteTSMainKeyword(@PathVariable("uuid") Long uuid){
        return new ResponseEntity<Object>(tsMainKeywordService.deleteOne(uuid), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteTSMainKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> deleteTSMainKeywords(@RequestBody Map<String, Object> requestMap){
        List<String> uuids = (List<String>) requestMap.get("uuids");
        return new ResponseEntity<Object>(tsMainKeywordService.deleteAll(uuids) , HttpStatus.OK);
    }

    //提供爬虫使用
    @RequestMapping(value = "/getTsMainKeywordsForComplaints", method = RequestMethod.POST)
    public ResponseEntity<?> getTsMainKeywordsForComplaints(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria) throws Exception{
        if(tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null){
            User user = userService.getUser(tsMainKeywordCriteria.getUserName());
            if(user != null && user.getPassword().equals(tsMainKeywordCriteria.getPassword())){
                String ipCity = tsMainKeywordCriteria.getIpCity();
                TSMainKeyword tsMainKeyword = tsMainKeywordService.getTsMainKeywordsForComplaints(ipCity);
                return new ResponseEntity<Object>(tsMainKeyword, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateTsMainKeywordsForComplaints", method = RequestMethod.POST)
    public ResponseEntity<?> updateTsMainKeywordsForComplaints(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria){
        if(tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null){
            User user = userService.getUser(tsMainKeywordCriteria.getUserName());
            if(user != null && user.getPassword().equals(tsMainKeywordCriteria.getPassword())){
                List<TSNegativeKeyword> oldNegativeKeywordList = tsNegativeKeywordService.findNegativeKeywordsByMainKeywordUuid(tsMainKeywordCriteria.getTsMainKeyword().getUuid());
                // 更新Negative
                tsNegativeKeywordService.exchangeNegativeKeywordsData(tsMainKeywordCriteria.getTsMainKeyword().getTsNegativeKeywords());
                // 添加Log
                tsComplainLogService.addComplainLogByNegativeKeywords(tsMainKeywordCriteria.getTsMainKeyword().getTsNegativeKeywords());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }

}
