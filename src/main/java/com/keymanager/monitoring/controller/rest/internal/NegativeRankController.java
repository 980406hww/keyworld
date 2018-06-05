package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.NegativeRankCriteria;
import com.keymanager.monitoring.dao.ConfigDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.NegativeRank;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.NegativeRankService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liaojijun on 2018/5/31.
 */
@RestController
@RequestMapping(value = "/internal/negativeRank")
public class NegativeRankController {

    @Autowired
    NegativeRankService  negativeRankService;

    @Autowired
    ConfigService configService;

    @RequiresPermissions("/internal/negativeRank/searchNegativeRanks")
    @RequestMapping(value ="/searchNegativeRanks",method= RequestMethod.GET)
    public ModelAndView searchNegativeRanks(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request){
        return constructRegativeRankoModelAndView(new NegativeRankCriteria(),currentPageNumber,pageSize);
    }

    @RequiresPermissions("/internal/negativeRank/searchNegativeRanks")
    @RequestMapping(value = "/searchNegativeRanks",method = RequestMethod.POST)
    public ModelAndView searchNegativeRankPost(HttpServletRequest request,NegativeRankCriteria negativeRankCriteria){
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructRegativeRankoModelAndView(negativeRankCriteria,Integer.parseInt(currentPageNumber),Integer.parseInt(pageSize));
    }

    private ModelAndView constructRegativeRankoModelAndView( NegativeRankCriteria negativeRankCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("negativeRank/negativeRank");
        Page<NegativeRank> page=negativeRankService.searchNegativeRanks(new Page<NegativeRank>(currentPageNumber,pageSize),negativeRankCriteria);
        Set<String> keywords=configService.getNegativeKeyword();
        modelAndView.addObject("keywords", keywords);
        modelAndView.addObject("negativeRankCriteria", negativeRankCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

}
