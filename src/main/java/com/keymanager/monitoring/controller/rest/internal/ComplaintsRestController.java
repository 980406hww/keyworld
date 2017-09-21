package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.TSMainKeywordCriteria;
import com.keymanager.monitoring.entity.TSMainKeyword;

import javax.servlet.http.HttpServletRequest;

import com.keymanager.monitoring.service.TSMainKeywordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping(value = "/internal/complaints")
public class ComplaintsRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ComplaintsRestController.class);

    @Autowired
    private TSMainKeywordService tsMainKeywordService;

    @RequiresPermissions("/internal/complaints/save")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveTSMainKeywords(@RequestBody TSMainKeyword tsMainKeyword){
        try {
            tsMainKeywordService.saveTSMainKeyword(tsMainKeyword);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/complaints/findTSMainKeywords")
    @RequestMapping(value = "/findTSMainKeywords", method = RequestMethod.GET)
    public ModelAndView findTSMainKeywords(@RequestParam(defaultValue = "1",name = "currentPage") int currentPage,@RequestParam(defaultValue="50",name = "displaysRecords") int pageSize,
                                           @RequestParam(defaultValue="") String keyword,@RequestParam(defaultValue="") String  group){
        return findTSMainKeywordsAndReturnView(currentPage,pageSize,keyword,group);
    }

    @RequiresPermissions("/internal/complaints/findTSMainKeywords")
    @RequestMapping(value = "/findTSMainKeywords", method = RequestMethod.POST)
    public ModelAndView findTSMainKeywords(HttpServletRequest httpServletRequest){
        String keyword = httpServletRequest.getParameter("itemKeyword");
        String group = httpServletRequest.getParameter("itemGroup");
        String currentPage  = httpServletRequest.getParameter("currentPageHidden");
        String pageSize = httpServletRequest.getParameter("displaysRecordsHidden");
        if (null == currentPage && null == pageSize) {
            currentPage = "1";
            pageSize = "50";
        }
        return findTSMainKeywordsAndReturnView(Integer.parseInt(currentPage),Integer.parseInt(pageSize),keyword,group);
    }

    private ModelAndView findTSMainKeywordsAndReturnView(int currentPage,int pageSize,String keyword,String  group){
        Page<TSMainKeyword> page = new Page<TSMainKeyword>(currentPage, pageSize);
        page.getCondition().put("keyword",keyword);
        page.getCondition().put("group",group);
        page = tsMainKeywordService.searchTSMainKeywords(page, keyword, group);
        ModelAndView modelAndView = new ModelAndView("/complaints/show");
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequestMapping(value = "/findTSMainKeywordById/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> findTSMainKeyword(@PathVariable("uuid") Long uuid){
        return new ResponseEntity<Object>(tsMainKeywordService.getTSMainKeyword(uuid), HttpStatus.OK);
    }

    @RequiresPermissions("/internal/complaints/delete")
    @RequestMapping(value ="/delete/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteTSMainKeyword(@PathVariable("uuid") Long uuid){
        return new ResponseEntity<Object>(tsMainKeywordService.deleteOne(uuid), HttpStatus.OK);
    }

    @RequiresPermissions("/internal/complaints/deleteTSMainKeywords")
    @RequestMapping(value = "/deleteTSMainKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> deleteTSMainKeywords(@RequestBody Map<String, Object> requestMap){
        List<String> uuids = (List<String>) requestMap.get("uuids");
        return new ResponseEntity<Object>(tsMainKeywordService.deleteAll(uuids) , HttpStatus.OK);
    }
}
