package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.AlgorithmTestPlanSearchCriteria;
import com.keymanager.monitoring.entity.AlgorithmTestPlan;
import com.keymanager.monitoring.entity.AlgorithmTestTask;
import com.keymanager.monitoring.service.AlgorithmTestPlanService;
import com.keymanager.monitoring.service.AlgorithmTestTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by lhc on 2019/8/16.
 */
@RestController
@RequestMapping(value = "/internal/algorithmAutoTest")
public class AlgorithmAutoTestRestController {

    private static Logger logger = LoggerFactory.getLogger(AlgorithmAutoTestRestController.class);

    @Autowired
    private AlgorithmTestPlanService algorithmTestPlanService;

    @Autowired
    private AlgorithmTestTaskService algorithmTestTaskService;

    @RequiresPermissions("/internal/algorithmAutoTest/searchAlgorithmTestPlans")
    @RequestMapping(value = "/searchAlgorithmTestPlans", method = RequestMethod.GET)
    public ModelAndView toSetCrawlRanking(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "50") int displaysRecords, HttpServletRequest request) {
        return constructCaptureRankJobModelAndView(request, new AlgorithmTestPlanSearchCriteria(), currentPage + "", displaysRecords + "");
    }

    @RequiresPermissions("/internal/algorithmAutoTest/searchAlgorithmTestPlans")
    @RequestMapping(value = "/searchAlgorithmTestPlans", method = RequestMethod.POST)
    public ModelAndView searchCaptureRankingJobs(AlgorithmTestPlanSearchCriteria algorithmTestPlanSearchCriteria, HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructCaptureRankJobModelAndView(request, algorithmTestPlanSearchCriteria, currentPageNumber, pageSize);
    }

    private ModelAndView constructCaptureRankJobModelAndView(HttpServletRequest request, AlgorithmTestPlanSearchCriteria algorithmTestPlanSearchCriteria, String currentPage, String pageSize) {
        ModelAndView modelAndView = new ModelAndView("algorithmAutoTest/algorithmTestPlan");

        Page<AlgorithmTestPlan> page = algorithmTestPlanService.searchAlgorithmTestPlans(new Page<AlgorithmTestPlan>(Integer.parseInt(currentPage), Integer.parseInt(pageSize)), algorithmTestPlanSearchCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("algorithmTestPlanSearchCriteria", algorithmTestPlanSearchCriteria);
        return modelAndView;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "/saveAlgorithmTestPlan", method = RequestMethod.POST)
    public ResponseEntity<?> saveCaptureRankJob(@RequestBody AlgorithmTestPlan algorithmTestPlan, HttpServletRequest request) {
        try {
            algorithmTestPlanService.saveAlgorithmTestPlan(algorithmTestPlan);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/algorithmAutoTest/searchAlgorithmTestPlans")
    @RequestMapping(value = "/getAlgorithmTestPlan", method = RequestMethod.POST)
    public AlgorithmTestPlan getAlgorithmTestPlan(Long uuid) {
        return algorithmTestPlanService.selectById(uuid);
    }

    @RequiresPermissions("/internal/algorithmAutoTest/deleteAlgorithmTestPlan")
    @RequestMapping(value = "/deleteAlgorithmTestPlan", method = RequestMethod.POST)
    public ResponseEntity<?> deleteCaptureRankJob(Long uuid) {
        try {
            algorithmTestPlanService.deleteById(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/algorithmAutoTest/deleteAlgorithmTestPlan")
    @RequestMapping(value = "/deleteAlgorithmTestPlans", method = RequestMethod.POST)
    public ResponseEntity<?> deleteCaptureRankJobs(@RequestBody Map<String, Object> requestMap) {
        try {
            algorithmTestPlanService.deleteBatchIds((List<Long>) requestMap.get("uuids"));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/algorithmAutoTest/changeAlgorithmTestPlanStatus")
    @RequestMapping(value = "/changeAlgorithmTestPlanStatus", method = RequestMethod.POST)
    public ResponseEntity<?> changeCaptureRankJobStatus(@RequestBody Map<String, Object> requestMap) {
        try {
            Long uuid = Long.parseLong((String) requestMap.get("uuid"));
            Integer status = Integer.parseInt((String)requestMap.get("status"));
            algorithmTestPlanService.changeAlgorithmTestPlanStatus(uuid,status);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "/updateAlgorithmTestPlansStatus", method = RequestMethod.POST)
    public ResponseEntity<?> updateCaptureRankJobsStatus(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            List<Long> uuids = (List<Long>) requestMap.get("uuids");
            Integer status = Integer.valueOf(String.valueOf(requestMap.get("status")));
            algorithmTestPlanService.updateCaptureRankJobsStatus(uuids, status);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/algorithmAutoTest/showAlgorithmTestTask")
    @RequestMapping(value = "/showAlgorithmTestTask", method = RequestMethod.POST)
    public ModelAndView showAlgorithmTestTask(Long algorithmTestPlanUuid) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("algorithmAutoTest/algorithmTestTask");
        try {
            List<AlgorithmTestTask> algorithmTestTasks = algorithmTestTaskService.selectAlgorithmTestTasksByAlgorithmTestPlanUuid(algorithmTestPlanUuid);
            //algorithmTestPlanService.deleteBatchIds((List<Long>) requestMap.get("uuids"));
            mv.addObject("algorithmTestTasks",algorithmTestTasks);
            return mv;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return mv;
        }
    }
}

