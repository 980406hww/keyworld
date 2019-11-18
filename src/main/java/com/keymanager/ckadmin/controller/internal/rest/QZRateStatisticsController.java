package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.QZRateKewordCountCriteria;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.QZRateStatisticsService;
import com.keymanager.ckadmin.vo.QZRateKeywordCountVO;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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
public class QZRateStatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(QZRateStatisticsController.class);

    @Resource(name = "qzRateStatisticsService2")
    private QZRateStatisticsService qzRateStatisticsService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @PostMapping("/getQZRateKeywordCountList")
    public ResultBean getQZRateKeywordCountList(@RequestBody QZRateKewordCountCriteria qzRateKewordCountCriteria, HttpSession session){
        ResultBean resultBean = new ResultBean(0,"success");
        try {
            qzRateKewordCountCriteria.setUserID((String) session.getAttribute("username"));
            Page<QZRateKeywordCountVO> page = new Page<>(qzRateKewordCountCriteria.getPage(), qzRateKewordCountCriteria.getLimit());
            page = customerKeywordService.getQZRateKeywordCountList(page, qzRateKewordCountCriteria);
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
            return resultBean;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @GetMapping("/toQZRateHistory/{terminalType}/{qzUuid}")
    public ModelAndView toQZRateHistory(@PathVariable(name = "terminalType") String terminalType, @PathVariable(name = "qzUuid") Long qzUuid) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("statistics/qzRateHistoryDetail");
        mv.addObject("qzRateTerminalType", terminalType);
        mv.addObject("qzUuid", qzUuid);
        return mv;
    }

    /**
     * 获取站点近3个月的涨幅趋势数据
     */
    @PostMapping("/getQZRateHistory")
    public ResultBean getQZRateHistory(@RequestBody Map requestMap){
        ResultBean resultBean = new ResultBean(200,"success");
        try{
            String qzUuid = (String) requestMap.get("qzUuid");
            String terminalType = (String) requestMap.get("terminalType");
            resultBean.setData(qzRateStatisticsService.getQzRateHistory(qzUuid, terminalType));
            return resultBean;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }
}
