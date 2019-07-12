package com.keymanager.monitoring.controller.rest.internal;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.IndustryCriteria;
import com.keymanager.monitoring.entity.IndustryInfo;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.service.IndustryInfoService;
import com.keymanager.monitoring.service.UserRoleService;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行业表 前端控制器
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
@RestController
@RequestMapping("/internal/industry")
public class IndustryInfoRestController {

    private static final Logger logger = LoggerFactory.getLogger(IndustryInfoRestController.class);

    @Autowired
    private IndustryInfoService industryInfoService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ConfigService configService;

    @RequiresPermissions("/internal/industry/searchIndustries")
    @GetMapping("/searchIndustries")
    public ModelAndView searchIndustries(@RequestParam(defaultValue = "1") int currentPage,
                                         @RequestParam(defaultValue = "50") int displayRecords,
                                         HttpServletRequest request) {
        return constructIndustryModelAndView(request, new IndustryCriteria(), currentPage + "", displayRecords + "");
    }

    @RequiresPermissions("/internal/industry/searchIndustries")
    @PostMapping("/searchIndustries")
    public ModelAndView searchIndustriesPost(HttpServletRequest request, IndustryCriteria industryCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructIndustryModelAndView(request, industryCriteria, currentPageNumber, pageSize);
    }

    private ModelAndView constructIndustryModelAndView(HttpServletRequest request, IndustryCriteria industryCriteria,
                                                       String currentPage, String pageSize) {
        ModelAndView modelAndView = new ModelAndView("/industry/industrylist");
        HttpSession session = request.getSession();
        String loginName = (String) session.getAttribute("username");
        UserInfo user = userInfoService.getUserInfo(loginName);
        List<UserInfo> activeUsers = userInfoService.findActiveUsers();
        if (null == industryCriteria.getTerminalType()) {
            industryCriteria.setTerminalType(TerminalTypeMapping.getTerminalType(request));
        }
        boolean isDepartmentManager = userRoleService.isDepartmentManager(user.getUuid());
        if (!isDepartmentManager) {
            industryCriteria.setLoginName(loginName);
        }
        Page<IndustryInfo> page = industryInfoService.searchIndustries(new Page<IndustryInfo>(Integer.parseInt(currentPage),
                Integer.parseInt(pageSize)), industryCriteria);
        modelAndView.addObject("industryCriteria", industryCriteria);
        modelAndView.addObject("searchEngineMap", configService.getSearchEngineMap(industryCriteria.getTerminalType()));
        modelAndView.addObject("page", page);
        modelAndView.addObject("user", user);
        modelAndView.addObject("isDepartmentManager", isDepartmentManager);
        modelAndView.addObject("activeUsers", activeUsers);
        return modelAndView;
    }

    @GetMapping("/getIndustry/{uuid}")
    public ResponseEntity<?> getIndustry(@PathVariable("uuid") long uuid) {
        try {
            IndustryInfo industryInfo = industryInfoService.getIndustry(uuid);
            return new ResponseEntity<>(industryInfo, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/saveIndustryInfo")
    public ResponseEntity<?> saveIndustryInfo(@RequestBody IndustryInfo industryInfo, HttpServletRequest request) {
        try {
            String loginName = (String) request.getSession().getAttribute("username");
            industryInfoService.saveIndustryInfo(industryInfo, loginName);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/delIndustryInfo/{uuid}")
    public ResponseEntity<?> delIndustryInfo(@PathVariable("uuid") long uuid) {
        try {
            industryInfoService.delIndustryInfo(uuid);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updateIndustryUserID")
    public ResponseEntity<?> updateIndustryUserID(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            String userID = (String) requestMap.get("userID");
            industryInfoService.updateIndustryUserID(uuids, userID);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.OK);
        }
    }

    @PostMapping("/deleteIndustries")
    public ResponseEntity<?> deleteIndustries(@RequestBody Map<String, Object> requestMap) {
        try {
            String uuids = (String) requestMap.get("uuids");
            industryInfoService.deleteIndustries(uuids);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updateIndustryStatus")
    public ResponseEntity<?> updateIndustryStatus(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            industryInfoService.updateIndustryStatus(uuids);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.OK);
        }
    }

    @PostMapping("/uploadIndustryInfos")
    public boolean uploadIndustryInfos(@RequestParam(value = "file", required = false) MultipartFile file,
                                       HttpServletRequest request) {
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        String userName = (String) request.getSession().getAttribute("username");
        String excelType = request.getParameter("excelType");
        try {
            boolean uploaded = industryInfoService.handleExcel(file.getInputStream(), excelType, terminalType, userName);
            if (uploaded) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return false;
    }
}

