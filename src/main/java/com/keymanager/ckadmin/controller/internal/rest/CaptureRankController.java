package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import com.keymanager.ckadmin.enums.RankJobAreaEnum;
import com.keymanager.ckadmin.service.CaptureRankJobService;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/captureRanks")
public class CaptureRankController {

    private static Logger logger = LoggerFactory.getLogger(CaptureRankController.class);

    @Resource(name = "captureRankJobService2")
    private CaptureRankJobService captureRankJobService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @RequiresPermissions("/internal/captureRanks/toCaptureRank")
    @GetMapping(value = "/toCaptureRank")
    public ModelAndView toCaptureRank() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("captureRanks/captureRank");
        return mv;
    }

    @RequiresPermissions("/internal/captureRanks/toCaptureRank")
    @RequestMapping(value = "/getCaptureRankJobs", method = RequestMethod.POST)
    public ResultBean searchCaptureRankingJobs(@RequestBody CaptureRankJobSearchCriteria criteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(criteria.getInit())) {
            return resultBean;
        }
        criteria.setRankJobType(null == criteria.getRankJobType() || "".equals(criteria.getRankJobType()) ? "Common" : criteria.getRankJobType());
        try {
            Page<CaptureRankJob> page = captureRankJobService.selectPageByCriteria(criteria);
            resultBean.setCount(page.getTotal());
            resultBean.setData(page.getRecords());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/captureRanks/toCaptureRank")
    @RequestMapping(value = "/getInitData", method = RequestMethod.GET)
    public ResultBean getInitData() {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        Map<String, Object> data = new HashMap<>(2);
        try {
            data.put("rankJobArea", RankJobAreaEnum.changeToMap());
            data.put("rankJobCityList", configService.getRankJobCity());
            resultBean.setData(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/captureRank/saveCaptureRankJob")
    @GetMapping(value = "/toSaveCaptureRanks")
    public ModelAndView toSaveCaptureRanks() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("captureRanks/captureRankAdd");
        return mv;
    }

    @RequiresPermissions("/internal/captureRank/saveCaptureRankJob")
    @RequestMapping(value = "/getInitDataForAdd/{terminal}", method = RequestMethod.GET)
    public ResultBean getInitDataForAdd(@PathVariable String terminal) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        Map<String, Object> data = new HashMap<>(2);
        try {
            data.put("rankJobArea", RankJobAreaEnum.changeToMap());
            data.put("rankJobCityList", configService.getRankJobCity());
            data.put("groups", customerKeywordService.searchGroupsByTerminalType(terminal));
            resultBean.setData(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/captureRank/saveCaptureRankJob")
    @RequestMapping(value = "/saveCaptureRankJob", method = RequestMethod.POST)
    public ResultBean saveCaptureRankJob(@RequestBody Map map, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String loginName = request.getSession().getAttribute("username").toString();
            captureRankJobService.saveCaptureRankJob(map, loginName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/captureRank/saveCaptureRankJob")
    @RequestMapping(value = "/getCaptureRankJob/{uuid}", method = RequestMethod.GET)
    public ResultBean getCaptureRankJob(@PathVariable Long uuid) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            resultBean.setData(captureRankJobService.getCaptureRankJobAndCustomerName(uuid));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/captureRank/deleteCaptureRankJob")
    @RequestMapping(value = "/deleteCaptureRankJob/{uuid}", method = RequestMethod.GET)
    public ResultBean deleteCaptureRankJob(@PathVariable Long uuid) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            captureRankJobService.deleteById(uuid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/captureRank/deleteCaptureRankJobs")
    @RequestMapping(value = "/deleteCaptureRankJobs", method = RequestMethod.POST)
    public ResultBean deleteCaptureRankJobs(@RequestBody List<Long> uuids) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            for (Long uuid : uuids) {
                captureRankJobService.deleteById(uuid);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/captureRank/changeCaptureRankJobStatus")
    @RequestMapping(value = "/changeCaptureRankJobStatus", method = RequestMethod.POST)
    public ResultBean changeCaptureRankJobStatus(@RequestBody CaptureRankJob captureRankJob, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            String loginName = request.getSession().getAttribute("username").toString();
            captureRankJobService.changeCaptureRankJobStatus(captureRankJob, loginName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/captureRank/changeCaptureRankJobStatus")
    @RequestMapping(value = "/changeCaptureRankJobStatuses", method = RequestMethod.POST)
    public ResultBean changeCaptureRankJobStatuses(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        String loginName = request.getSession().getAttribute("username").toString();
        try {
            List<Long> uuids = (List<Long>) map.get("uuids");
            Boolean status = (Boolean) map.get("status");
            captureRankJobService.changeCaptureRankJobStatuses(uuids, loginName, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequestMapping(value = "/resetCaptureRankJobs", method = RequestMethod.POST)
    public ResultBean resetCaptureRankJobs(@RequestBody List<Long> uuids) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            captureRankJobService.resetCaptureRankJobs(uuids);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
