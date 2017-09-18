package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.service.*;
import com.keymanager.util.TerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/customerKeywordPositionIndexLog")
public class CustomerKeywordPositionIndexLogController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(CustomerKeywordPositionIndexLogController.class);

	@Autowired
	private CustomerKeywordPositionIndexLogService customerKeywordPositionIndexLogService;

	//查询历史缴费记录
	@RequestMapping(value = "/historyPositionAndIndex/{customerKeywordUuid}/{dayCount}" , method = RequestMethod.GET)
	public ModelAndView historyPositionAndIndex(@PathVariable("customerKeywordUuid")Long customerKeywordUuid, @PathVariable("dayCount")String dayCount, HttpServletRequest request){
		String terminalType = TerminalTypeMapping.getTerminalType(request);
		Map<String,Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("terminalType",terminalType);
		conditionMap.put("customerKeywordUuid",customerKeywordUuid);
		conditionMap.put("dayCount", dayCount);
		Page<CustomerKeywordPositionIndexLog> page = customerKeywordPositionIndexLogService.searchCustomerKeywordPositionIndexLogs(new Page<CustomerKeywordPositionIndexLog>(1,10000),conditionMap);
		ModelAndView modelAndView = new ModelAndView("/customerkeyword/historyPositionAndIndex");
		modelAndView.addObject("page", page);
		return modelAndView;
	}
}
