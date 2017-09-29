package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CaptureCurrentRankJobCriteria;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.CaptureRankExectionStatus;
import com.keymanager.monitoring.service.*;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@RestController
@RequestMapping(value = "/internal/crawlRanking")
public class CrawlRankingRsetController {

    private static Logger logger = LoggerFactory.getLogger(CrawlRankingRsetController.class);

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private CaptureCurrentRankJobService captureCurrentRankJobService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value="/saveCaptureCurrentRankJob",method = RequestMethod.POST)
    public ResponseEntity<?> saveCrawlRanking(@RequestBody CaptureCurrentRankJob captureCurrentRankJob,HttpServletRequest request){
        try{
            if(captureCurrentRankJob.getUuid()==null) {
                captureCurrentRankJob.setExectionStatus(CaptureRankExectionStatus.New.name());
                captureCurrentRankJob.setOperationType(request.getSession().getAttribute("terminalType").toString());
                captureCurrentRankJob.setCreateBy(request.getSession().getAttribute("username").toString());
                captureCurrentRankJobService.insert(captureCurrentRankJob);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
            else
            {
                captureCurrentRankJob.setOperationType(request.getSession().getAttribute("terminalType").toString());
                captureCurrentRankJob.setUpdateBy(request.getSession().getAttribute("username").toString());
                captureCurrentRankJob.setUpdateTime(new Date());
                captureCurrentRankJobService.updateById(captureCurrentRankJob);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/crawlRanking",method = RequestMethod.POST)
    public ModelAndView searchCrawlRanking(HttpServletRequest request, CaptureCurrentRankJobCriteria captureCurrentRankJobCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == currentPageNumber) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructCaptureCurrentRankJobModelAndView(request, captureCurrentRankJobCriteria, currentPageNumber, pageSize);
    }
    @RequestMapping(value="/crawlRanking",method = RequestMethod.GET)
    public ModelAndView toSetCrawlRanking(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "50") int displaysRecords,
                                          HttpServletRequest request) {
        return constructCaptureCurrentRankJobModelAndView(request, new CaptureCurrentRankJobCriteria(), currentPage+"", displaysRecords+"");
    }

    private ModelAndView constructCaptureCurrentRankJobModelAndView(HttpServletRequest request, CaptureCurrentRankJobCriteria captureCurrentRankJobCriteria, String currentPage, String pageSize)
    {
        ModelAndView modelAndView = new ModelAndView("/crawlRanking/crawlRanking");
        Page<CaptureCurrentRankJob> page = captureCurrentRankJobService.searchCaptureCurrentRankJob(new Page<CaptureCurrentRankJob>(Integer.parseInt(currentPage), Integer.parseInt(pageSize)),captureCurrentRankJobCriteria);
        modelAndView.addObject("page",page);
        modelAndView.addObject("captureCurrentRankJobCriteria",captureCurrentRankJobCriteria);
        return modelAndView;
    }

    @RequestMapping(value="/getCaptureCurrentRankJob",method = RequestMethod.POST)
    public CaptureCurrentRankJob getCaptureCurrentRankJob(Long uuid) {
        return captureCurrentRankJobService.selectById(uuid);
    }

    @RequestMapping(value="/searchCustomer",method = RequestMethod.POST)
    public List<Customer> searchCustomer(String contactPerson) {
        if(contactPerson!=null)
        {
            return customerService.selectList(new EntityWrapper<Customer>().setSqlSelect("fUuid as uuid,fContactPerson as contactPerson,fQQ as qq,fTelphone as telphone,fEmail as email").like("fContactPerson",contactPerson));
        }
        return customerService.selectList(new EntityWrapper<Customer>().setSqlSelect("fUuid as uuid,fContactPerson as contactPerson,fQQ as qq,fTelphone as telphone,fEmail as email"));
    }
    @RequestMapping(value="/searchGroups",method = RequestMethod.POST)
    public List<CustomerKeyword> searchGroups(Long customerID) {
        if(customerID!=null)
        {
            //return customerKeywordService.selectList(new EntityWrapper<CustomerKeyword>().setSqlSelect("DISTINCT  fOptimizeGroupName as optimizeGroupName").eq("fCustomerUuid", customerID));
            return customerKeywordService.selectList(new EntityWrapper<CustomerKeyword>().groupBy("fOptimizeGroupName").eq("fCustomerUuid",customerID));
        }
        //List<CustomerKeyword> customerKeywords = customerKeywordService.selectList(new EntityWrapper<CustomerKeyword>().groupBy("fOptimizeGroupName"));
        //List<CustomerKeyword> customerKeywords = customerKeywordService.selectList(new EntityWrapper<CustomerKeyword>().setSqlSelect("DISTINCT  fOptimizeGroupName as optimizeGroupName"));
        return customerKeywordService.selectList(new EntityWrapper<CustomerKeyword>().groupBy("fOptimizeGroupName"));
    }


    @RequestMapping(value="/deleteCaptureCurrentRankJob",method = RequestMethod.POST)
    public ResponseEntity<?> deleteCaptureCurrentRankJob(Long uuid) {
        try {
            captureCurrentRankJobService.deleteById(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value="/deleteCaptureCurrentRankJobs",method = RequestMethod.POST)
    public ResponseEntity<?> deleteCaptureCurrentRankJobs(Long[] uuids) {
        try {
            captureCurrentRankJobService.deleteBatchIds(new ArrayList<Long>(Arrays.asList(uuids)));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
    /*@RequestMapping(value="/crawlRankingExternalInterface",method = RequestMethod.GET)
    public ModelAndView doCrawlRanking() {
        List<CaptureCurrentRankJob> captureCurrentRankJobs = captureCurrentRankJobService.searchCaptureCurrentRankJobs();
        List<CustomerKeyword> customerKeywordlist= new ArrayList<CustomerKeyword>();
        for(CaptureCurrentRankJob captureCurrentRankJob:captureCurrentRankJobs)
        {
            String[] groupNames = captureCurrentRankJob.getGroupNames().split(",");
            String[] customerIds = captureCurrentRankJob.getCustomerID().split(",");
            for(String groupName:groupNames)
            {
                for(String customerId:customerIds)
                {
                    CustomerKeyword customerKeyword = new CustomerKeyword();
                    customerKeyword.setCustomerUuid(Long.parseLong(customerId));
                    customerKeyword.setOptimizeGroupName(groupName);
                    customerKeywordlist.addAll(customerKeywordService.searchTitleAndUrl(customerKeyword));
                }
            }
            captureCurrentRankJob.setExectionStatus("processing");
            captureCurrentRankJobService.updateBatchById(captureCurrentRankJobs);
        }

       return new ModelAndView("/crawlRanking/crawlRanking").addObject("captureCurrentRankJobs", captureCurrentRankJobs);
    }*/

   /* @RequestMapping(value="/crawlRankingExternalInterface",method = RequestMethod.GET)
    public ModelAndView doCrawlRanking() {
        List<CaptureCurrentRankJob> captureCurrentRankJobs = captureCurrentRankJobService.searchCaptureCurrentRankJobs();
        List<CustomerKeyword> customerKeywordlist= new ArrayList<CustomerKeyword>();
        for(CaptureCurrentRankJob captureCurrentRankJob:captureCurrentRankJobs)
        {
            String[] groupNames = captureCurrentRankJob.getGroupNames().split(",");
            String customerID=captureCurrentRankJob.getCustomerID();
            customerKeywordlist.addAll(customerKeywordService.searchTitleAndUrl(groupNames,customerID));
            captureCurrentRankJob.setExectionStatus("processing");
            captureCurrentRankJobService.updateBatchById(captureCurrentRankJobs);
        }

        return new ModelAndView("/crawlRanking/crawlRanking").addObject("captureCurrentRankJobs", captureCurrentRankJobs);
    }*/
}
