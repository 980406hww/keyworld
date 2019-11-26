package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.KeywordInfoCriteria;
import com.keymanager.ckadmin.entity.KeywordInfo;
import com.keymanager.ckadmin.service.KeywordInfoService;
import com.keymanager.util.TerminalTypeMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/keywordsInfo")
public class KeywordsInfoController {

    @Resource(name = "keywordInfoService2")
    private KeywordInfoService keywordInfoService;

    private static Logger logger = LoggerFactory.getLogger(KeywordsInfoController.class);

    @RequestMapping(value = "/toKeywordsInfo", method = RequestMethod.GET)
    public ModelAndView toKeywordsInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywordsInfo/keywordsInfo");
        return mv;
    }

    @RequestMapping(value = "/getKeywordsInfoData", method = RequestMethod.POST)
    public ResultBean getKeywordsInfoData(@RequestBody KeywordInfoCriteria criteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(criteria.getInit())) {
            return resultBean;
        }
        try {
            Page<KeywordInfo> page = keywordInfoService.searchKeywordInfos(criteria);
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
