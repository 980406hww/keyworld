package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.entity.CaptureCurrentRankJob;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.service.CaptureCurrentRankJobService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/27.
 */
@RequestMapping(value = "/external/crawlRanking")
public class ExternalCrawlRankingRsetController {
    @Autowired
    private CaptureCurrentRankJobService captureCurrentRankJobService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @RequestMapping(value="/crawlRankingExternalInterface",method = RequestMethod.GET)
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
    }
}
