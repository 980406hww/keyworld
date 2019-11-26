package com.keymanager.ckadmin.controller.internal.rest;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.SalesInfoCriteria;
import com.keymanager.ckadmin.entity.SalesManage;
import com.keymanager.ckadmin.enums.WebsiteTypeEnum;
import com.keymanager.ckadmin.service.SalesManageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/salesInfo")
public class SalesInfoController {

    private static Logger logger = LoggerFactory.getLogger(SalesManage.class);

    @Resource(name = "salesManageService2")
    private SalesManageService salesManageService;

    @RequiresPermissions("/internal/salesInfo/toSalesInfo")
    @GetMapping("/toSalesInfo")
    public ModelAndView toSalesInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("salesInfo/SalesInfoList");
        return mv;
    }

    @RequiresPermissions("/internal/salesManage/saveSalesInfo")
    @GetMapping("/toAddSalesInfo")
    public ModelAndView toAddSalesInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("salesInfo/AddSalesInfo");
        return mv;
    }

    @RequiresPermissions("/internal/salesManage/saveSalesInfo")
    @RequestMapping(value = "/saveSalesManage", method = RequestMethod.POST)
    public ResultBean saveOrUpdateSalesManage(@RequestBody SalesManage salesManage) {
        ResultBean resultBean = new ResultBean();
        try {
            if (salesManage.getUuid() == null) {
                salesManage.setCreateTime(new Date());
            }
            salesManage.setUpdateTime(new Date());
            salesManageService.insertOrUpdate(salesManage);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping("/searchSalesInfos")
    public ResultBean searchSalesInfos(@RequestBody SalesInfoCriteria salesInfoCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<SalesManage> page = new Page<>(salesInfoCriteria.getPage(), salesInfoCriteria.getLimit());
            page = salesManageService.SearchSalesManages(salesInfoCriteria, page);
            List<SalesManage> salesInfoList = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(salesInfoList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/salesManage/updateSalesInfo")
    @GetMapping("/getSalesManage/{uuid}")
    public ResultBean getSalesManage(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            SalesManage salesManage = salesManageService.getSalesManageByUuid(uuid);
            resultBean.setCode(200);
            resultBean.setData(salesManage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/salesManage/deleteSalesInfo")
    @GetMapping("/deleteSalesManage/{uuid}")
    public ResultBean deleteSalesManage(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            salesManageService.deleteSalesManage(uuid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/salesManage/deleteSalesInfo")
    @PostMapping("/deleteBatchSalesManage")
    public ResultBean deleteBatchSalesManage(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            salesManageService.deleteBatchSalesManage(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @GetMapping("/returnWebsiteTypeMap")
    public ResultBean returnWebsiteTypeMap(){
        ResultBean resultBean = new ResultBean();
        try {
            resultBean.setData(WebsiteTypeEnum.changeToMap());
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
