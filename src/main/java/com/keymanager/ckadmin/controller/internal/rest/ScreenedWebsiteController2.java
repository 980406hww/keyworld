package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.ScreenedWebsiteCriteria;
import com.keymanager.ckadmin.entity.NegativeRank;
import com.keymanager.ckadmin.entity.ScreenedWebsite;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.ScreenedWebsiteService;
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
@RequestMapping(value = "/internal/screenedWebsite")
public class ScreenedWebsiteController2 {

    private static Logger logger = LoggerFactory.getLogger(ScreenedWebsiteController2.class);

    @Resource(name = "screenedWebsiteService2")
    private ScreenedWebsiteService screenedWebsiteService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @RequiresPermissions("/internal/screenedWebsite/searchScreenedWebsiteLists")
    @GetMapping(value = "/toScreenedWebsites")
    public ModelAndView toScreenedWebsites() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("screenedWebsites/screenedWebsite");
        return mv;
    }

    @RequiresPermissions("/internal/screenedWebsite/searchScreenedWebsiteLists")
    @PostMapping(value = "/getScreenedWebsites")
    public ResultBean getScreenedWebsites(@RequestBody ScreenedWebsiteCriteria screenedWebsiteCriteria) {
        ResultBean resultBean = new ResultBean();
        if (SQLFilterUtils.sqlInject(screenedWebsiteCriteria.toString())) {
            resultBean.setCode(400);
            resultBean.setMsg("查询参数错误或包含非法字符，请检查后重试！");
            return resultBean;
        }
        try {
            Page<ScreenedWebsite> page = new Page<>(screenedWebsiteCriteria.getPage(), screenedWebsiteCriteria.getLimit());
            page = screenedWebsiteService.searchCustomerKeywordListsPage(page, screenedWebsiteCriteria);
            List<ScreenedWebsite> screenedWebsites = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(screenedWebsites);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }


//    @RequiresPermissions("/internal/screenedWebsite/searchScreenedWebsiteLists")
//    @RequestMapping(value = "updateNegativeRankKeyword2",method = RequestMethod.POST)
//    public ResultBean updateNegativeRankKeyword(@RequestBody NegativeRank negativeRank){
//        ResultBean resultBean = new ResultBean(200,"success");
//        try {
//            negativeRankService.updateNegativeRankKeyword(negativeRank);
//            return resultBean;
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            resultBean.setCode(400);
//            resultBean.setMsg("未知错误");
//            return resultBean;
//        }
//    }

}
