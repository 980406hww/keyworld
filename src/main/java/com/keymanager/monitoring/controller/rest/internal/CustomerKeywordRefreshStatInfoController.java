package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.service.CustomerKeywordRefreshStatInfoService;
import com.keymanager.util.PortTerminalTypeMapping;
import com.keymanager.value.CustomerKeywordRefreshStatInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/9/12.
 */
@RestController
@RequestMapping(value = "/internal/refreshstatinfo")
public class CustomerKeywordRefreshStatInfoController {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordRefreshStatInfoController.class);

    @Autowired
    private CustomerKeywordRefreshStatInfoService customerKeywordRefreshStatInfoService;

    @RequestMapping(value = "/searchRefreshStatInfos", method = RequestMethod.GET)
    public ModelAndView searchRefreshStatInfos(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/refresh/list");
        return modelAndView;
    }

    @RequestMapping(value = "/searchRefreshStatInfos", method = RequestMethod.POST)
    public ModelAndView searchRefreshStatInfosPost(HttpServletRequest request, CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        try {
            return constructNegativeListModelAndView(request, customerKeywordRefreshStatInfoCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/refresh/list");
        }
    }

    private ModelAndView constructNegativeListModelAndView(HttpServletRequest request, CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        ModelAndView modelAndView = new ModelAndView("/refresh/list");
        String entryType = (String) request.getSession().getAttribute("entry");
        customerKeywordRefreshStatInfoCriteria.setType(entryType);
        String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
        customerKeywordRefreshStatInfoCriteria.setTerminalType(terminalType);
        List<CustomerKeywordRefreshStatInfoVO> page = customerKeywordRefreshStatInfoService.generateCustomerKeywordStatInfo(customerKeywordRefreshStatInfoCriteria);
        modelAndView.addObject("customerKeywordRefreshStatInfoCriteria", customerKeywordRefreshStatInfoCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }
}
