package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.AlgorithmTestPlanSearchCriteria;
import com.keymanager.monitoring.entity.AlgorithmTestPlan;
import com.keymanager.monitoring.entity.AlgorithmTestTask;
import com.keymanager.monitoring.service.AlgorithmTestPlanService;
import com.keymanager.monitoring.service.AlgorithmTestTaskService;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.util.TerminalTypeMapping;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @Autowired
    private ConfigService configService;

    @RequiresPermissions("/internal/algorithmAutoTest/searchAlgorithmTestPlans")
    @RequestMapping(value = "/searchAlgorithmTestPlans", method = RequestMethod.GET)
    public ModelAndView toSetCrawlRanking(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "50") int displaysRecords, HttpServletRequest request) {
        return constructAlgorithmTestPlanModelAndView(request, new AlgorithmTestPlanSearchCriteria(), currentPage + "", displaysRecords + "");
    }

    @RequiresPermissions("/internal/algorithmAutoTest/searchAlgorithmTestPlans")
    @RequestMapping(value = "/searchAlgorithmTestPlans", method = RequestMethod.POST)
    public ModelAndView searchAlgorithmTestPlans(AlgorithmTestPlanSearchCriteria algorithmTestPlanSearchCriteria, HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructAlgorithmTestPlanModelAndView(request, algorithmTestPlanSearchCriteria, currentPageNumber, pageSize);
    }

    private ModelAndView constructAlgorithmTestPlanModelAndView(HttpServletRequest request, AlgorithmTestPlanSearchCriteria algorithmTestPlanSearchCriteria, String currentPage, String pageSize) {
        ModelAndView modelAndView = new ModelAndView("algorithmAutoTest/algorithmTestPlan");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        Page<AlgorithmTestPlan> page = algorithmTestPlanService.searchAlgorithmTestPlans(new Page<AlgorithmTestPlan>(Integer.parseInt(currentPage), Integer.parseInt(pageSize)), algorithmTestPlanSearchCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("algorithmTestPlanSearchCriteria", algorithmTestPlanSearchCriteria);
        modelAndView.addObject("searchEngineMap", configService.getSearchEngineMap(terminalType));
        modelAndView.addObject("terminalType", terminalType);
        return modelAndView;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "/saveAlgorithmTestPlan", method = RequestMethod.POST)
    public ResponseEntity<?> saveAlgorithmTestPlan(@RequestBody AlgorithmTestPlan algorithmTestPlan, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String loginName = (String) session.getAttribute("username");
            algorithmTestPlan.setCreateBy(loginName);
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
    public ResponseEntity<?> deleteAlgorithmTestPlan(Long uuid) {
        try {
            algorithmTestPlanService.deletePlanAndTaskByPlanId(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/algorithmAutoTest/deleteAlgorithmTestPlan")
    @RequestMapping(value = "/deleteAlgorithmTestPlans", method = RequestMethod.POST)
    public ResponseEntity<?> deleteAlgorithmTestPlans(@RequestBody Map<String, Object> requestMap) {
        try {
            algorithmTestPlanService.deletePlanAndTaskByPlanIds((List<Long>) requestMap.get("uuids"));
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/algorithmAutoTest/changeAlgorithmTestPlanStatus")
    @RequestMapping(value = "/changeAlgorithmTestPlanStatus", method = RequestMethod.POST)
    public ResponseEntity<?> changeAlgorithmTestPlanStatus(@RequestBody Map<String, Object> requestMap) {
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
    public ResponseEntity<?> updateAlgorithmTestPlansStatus(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            List<Long> uuids = (List<Long>) requestMap.get("uuids");
            Integer status = Integer.valueOf(String.valueOf(requestMap.get("status")));
            algorithmTestPlanService.updateAlgorithmTestPlansStatus(uuids, status);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/algorithmAutoTest/showAlgorithmTestTask")
    @RequestMapping(value = "/showAlgorithmTestTask", method = RequestMethod.POST)
    public ModelAndView showAlgorithmTestTask(Long algorithmTestPlanUuid, @RequestParam(defaultValue = "1") String currentPageNumber, @RequestParam(defaultValue = "50") String pageSize) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("algorithmAutoTest/algorithmTestTask");
        try {
            Page<AlgorithmTestTask> algorithmTestTaskPage = algorithmTestTaskService.selectAlgorithmTestTasksByAlgorithmTestPlanUuid(new Page<AlgorithmTestTask>(Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize)), algorithmTestPlanUuid);
            mv.addObject("page",algorithmTestTaskPage);
            mv.addObject("algorithmTestPlanUuid",algorithmTestPlanUuid);
            return mv;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return mv;
        }
    }
}

