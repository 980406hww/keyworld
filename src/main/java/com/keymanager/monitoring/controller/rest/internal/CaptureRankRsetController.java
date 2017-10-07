package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.service.*;
import com.keymanager.util.TerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@RestController
@RequestMapping(value = "/internal/captureRank")
public class CaptureRankRsetController {

    private static Logger logger = LoggerFactory.getLogger(CaptureRankRsetController.class);

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private CaptureRankJobService captureRankJobService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/saveCaptureRankJob", method = RequestMethod.POST)
    public ResponseEntity<?> saveCaptureRankJob(@RequestBody CaptureRankJob captureRankJob, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String loginName = request.getSession().getAttribute("username").toString();
            captureRankJobService.saveCaptureRankJob(captureRankJob, terminalType, loginName);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/searchCaptureRankJobs", method = RequestMethod.POST)
    public ModelAndView searchCaptureRankingJobs(CaptureRankJobSearchCriteria captureRankJobSearchCriteria, HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == currentPageNumber) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructCaptureRankJobModelAndView(request, captureRankJobSearchCriteria, currentPageNumber, pageSize);
    }

    @RequestMapping(value = "/searchCaptureRankJobs", method = RequestMethod.GET)
    public ModelAndView toSetCrawlRanking(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "50") int displaysRecords,
                                          HttpServletRequest request) {
        return constructCaptureRankJobModelAndView(request, new CaptureRankJobSearchCriteria(), currentPage + "", displaysRecords + "");
    }

    private ModelAndView constructCaptureRankJobModelAndView(HttpServletRequest request, CaptureRankJobSearchCriteria captureRankJobSearchCriteria, String currentPage, String pageSize) {
        ModelAndView modelAndView = new ModelAndView("captureRank/captureRank");
        Page<CaptureRankJob> page = captureRankJobService.searchCaptureRankJob(new Page<CaptureRankJob>(Integer.parseInt(currentPage), Integer.parseInt(pageSize)), captureRankJobSearchCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("captureRankJobSearchCriteria", captureRankJobSearchCriteria);
        return modelAndView;
    }

    @RequestMapping(value = "/getCaptureRankJob", method = RequestMethod.POST)
    public CaptureRankJob getCaptureRankJob(Long uuid) {
        return captureRankJobService.selectById(uuid);
    }

    @RequestMapping(value = "/searchCustomer", method = RequestMethod.POST)
    public List<Customer> searchCustomer(String contactPerson) {return customerService.searchCustomersWithKeyword(contactPerson);
    }

    @RequestMapping(value = "/searchGroups", method = RequestMethod.POST)
    public List<CustomerKeyword> searchGroups(Long customerID) {
       return customerKeywordService.searchGroups(customerID);//查看到这里
    }


    @RequestMapping(value = "/deleteCaptureRankJob", method = RequestMethod.POST)
    public ResponseEntity<?> deleteCaptureRankJob(Long uuid) {
        try {
            captureRankJobService.deleteById(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deleteCaptureRankJobs", method = RequestMethod.POST)
    public ResponseEntity<?> deleteCaptureRankJobs(Long[] uuids) {
        try {
            captureRankJobService.deleteBatchIds(new ArrayList<Long>(Arrays.asList(uuids)));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
