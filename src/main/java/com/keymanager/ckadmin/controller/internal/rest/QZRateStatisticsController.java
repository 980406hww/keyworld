package com.keymanager.ckadmin.controller.internal.rest;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.QZRateKewordCountCriteria;
import com.keymanager.ckadmin.criteria.QZRateStatisticsCountCriteria;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.QZRateStatisticsService;
import com.keymanager.ckadmin.vo.QZRateKeywordCountVO;
import com.keymanager.ckadmin.vo.QZRateStatisticsCountVO;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lhc0821
 */
@RestController
@RequestMapping("/internal/qzRateStatistics")
public class QZRateStatisticsController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(QZRateStatisticsController.class);

    @Resource(name = "qzRateStatisticsService2")
    private QZRateStatisticsService qzRateStatisticsService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @RequiresPermissions("/internal/qzRateStatistics/toQZRateStatisticsDetail")
    @RequestMapping("/generateQZRateStatisticsDataMap")
    public ResultBean generateQZRateStatisticsDataMap(@RequestBody QZRateStatisticsCountCriteria qzRateStatisticsCountCriteria, HttpSession session) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")) {
                qzRateStatisticsCountCriteria.setUserID((String) session.getAttribute("username"));
            }
            List<QZRateStatisticsCountVO> qzRateStatisticsCountVos = qzRateStatisticsService.getQZRateStatisticCount(qzRateStatisticsCountCriteria);
            resultBean.setData(qzRateStatisticsService.generateEchartsData(qzRateStatisticsCountVos));
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/qzRateStatistics/toQZRateStatisticsDetail")
    @GetMapping(value = {"/toQZRateStatisticsDetail/{rateRange}/{terminalType}/{searchEngine}", "/toQZRateStatisticsDetail/{rateRange}/{terminalType}",
        "/toQZRateStatisticsDetail/{rateRange}/{searchEngine}", "/toQZRateStatisticsDetail/{rateRange}"})
    public ModelAndView toQZRateStatisticsDetail(@PathVariable(name = "rateRange") String rateRange, @PathVariable(name = "terminalType", required = false) String terminalType,
        @PathVariable(name = "searchEngine", required = false) String searchEngine) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("statistics/qzRateStatisticsDetail");
        try {
            Map<String, String> map = new HashMap<>(3);
            map.put("qzRateRange", rateRange);
            if (null == searchEngine) {
                map.put("searchEngine", "");
            } else {
                searchEngine = URLDecoder.decode(searchEngine, "UTF-8");
                map.put("searchEngine", searchEngine);
            }
            if (null == terminalType) {
                map.put("terminalType", "");
            } else {
                map.put("terminalType", terminalType);
            }
            mv.addObject("formData", JSON.toJSONString(map));
            return mv;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return mv;
    }

    @RequiresPermissions("/internal/qzRateStatistics/toQZRateStatisticsDetail")
    @PostMapping("/getQZRateKeywordCountList")
    public ResultBean getQZRateKeywordCountList(@RequestBody QZRateKewordCountCriteria qzRateKewordCountCriteria, HttpSession session){
        ResultBean resultBean = new ResultBean(0,"success");
        try {
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")) {
                qzRateKewordCountCriteria.setUserID((String) session.getAttribute("username"));
            }
            Page<QZRateKeywordCountVO> page = new Page<>(qzRateKewordCountCriteria.getPage(), qzRateKewordCountCriteria.getLimit());
            page = customerKeywordService.getQZRateKeywordCountList(page, qzRateKewordCountCriteria);
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    /**
     * 获取站点近3个月的涨幅趋势数据
     */
    @RequiresPermissions("/internal/qzRateStatistics/toQZRateStatisticsDetail")
    @PostMapping("/getQZRateHistory")
    public ResultBean getQZRateHistory(@RequestBody Map requestMap){
        ResultBean resultBean = new ResultBean(200,"success");
        try{
            String qzUuid = (String) requestMap.get("qzUuid");
            String terminalType = (String) requestMap.get("terminalType");
            resultBean.setData(qzRateStatisticsService.getQzRateHistory(qzUuid, terminalType));
            return resultBean;
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }
}
