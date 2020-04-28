package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.NegativeRankCriteria;
import com.keymanager.ckadmin.entity.NegativeRank;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.NegativeRankService;
import com.keymanager.ckadmin.util.SQLFilterUtils;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName NegativeRankController2
 * @Description 负面排名
 * @Author lhc
 * @Date 2019/10/15 11:31
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/internal/negativeRank")
public class NegativeRankController2 {

    private static Logger logger = LoggerFactory.getLogger(NegativeRankController2.class);

    @Resource(name = "negativeRankService2")
    private NegativeRankService negativeRankService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @GetMapping(value = "/toNegativeRanks")
    public ModelAndView toNegativeRanks() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("negativeRanks/negativeRank");
        return mv;
    }

    @PostMapping(value = "/getNegativeRanks")
    public ResultBean getNegativeRanks(@RequestBody NegativeRankCriteria negativeRankCriteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(negativeRankCriteria.getInit())) {
            return resultBean;
        }
        if (SQLFilterUtils.sqlInject(negativeRankCriteria.toString())) {
            resultBean.setCode(400);
            resultBean.setMsg("查询参数错误或包含非法字符，请检查后重试！");
            return resultBean;
        }
        try {
            Page<NegativeRank> page = new Page<>(negativeRankCriteria.getPage(), negativeRankCriteria.getLimit());
            page = negativeRankService.searchNegativeRanks(page, negativeRankCriteria);
            List<NegativeRank> customers = page.getRecords();
            resultBean.setCount(page.getTotal());
            resultBean.setData(customers);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping(value = "/getNegativeKeywords")
    public ResultBean getNegativeKeywords() {
        ResultBean resultBean = new ResultBean();
        try {
            Set<String> keywords=configService.getNegativeKeyword();
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(keywords);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativeRank/toNegativeRanks")
    @RequestMapping(value = "/updateNegativeRankKeyword2",method = RequestMethod.POST)
    public ResultBean updateNegativeRankKeyword(@RequestBody NegativeRank negativeRank){
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            negativeRankService.updateNegativeRankKeyword(negativeRank);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

}
