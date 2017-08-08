package com.keymanager.monitoring.controller.rest;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.TSMainKeywordCriteria;
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
    private UserService userService;

    private TSMainKeyword tsMainKeyword = new TSMainKeyword();

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveTSMainKeywords(@RequestBody TSMainKeyword tsMainKeyword){
        tsMainKeywordService.saveTSMainKeyword(tsMainKeyword);
        return new ResponseEntity<Object>(tsMainKeyword, HttpStatus.OK);
    }

    @RequestMapping(value = "/saveWithAuthentication", method = RequestMethod.POST)
    public ResponseEntity<?> saveTSMainKeywords(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria){
        if(tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null) {
            User user = userService.getUser(tsMainKeywordCriteria.getUserName());
            if (user != null && user.getPassword().equals(tsMainKeywordCriteria.getPassword())) {
                tsMainKeywordService.saveTSMainKeyword(tsMainKeyword);
                return new ResponseEntity<Object>(tsMainKeyword, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/findTSMainKeywords", method = RequestMethod.GET)
    public ModelAndView findTSMainKeywords(@RequestParam(defaultValue = "1") int currentPage,@RequestParam(defaultValue="15") int displaysRecords,@RequestParam(defaultValue="") String keyword,@RequestParam(defaultValue="") String  group){
        PageInfo<TSMainKeyword> pageInfo = new PageInfo<TSMainKeyword>();
        Map<String,Object> items = new HashMap<String, Object>();
        pageInfo.setCurrentpage(currentPage);
        pageInfo.setDisplaysRecords(displaysRecords);
        items.put("pageInfo",pageInfo);
        tsMainKeyword.setKeyword(keyword);
        tsMainKeyword.setGroup(group);
        items.put("tsMainKeyword",tsMainKeyword);
        pageInfo.setTotalSize(tsMainKeywordService.getTSmainKeywordCount(tsMainKeyword));
        if (pageInfo.getCurrentpage()<1) {
            pageInfo.setCurrentpage(1);
        }
        else if (pageInfo.getCurrentpage()>=pageInfo.getTotalPage()) {
            pageInfo.setCurrentpage(pageInfo.getTotalPage());
        }
        List<TSMainKeyword> tsMainKeywords = tsMainKeywordService.searchTSMainKeywords(items);
        pageInfo.setContent(tsMainKeywords);
        Map<String,Object> searchCondition = new HashMap<String, Object>();
        searchCondition.put("keyword",keyword);
        searchCondition.put("group",group);
        pageInfo.setSearchCondition(searchCondition);
        ModelAndView modelAndView = new ModelAndView("/complaints/show");
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
    }

    @RequestMapping(value = "/findTSMainKeywords", method = RequestMethod.POST)
    public ModelAndView findTSMainKeywords(@RequestParam(defaultValue = "1") int currentPage,@RequestParam(defaultValue="15") Integer displaysRecords,HttpServletRequest httpServletRequest){
        PageInfo<TSMainKeyword> pageInfo = new PageInfo<TSMainKeyword>();
        Map<String,Object> items = new HashMap<String, Object>();
        pageInfo.setCurrentpage(currentPage);
        pageInfo.setDisplaysRecords(displaysRecords);
        items.put("pageInfo",pageInfo);
        String keyword = httpServletRequest.getParameter("itemkeywork");
        String group = httpServletRequest.getParameter("itemGroup");
        tsMainKeyword.setKeyword(keyword);
        tsMainKeyword.setGroup(group);
        items.put("tsMainKeyword",tsMainKeyword);
        pageInfo.setTotalSize(tsMainKeywordService.getTSmainKeywordCount(tsMainKeyword));
        if (pageInfo.getCurrentpage()<1) {
            pageInfo.setCurrentpage(1);
        }
        else if (pageInfo.getCurrentpage()>=pageInfo.getTotalPage()) {
            pageInfo.setCurrentpage(pageInfo.getTotalPage());
        }
        List<TSMainKeyword> tsMainKeywords = tsMainKeywordService.searchTSMainKeywords(items);
        pageInfo.setContent(tsMainKeywords);
        Map<String,Object> searchCondition = new HashMap<String, Object>();
        searchCondition.put("keyword",keyword);
        searchCondition.put("group",group);
        pageInfo.setSearchCondition(searchCondition);
        ModelAndView modelAndView = new ModelAndView("/complaints/show");
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
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
