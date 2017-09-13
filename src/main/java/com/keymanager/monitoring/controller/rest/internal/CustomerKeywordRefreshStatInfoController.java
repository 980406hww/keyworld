package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.service.CustomerKeywordRefreshStatInfoService;
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
    public ModelAndView searchRefreshStatInfos(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructNegativeListModelAndView(request, new CustomerKeywordRefreshStatInfoCriteria(), currentPageNumber, pageSize);
    }

    @RequestMapping(value = "/searchRefreshStatInfos", method = RequestMethod.POST)
    public ModelAndView searchRefreshStatInfosPost(HttpServletRequest request, CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "50";
            }
            return constructNegativeListModelAndView(request, customerKeywordRefreshStatInfoCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/refresh/list");
        }
    }

    private ModelAndView constructNegativeListModelAndView(HttpServletRequest request, CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/refresh/list");
        String entryType = (String) request.getSession().getAttribute("entry");
        customerKeywordRefreshStatInfoCriteria.setType(entryType);
        Page<CustomerKeywordRefreshStatInfoVO> page = customerKeywordRefreshStatInfoService.generateCustomerKeywordStatInfo(new Page<CustomerKeywordRefreshStatInfoVO>(currentPageNumber,
                pageSize), customerKeywordRefreshStatInfoCriteria);
        modelAndView.addObject("customerKeywordRefreshStatInfoCriteria", customerKeywordRefreshStatInfoCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }
}
