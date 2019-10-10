package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.IndustryDetailCriteria;
import com.keymanager.ckadmin.entity.IndustryDetail;
import com.keymanager.ckadmin.service.IndustryDetailService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

}
