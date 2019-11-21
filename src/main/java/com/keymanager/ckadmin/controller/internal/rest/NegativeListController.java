package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.NegativeListCriteria;
import com.keymanager.ckadmin.entity.NegativeList;
import com.keymanager.ckadmin.service.NegativeListService;
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
@RequestMapping(value = "/internal/negativelists")
public class NegativeListController {

    private static Logger logger = LoggerFactory.getLogger(NegativeListController.class);

    @Resource(name = "negativeListService2")
    private NegativeListService negativeListService;

    @RequiresPermissions("/internal/negativelists/toSearchNegativeLists")
    @RequestMapping(value = "/toSearchNegativeLists", method = RequestMethod.GET)
    public ModelAndView toSearchPositiveLists() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("negativeList/NegativeList");
        return mv;
    }

    @RequiresPermissions("/internal/negativelist/saveNegativeList")
    @RequestMapping(value = "/toSaveNegativeList", method = RequestMethod.GET)
    public ModelAndView toSaveNegativeList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("negativeList/AddNegativeList");
        return mv;
    }

    @RequiresPermissions("/internal/negativelists/toSearchNegativeLists")
    @RequestMapping(value = "/searchNegativeLists", method = RequestMethod.POST)
    public ResultBean searchNegativeLists(@RequestBody NegativeListCriteria negativeListCriteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(negativeListCriteria.getInit())) {
            return resultBean;
        }
        try {
            Page<NegativeList> page = new Page<>(negativeListCriteria.getPage(), negativeListCriteria.getLimit());
            page = negativeListService.searchNegativeLists(page, negativeListCriteria);
            List<NegativeList> negativeList = page.getRecords();
            resultBean.setCount(page.getTotal());
            resultBean.setData(negativeList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativelist/saveNegativeList")
    @RequestMapping(value = "/saveNegativeList", method = RequestMethod.POST)
    public ResultBean saveNegativeList(@RequestBody NegativeList negativeList) {
        ResultBean resultBean = new ResultBean();
        try {
            negativeListService.saveNegativeList(negativeList);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativelist/deleteNegativeLists")
    @RequestMapping(value = "/deleteNegativeLists", method = RequestMethod.POST)
    public ResultBean deleteNegativeLists(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            negativeListService.deleteAll(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativelist/deleteNegativeList")
    @RequestMapping(value = "/deleteNegativeList/{uuid}", method = RequestMethod.POST)
    public ResultBean deleteNegativeList(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            NegativeList negativeList = negativeListService.selectById(uuid);
            negativeListService.deleteNegativeList(uuid, negativeList);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/getNegativeList/{uuid}", method = RequestMethod.GET)
    public ResultBean getNegativeList(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            NegativeList negativeList = negativeListService.getNegativeList(uuid);
            resultBean.setCode(200);
            resultBean.setData(negativeList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativelist/evictAllNegativeListCache")
    @RequestMapping(value = "/evictAllNegativeListCache", method = RequestMethod.GET)
    public ResultBean evictAllNegativeListCache() {
        ResultBean resultBean = new ResultBean();
        try {
            negativeListService.evictAllNegativeListCache();
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
        }
        return resultBean;
    }
}
