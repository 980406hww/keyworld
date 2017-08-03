package com.keymanager.monitoring.controller.rest;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.criteria.TSMainKeywordCriteria;
import com.keymanager.monitoring.dao.TSMainKeywordDao;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.entity.TSMainKeyword;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.TSMainKeywordService;
import com.keymanager.monitoring.service.TSNegativeKeywordService;
import com.keymanager.monitoring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
@RestController
@RequestMapping(value = "/spring/complaints")
public class TSMainKeywordRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(TSMainKeywordRestController.class);

    @Autowired
    private TSMainKeywordService tsMainKeywordService;

    @Autowired
    private TSNegativeKeywordService tsNegativeKeywordService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveTSMainKeyword(@RequestBody TSMainKeyword tsMainKeyword){
        tsMainKeywordService.saveTSMainKeyword(tsMainKeyword);
        return new ResponseEntity<Object>(tsMainKeyword, HttpStatus.OK);
    }

    @RequestMapping(value = "/findTSMainKeywords", method = RequestMethod.GET)
    public ModelAndView findTSMainKeywords(){
        List<TSMainKeyword> tsMainKeywords = tsMainKeywordService.searchTSMainKeywords(null,null,null);
        ModelAndView modelAndView = new ModelAndView("/complaints/show");
        modelAndView.addObject("tsMainKeywords",tsMainKeywords);
        return modelAndView;
    }
//    @RequestMapping(value = "/findTSMainKeywords", method = RequestMethod.POST)
//    public ResponseEntity<?> findTSMainKeywords(@RequestBody Map<String, Object> requestMap){
//        Long uuid = (Long) requestMap.get("uuid");
//        String keyword = (String) requestMap.get("keyword");
//        String group = (String) requestMap.get("group");
//        return new ResponseEntity<Object>(tsMainKeywordService.searchTSMainKeywords(uuid, keyword, group), HttpStatus.OK);
//    }

    @RequestMapping(value = "/findTSMainKeywordById/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> findTSMainKeyword(@PathVariable("uuid") Long uuid){
        return new ResponseEntity<Object>(tsMainKeywordService.searchTSMainKeywords(uuid, null, null), HttpStatus.OK);
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
          TSMainKeyword tsMainKeyword = tsMainKeywordService.getTsMainKeywordsForComplaints();
          return new ResponseEntity<Object>(tsMainKeyword, HttpStatus.OK);
        }
      }
      return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateTsMainKeywordsForComplaints", method = RequestMethod.POST)
    public ResponseEntity<?> updateTsMainKeywordsForComplaints(){
      return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

}
