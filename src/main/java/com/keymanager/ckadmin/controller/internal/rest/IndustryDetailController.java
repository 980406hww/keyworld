package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.IndustryDetailCriteria;
import com.keymanager.ckadmin.entity.IndustryDetail;
import com.keymanager.ckadmin.service.IndustryDetailService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/internal/industryDetails")
public class IndustryDetailController {

    private static final Logger logger = LoggerFactory.getLogger(IndustryDetailController.class);

    @Resource(name = "industryDetailService2")
    private IndustryDetailService industryDetailService;

    @RequiresPermissions("/internal/industryDetail/searchIndustryDetails")
    @GetMapping("/toIndustryDetails/{industryUuid}")
    public ModelAndView toIndustryDetails(@PathVariable("industryUuid") long industryUuid) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("industryUuid", industryUuid);
        mv.setViewName("industryList/IndustryDetails");
        return mv;
    }

    @RequiresPermissions("/internal/industryDetail/saveIndustryDetail")
    @GetMapping("/toSaveIndustryDetail")
    public ModelAndView toSaveIndustryDetail() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("industryList/SaveIndustryDetail");
        return mv;
    }

    @RequiresPermissions("/internal/industryDetail/saveIndustryDetail")
    @GetMapping("/getIndustryDetail/{uuid}")
    public ResultBean getIndustryDetail(@PathVariable("uuid") long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            IndustryDetail industryDetail = industryDetailService.getIndustryDetail(uuid);
            resultBean.setData(industryDetail);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industryDetail/saveIndustryDetail")
    @PostMapping("/saveIndustryDetail")
    public ResultBean saveIndustryDetail(@RequestBody IndustryDetail industryDetail) {
        ResultBean resultBean = new ResultBean();
        try {
            industryDetailService.saveIndustryDetail(industryDetail);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industryDetail/saveIndustryDetail")
    @PostMapping("/updRemarkByUuids")
    public ResultBean updRemarkByUuids(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Long> uuids = (List<Long>) requestMap.get("uuids");
            String remark = (String) requestMap.get("remark");
            industryDetailService.updRemarkByUuids(uuids, remark);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industryDetail/delIndustryDetail")
    @GetMapping("/delIndustryDetail/{uuid}")
    public ResultBean delIndustryDetail(@PathVariable("uuid") long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            industryDetailService.delIndustryDetail(uuid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industryDetail/deleteIndustryDetails")
    @PostMapping("/deleteIndustryDetails")
    public ResultBean deleteIndustryDetails(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            industryDetailService.deleteIndustryDetails(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industryDetail/searchIndustryDetails")
    @PostMapping("/searchIndustryDetails")
    public ResultBean searchIndustryDetails(@RequestBody IndustryDetailCriteria industryDetailCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<IndustryDetail> page = new Page<>(industryDetailCriteria.getPage(), industryDetailCriteria.getLimit());
            page = industryDetailService.searchIndustryDetails(page, industryDetailCriteria);
            List<IndustryDetail> industryDetailList = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(industryDetailList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industryDetail/delIndustryDetail")
    @GetMapping("/removeUselessIndustryDetail/{industryID}")
    public ResultBean removeUselessIndustryDetail(@PathVariable("industryID") long industryID) {
        ResultBean resultBean = new ResultBean();
        try {
            industryDetailService.removeUselessIndustryDetail(industryID);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

}
