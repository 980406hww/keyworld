package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.ScreenedWebsiteCriteria;
import com.keymanager.ckadmin.entity.ScreenedWebsite;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.ScreenedWebsiteService;
import com.keymanager.ckadmin.util.SQLFilterUtils;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @RequiresPermissions("/internal/screenedWebsite/searchScreenedWebsiteLists")
    @GetMapping(value = "/toScreenedWebsitesAdd")
    public ModelAndView toScreenedWebsitesAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("screenedWebsites/screenedWebsiteAdd");
        return mv;
    }

    @RequiresPermissions("/internal/screenedWebsite/searchScreenedWebsiteLists")
    @PostMapping(value = "/getScreenedWebsite/{uuid}")
    public ResultBean getScreenedWebsite(@PathVariable(name = "uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            ScreenedWebsite screenedWebsite = screenedWebsiteService.getScreenedWebsite(uuid);
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(screenedWebsite);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/screenedWebsite/searchScreenedWebsiteLists")
    @PostMapping(value = "/checkGroupExist")
    public ResultBean checkGroupExist(@RequestBody ScreenedWebsiteCriteria screenedWebsiteCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Integer screenedWebsite = screenedWebsiteService.checkGroupExist(screenedWebsiteCriteria);
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(screenedWebsite);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping(value = "/saveScreenedWebsite")
    public ResultBean saveScreenedWebsite(@RequestBody ScreenedWebsite screenedWebsite, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("username");
            String password = (String) session.getAttribute("password");
            screenedWebsiteService.saveScreenedWebsite(screenedWebsite, userName, password);
            resultBean.setCode(200);
            resultBean.setMsg("success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }


    @RequestMapping(value = "/delScreenedWebsite2", method = RequestMethod.POST)
    public ResultBean delScreenedWebsite(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("username");
            String password = (String) session.getAttribute("password");
            screenedWebsiteService.delScreenedWebsite(requestMap, userName, password);
            resultBean.setCode(200);
            resultBean.setMsg("success");
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
