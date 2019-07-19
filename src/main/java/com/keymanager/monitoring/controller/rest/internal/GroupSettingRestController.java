package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.entity.OperationCombine;
import com.keymanager.monitoring.service.*;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/internal/groupsetting")
public class GroupSettingRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(GroupSettingRestController.class);

    @Autowired
    private GroupSettingService groupSettingService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private OperationTypeService operationTypeService;

    @Autowired
    private OperationCombineService operationCombineService;

    @RequiresPermissions("/internal/groupsetting/searchGroupSettings")
    @RequestMapping(value = "/searchGroupSettings", method = RequestMethod.GET)
    public ModelAndView searchGroupSettingsGet(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "25") int pageSize, HttpServletRequest request) {
        return constructGroupSettingModelAndView(request, new GroupSettingCriteria(), currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/groupsetting/searchGroupSettings")
    @RequestMapping(value = "/searchGroupSettings", method = RequestMethod.POST)
    public ModelAndView searchClientStatusesPost(HttpServletRequest request, GroupSettingCriteria groupSettingCriteria) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "10";
            }
            return constructGroupSettingModelAndView(request, groupSettingCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/groupsetting/list");
        }
    }

    private ModelAndView constructGroupSettingModelAndView(HttpServletRequest request, GroupSettingCriteria groupSettingCriteria, int currentPageNumber, int pageSize) {
        long startMilleSeconds = System.currentTimeMillis();
        ModelAndView modelAndView = new ModelAndView("/groupsetting/list");
        if (null == groupSettingCriteria.getTerminalType()) {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            groupSettingCriteria.setTerminalType(terminalType);
        }
        Page<OperationCombine> page = groupSettingService.searchGroupSettings(new Page<OperationCombine>(currentPageNumber, pageSize), groupSettingCriteria);
        List operationTypeValues =  operationTypeService.getOperationTypeValuesByRole(groupSettingCriteria.getTerminalType());
        List<String> operationCombineNames = operationCombineService.getOperationCombineNames(groupSettingCriteria.getTerminalType());
        HttpSession session = request.getSession();
        String entryType = (String) session.getAttribute("entryType");
        String maxInvalidCount = configService.getConfig(Constants.CONFIG_TYPE_MAX_INVALID_COUNT, entryType).getValue();
        modelAndView.addObject("groupSettingCriteria", groupSettingCriteria);
        modelAndView.addObject("operationTypeValues", operationTypeValues);
        modelAndView.addObject("operationCombineNames", operationCombineNames);
        modelAndView.addObject("page", page);
        modelAndView.addObject("terminalType", groupSettingCriteria.getTerminalType());
        modelAndView.addObject("maxInvalidCount", maxInvalidCount);
        performanceService.addPerformanceLog(groupSettingCriteria.getTerminalType() + ":searchGroupSettings", System.currentTimeMillis() - startMilleSeconds, null);
        return modelAndView;
    }

    @RequiresPermissions("/internal/groupsetting/delGroupSetting")
    @PostMapping("/delGroupSetting/{uuid}")
    public ResponseEntity<?> delGroupSetting(@PathVariable("uuid") long uuid) {
        try {
            groupSettingService.deleteGroupSetting(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/groupsetting/findGroupSetting")
    @PostMapping("/findGroupSetting/{uuid}")
    public ResponseEntity<?> findGroupSetting(@PathVariable("uuid") long uuid) {
        try {
            GroupSetting groupSetting = groupSettingService.findGroupSetting(uuid);
            return new ResponseEntity<Object>(groupSetting, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null , HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/groupsetting/saveGroupSetting")
    @PostMapping("/saveGroupSetting")
    public ResponseEntity<?> saveGroupSetting(@RequestBody GroupSetting groupSetting) {
        try {
            groupSettingService.saveGroupSetting(groupSetting, true);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/groupsetting/updateGroupSetting")
    @PostMapping("/updateGroupSetting")
    public ResponseEntity<?> updateGroupSetting(@RequestBody UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        try {
            groupSettingService.updateGroupSetting(updateGroupSettingCriteria);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/groupsetting/findGroupSetting")
    @PostMapping("/getGroupSettingCount/{operationCombineUuid}")
    public ResponseEntity<?> getGroupSettingCount(@PathVariable("operationCombineUuid") long operationCombineUuid) {
        try {
            int groupSettingCount = groupSettingService.getGroupSettingUuids(operationCombineUuid).size();
            return new ResponseEntity<Object>(groupSettingCount, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
