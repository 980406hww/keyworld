package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.PositiveListCriteria;
import com.keymanager.ckadmin.entity.PositiveList;
import com.keymanager.ckadmin.service.PositiveListService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping(value = "/internal/positivelists")
public class PositiveListController {
    private static Logger logger = LoggerFactory.getLogger(PositiveListController.class);

    @Resource(name = "positiveListService2")
    private PositiveListService positiveListService;

    @RequiresPermissions("/internal/positivelists/toSearchPositiveLists")
    @RequestMapping(value = "/toSearchPositiveLists", method = RequestMethod.GET)
    public ModelAndView toSearchPositiveLists(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("positiveList/PositiveList");
        return mv;
    }

    @RequiresPermissions("/internal/positivelist/savePositiveList")
    @RequestMapping(value = "/toSavePositiveList", method = RequestMethod.GET)
    public ModelAndView toSavePositiveList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("positiveList/AddPositiveList");
        return mv;
    }

    @RequiresPermissions("/internal/positivelists/toSearchPositiveLists")
    @RequestMapping(value = "/searchPositiveLists", method = RequestMethod.POST)
    public ResultBean searchPositiveListsPost(HttpServletRequest request, @RequestBody PositiveListCriteria positiveListCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<PositiveList> page = new Page<>(positiveListCriteria.getPage(), positiveListCriteria.getLimit());
            page = positiveListService.searchPositiveLists(page, positiveListCriteria);
            List<PositiveList> positiveLists = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(positiveLists);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/positivelist/savePositiveList")
    @RequestMapping(value = "/savePositiveList", method = RequestMethod.POST)
    public ResultBean savePositiveList(@RequestBody PositiveList positiveList, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            positiveListService.savePositiveList(positiveList, null);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/getPositiveList/{uuid}", method = RequestMethod.GET)
    public ResultBean getPositiveList(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            PositiveList positiveList = positiveListService.getPositiveList(uuid);
            resultBean.setCode(200);
            resultBean.setData(positiveList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/positivelist/deletePositiveList")
    @RequestMapping(value = "/deletePositiveList/{uuid}", method = RequestMethod.POST)
    public ResultBean deletePositiveList(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            positiveListService.deletePositiveList(uuid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/positivelist/deletePositiveLists")
    @RequestMapping(value = "/deletePositiveLists", method = RequestMethod.POST)
    public ResultBean deletePositiveLists(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            positiveListService.deletePositiveLists(uuids);
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
