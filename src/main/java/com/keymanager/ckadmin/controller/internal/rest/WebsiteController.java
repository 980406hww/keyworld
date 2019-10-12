package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.WebsiteCriteria;
import com.keymanager.ckadmin.entity.Website;
import com.keymanager.ckadmin.service.WebsiteService;
import com.keymanager.ckadmin.vo.WebsiteVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/internal/websites")
public class WebsiteController {
    private static final Logger logger = LoggerFactory.getLogger(WebsiteController.class);

    @Resource(name = "websiteService2")
    private WebsiteService websiteService;

    @RequiresPermissions("/internal/website/searchWebsites")
    @GetMapping("/toWebSiteList")
    public ModelAndView toWebSiteList(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("WebSiteList/WebSiteList");
        return mv;
    }

    @RequiresPermissions("/internal/website/searchWebsites")
    @PostMapping(value = "/searchWebsites")
    public ResultBean searchWebsitesPost(@RequestBody WebsiteCriteria websiteCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<WebsiteVO> page = new Page<>(websiteCriteria.getPage(), websiteCriteria.getLimit());
            page = websiteService.searchWebsites(page, websiteCriteria);
            List<WebsiteVO> websiteList = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(websiteList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

}
