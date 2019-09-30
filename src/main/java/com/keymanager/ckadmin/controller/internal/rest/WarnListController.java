package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.WarnListCriteria;
import com.keymanager.ckadmin.entity.WarnList;
import com.keymanager.ckadmin.service.WarnListService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/warnlists")
public class WarnListController {
    private static Logger logger = LoggerFactory.getLogger(WarnListController.class);

    @Resource(name = "warnListService2")
    private WarnListService warnListService;

    @RequiresPermissions("/internal/warnlist/searchWarnLists")
    @RequestMapping(value = "/toSearchWarnLists", method = RequestMethod.GET)
    public ModelAndView toSearchWarnLists() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("warnList/WarnList");
        return mv;
    }

    @RequiresPermissions("/internal/warnlist/saveWarnList")
    @RequestMapping(value = "/toSaveWarnList", method = RequestMethod.GET)
    public ModelAndView toSaveWarnList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("warnList/AddWarnList");
        return mv;
    }

    @RequiresPermissions("/internal/warnlist/searchWarnLists")
    @RequestMapping(value = "/searchWarnLists", method = RequestMethod.POST)
    public ResultBean searchWarnLists(@RequestBody WarnListCriteria warnListCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<WarnList> page = new Page<>(warnListCriteria.getPage(), warnListCriteria.getLimit());
            page = warnListService.searchWarnLists(page, warnListCriteria);
            List<WarnList> warnLists = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(warnLists);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/warnlist/saveWarnList")
    @RequestMapping(value = "/saveWarnList", method = RequestMethod.POST)
    public ResultBean saveWarnList(@RequestBody WarnList warnList) {
        ResultBean resultBean = new ResultBean();
        try {
            warnListService.saveWarnList(warnList);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/getWarnList/{uuid}", method = RequestMethod.GET)
    public ResultBean getWarnList(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            WarnList warnList = warnListService.getWarnList(uuid);
            resultBean.setCode(200);
            resultBean.setData(warnList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/warnlist/deleteWarnList")
    @RequestMapping(value = "/deleteWarnList/{uuid}", method = RequestMethod.POST)
    public ResultBean deleteWarnList(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            warnListService.deleteWarnList(uuid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/warnlist/deleteWarnLists")
    @RequestMapping(value = "/deleteWarnLists", method = RequestMethod.POST)
    public ResultBean deleteWarnLists(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            warnListService.deleteWarnLists(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }
}
