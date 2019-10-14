package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.NegativeKeywordNameCriteria;
import com.keymanager.ckadmin.entity.NegativeKeywordName;
import com.keymanager.ckadmin.service.NegativeKeywordNameService;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/negativeKeywords")
public class NegativeKeywordNameController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(NegativeKeywordNameController.class);

    @Resource(name = "negativeKeywordNameService2")
    private NegativeKeywordNameService negativeKeywordNameService;

    @RequiresPermissions("/internal/negativelist/searchNegativeLists")
    @RequestMapping(value = "/toNegativeKeywords", method = RequestMethod.GET)
    public ModelAndView toSearchPositiveLists() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("negativesKeywords/negativeKeywords");
        return mv;
    }

    @RequiresPermissions("/internal/negativeKeywordName/searchNegativeKeywordNames")
    @RequestMapping(value = "/getNegativeKeywords", method = RequestMethod.POST)
    public ResultBean searchNegativeKeywordNamesPost(@RequestBody NegativeKeywordNameCriteria criteria) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        try {
            Page<NegativeKeywordName> page = negativeKeywordNameService.searchNegativeKeywordNames(criteria);
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativeKeywordName/searchNegativeKeywordNames")
    @RequestMapping(value = "/getNegativeGroup", method = RequestMethod.GET)
    public ResultBean getGroups() {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        try {
            resultBean.setData(negativeKeywordNameService.getGroups());
            resultBean.setMsg("success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
