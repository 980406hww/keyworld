package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.IndustryDetailCriteria;
import com.keymanager.monitoring.entity.IndustryDetail;
import com.keymanager.monitoring.service.IndustryDetailService;
import com.keymanager.util.Constants;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 行业详情表 前端控制器
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
@RestController
@RequestMapping("/internal/industryDetail")
public class IndustryDetailRestController {

    private static final Logger logger = LoggerFactory.getLogger(IndustryDetailRestController.class);

    @Autowired
    private IndustryDetailService industryDetailService;

    @RequiresPermissions("/internal/industryDetail/searchIndustryDetails")
    @GetMapping("/searchIndustryDetails/{industryUuid}")
    public ModelAndView searchIndustryDetails(@PathVariable("industryUuid") long industryUuid,
                                              @RequestParam(defaultValue = "1") int currentPage,
                                              @RequestParam(defaultValue = "50") int displayRecords) {
        IndustryDetailCriteria industryDetailCriteria = new IndustryDetailCriteria();
        industryDetailCriteria.setIndustryID(industryUuid);
        return constructIndustryDetailModelAndView(industryDetailCriteria, currentPage + "", displayRecords + "");
    }

    @RequiresPermissions("/internal/industryDetail/searchIndustryDetails")
    @PostMapping("/searchIndustryDetails")
    public ModelAndView searchIndustryDetailsPost(HttpServletRequest request, IndustryDetailCriteria industryDetailCriteria) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "50";
            }
            return constructIndustryDetailModelAndView(industryDetailCriteria, currentPageNumber, pageSize);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/industry/industryDetailList");
        }
    }

    private ModelAndView constructIndustryDetailModelAndView(IndustryDetailCriteria industryDetailCriteria,
                                                       String currentPage, String pageSize) {
        ModelAndView modelAndView = new ModelAndView("/industry/industryDetailList");
        Page<IndustryDetail> page = industryDetailService.searchIndustryDetails(new Page<IndustryDetail>(Integer.parseInt(currentPage),
                Integer.parseInt(pageSize)), industryDetailCriteria);
        modelAndView.addObject("industryDetailCriteria", industryDetailCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("weightList", Constants.QZSETTING_WEIGHT_LIST);
        return modelAndView;
    }

    @GetMapping("/getIndustryDetail/{uuid}")
    public ResponseEntity<?> getIndustryDetail(@PathVariable("uuid") long uuid) {
        try {
            IndustryDetail industryDetail = industryDetailService.getIndustryDetail(uuid);
            return new ResponseEntity<>(industryDetail, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/saveIndustryDetail")
    public ResponseEntity<?> saveIndustryDetail(@RequestBody IndustryDetail industryDetail) {
        try {
            industryDetailService.saveIndustryDetail(industryDetail);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/delIndustryDetail/{uuid}")
    public ResponseEntity<?> delIndustryDetail(@PathVariable("uuid") long uuid) {
        try {
            industryDetailService.delIndustryDetail(uuid);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/deleteIndustryDetails")
    public ResponseEntity<?> deleteIndustryDetails(@RequestBody Map<String, Object> requestMap) {
        try {
            String uuids = (String) requestMap.get("uuids");
            industryDetailService.deleteIndustryDetails(uuids);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updateIndustryDetailRemark")
    public ResponseEntity<?> updateIndustryDetailRemark(@RequestBody Map<String, Object> requestMap) {
        try {
            long uuid = Long.valueOf((String) requestMap.get("uuid"));
            String remark = (String) requestMap.get("remark");
            industryDetailService.updateIndustryDetailRemark(uuid, remark);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
}

