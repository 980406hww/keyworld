package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.NegativeRankCriteria;
import com.keymanager.monitoring.entity.NegativeRank;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.NegativeRankService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * Created by liaojijun on 2018/5/31.
 */
@RestController
@RequestMapping(value = "/internal/negativeRank")
public class NegativeRankController {

    private static Logger logger = LoggerFactory.getLogger(NegativeListRestController.class);

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

    @RequiresPermissions("/internal/negativeRank/updateNegativeRankKeyword")
    @RequestMapping(value = "updateNegativeRankKeyword",method = RequestMethod.POST)
    public ResponseEntity<?> updateNegativeRankKeyword(@RequestBody NegativeRank negativeRank){
       try {
           negativeRankService.updateNegativeRankKeyword(negativeRank);
           return new ResponseEntity<Object>(true, HttpStatus.OK);
       }catch (Exception e){
           logger.error(e.getMessage());
           return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
       }
    }
}
