package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.ckadmin.vo.MachineGroupQueueVO;
import com.keymanager.ckadmin.vo.KeywordCountVO;
import com.keymanager.util.TerminalTypeMapping;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName CustomerKeywordController
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/3 8:56
 * @Version 1.0
 */

@RestController
@RequestMapping(value = "/internal/customerKeyword")
public class CustomerKeywordController {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordController.class);

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "performanceService2")
    private PerformanceService performanceService;

    @RequestMapping(value = "/toMachineGroupAndSize", method = RequestMethod.GET)
    public ModelAndView toSearchWarnLists() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customerKeywords/MachineGroupQueue2");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/showMachineGroupAndSize")
    @RequestMapping(value = "/searchMachineGroupAndSize", method = RequestMethod.POST)
    public ResultBean searchMachineGroupAndSize(HttpServletRequest request){
        ResultBean resultBean = new ResultBean();
        long startMilleSeconds = System.currentTimeMillis();
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        try {
            List<MachineGroupQueueVO> machineGroupQueueVOS = customerKeywordService.getMachineGroupAndSize();
            performanceService.addPerformanceLog(terminalType + ":showMachineGroupAndSize", (System.currentTimeMillis() - startMilleSeconds), null);
            resultBean.setCode(0);
            resultBean.setMsg("success");
            resultBean.setData(machineGroupQueueVOS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @RequestMapping(value = "/changeCustomerKeywordStatus2", method = RequestMethod.POST)
    public ResultBean changeCustomerKeywordStatus(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String entryType = (String) request.getSession().getAttribute("entryType");
            String customerUuid = (String) requestMap.get("customerUuid");
            String status = (String) requestMap.get("status");
            customerKeywordService.changeCustomerKeywordStatus(terminalType, entryType, Long.parseLong(customerUuid), Integer.parseInt(status));
            return new ResultBean(200, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "error");
        }
    }

    /**
     * 根据用户id获取关键字统计信息
     */
    @GetMapping("/getCustomerKeywordsCount/{customerUuid}")
    public ResultBean getOperationCombines(@PathVariable Long customerUuid, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            KeywordCountVO keywordCountVO = customerKeywordService.getCustomerKeywordsCountByCustomerUuid(customerUuid, terminalType);
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(keywordCountVO);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/resetInvalidRefreshCount")
    @RequestMapping(value = "/resetInvalidRefresh", method = RequestMethod.POST)
    public ResultBean resetInvalidRefresh(@RequestBody RefreshStatisticsCriteria criteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            criteria.setTerminalType(terminalType);
            customerKeywordService.resetInvalidRefreshCount(criteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
