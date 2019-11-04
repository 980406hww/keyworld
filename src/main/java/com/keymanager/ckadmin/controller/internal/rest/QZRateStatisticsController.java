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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/internal/qzRateStatistics")
public class QZRateStatisticsController {

    @Resource(name = "qzRateStatisticsService2")
    private QZRateStatisticsService qzRateStatisticsService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @PostMapping("/getQZRateKeywordCountList")
    public ResultBean getQZRateKeywordCountList(@RequestBody QZRateKewordCountCriteria qzRateKewordCountCriteria, HttpSession session){
        ResultBean resultBean = new ResultBean(0,"success");
        try{
            qzRateKewordCountCriteria.setUserID((String) session.getAttribute("username"));
            Page<QZRateKeywordCountVO> page = new Page<>(qzRateKewordCountCriteria.getPage(),qzRateKewordCountCriteria.getLimit());
            page = customerKeywordService.getQZRateKewordCountList(page,qzRateKewordCountCriteria);
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
            return resultBean;
        }catch (Exception e){
            e.printStackTrace();
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
    }

    @GetMapping("/toQZRateHistory/{terminalType}/{qzUuid}")
    public ModelAndView toQZRateHistory(@PathVariable(name = "terminalType") String terminalType, @PathVariable(name = "qzUuid") Long qzUuid){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("statistics/qzRateHistoryDetail");
        mv.addObject("qzRateTerminalType", terminalType);
        mv.addObject("qzUuid", qzUuid);
        return mv;
    }

    @PostMapping("/getQZRateHistory")
    public ResultBean getQZRateHistory(@RequestBody Map requestMap){
        ResultBean resultBean = new ResultBean(200,"success");
        try{
            String qzUuid = (String) requestMap.get("qzUuid");
            String terminalType = (String) requestMap.get("terminalType");
            Map map = qzRateStatisticsService.getQzRateHsitory(qzUuid,terminalType);
            resultBean.setData(map);
            return resultBean;
        }catch (Exception e){
            e.printStackTrace();
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
    }

    /**
     * 根据操作词曲线值生成涨幅表数据
     */
    @RequestMapping("/excuteTask")
    public void excuteTask(){
        qzRateStatisticsService.generateQZRateStatistics();
    }
}
