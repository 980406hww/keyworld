package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.entity.CaptureCurrentRankJob;
import com.keymanager.monitoring.entity.CustomerChargeType;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.service.CaptureCurrentRankJobService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@RestController
@RequestMapping(value = "/internal/crawlRanking")
public class CrawlRankingRsetController {

    private static Logger logger = LoggerFactory.getLogger(CrawlRankingRsetController.class);

    @Autowired
    private CaptureCurrentRankJobService captureCurrentRankJobService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @RequestMapping(value="/setCrawlRanking",method = RequestMethod.POST)
    public ResponseEntity<?> setCrawlRanking(@RequestBody CaptureCurrentRankJob captureCurrentRankJob){
        try{
            captureCurrentRankJobService.insert(captureCurrentRankJob);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/crawlRanking",method = RequestMethod.GET)
    public ModelAndView toSetCrawlRanking() {
        List<CaptureCurrentRankJob> captureCurrentRankJobs = captureCurrentRankJobService.selectList(null);
        return new ModelAndView("/crawlRanking/crawlRanking").addObject("captureCurrentRankJobs", captureCurrentRankJobs);
    }

    @RequestMapping(value="/crawlRankingExternalInterface",method = RequestMethod.GET)
    public ModelAndView doCrawlRanking() {
        List<CaptureCurrentRankJob> captureCurrentRankJobs = captureCurrentRankJobService.searchCaptureCurrentRankJobs();
        List<CustomerKeyword> customerKeywordlist= new ArrayList<CustomerKeyword>();
        for(CaptureCurrentRankJob captureCurrentRankJob:captureCurrentRankJobs)
        {
            String[] groupNames = captureCurrentRankJob.getGroupNames().split(",");
            String[] customerIds = captureCurrentRankJob.getCustomerIds().split(",");
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
    }
}
