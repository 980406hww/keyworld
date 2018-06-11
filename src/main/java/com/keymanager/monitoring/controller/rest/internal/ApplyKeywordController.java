package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplyKeywordCriteria;
import com.keymanager.monitoring.entity.ApplyInfo;
import com.keymanager.monitoring.entity.ApplyKeyword;
import com.keymanager.monitoring.service.ApplyInfoService;
import com.keymanager.monitoring.service.ApplyKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@RestController
@RequestMapping(value = "/internal/applyKeyword")
public class ApplyKeywordController {
    private static Logger logger = LoggerFactory.getLogger(ApplyKeywordController.class);

    @Autowired
    private ApplyKeywordService applyKeywordService;

    @Autowired
    private ApplyInfoService applyInfoService;

    @RequestMapping(value = "/searchApplyKeyword", method = RequestMethod.GET)
    public ModelAndView searchApplyKeyword(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize,
                                        HttpServletRequest request) {
        return constructApplyKeywordModelAndView(request,new ApplyKeywordCriteria(),currentPageNumber, pageSize);
    }

    @RequestMapping(value = "/searchApplyKeyword", method = RequestMethod.POST)
    public ModelAndView searchApplyKeyword(ApplyKeywordCriteria applyKeywordCriteria,HttpServletRequest request) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "30";
            }
            return constructApplyKeywordModelAndView(request, applyKeywordCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private ModelAndView constructApplyKeywordModelAndView(HttpServletRequest request, ApplyKeywordCriteria applyKeywordCriteria , int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/applyKeyword/applyKeywordList");
        List<ApplyInfo> applyInfoList = applyInfoService.selectApplyInfoList();
        Page<ApplyKeyword> page = applyKeywordService.searchApplyKeyword(new Page<ApplyKeyword>(currentPageNumber,pageSize),applyKeywordCriteria);
        modelAndView.addObject("applyInfoList",applyInfoList);
        modelAndView.addObject("applyKeywordCriteria",applyKeywordCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequestMapping(value = "/deleteApplyKeyword/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteSupplier(@PathVariable("uuid") Long uuid) {
        try {
            applyKeywordService.deleteSupplier(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deleteApplyKeywordList" , method = RequestMethod.POST)
    public ResponseEntity<?> deleteApplyKeywordList(@RequestBody Map<String, Object> requestMap){
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            applyKeywordService.deleteApplyKeywordList(uuids);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/saveapplyKeyword" , method = RequestMethod.POST)
    public ResponseEntity<?> saveapplyKeyword(HttpServletRequest request){
        try {
            String uuid = (String)request.getParameter("uuid");
            String keywords = (String)request.getParameter("keywords");
            Long applyUuid = (Integer.valueOf((String)request.getParameter("applyUuid"))).longValue();
            String applyName = (String)request.getParameter("applyName");
            applyKeywordService.saveApplyKeyword(uuid,keywords,applyUuid,applyName);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getApplyKeyword/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getApplyKeyword(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(applyKeywordService.selectById(uuid), HttpStatus.OK);
    }
}
