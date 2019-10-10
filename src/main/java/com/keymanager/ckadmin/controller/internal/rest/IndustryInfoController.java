package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.IndustryCriteria;
import com.keymanager.ckadmin.entity.IndustryInfo;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.excel.operator.IndustryDetailInfoCsvExportWriter;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.IndustryInfoService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.util.TerminalTypeMapping;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/internal/industryList")
public class IndustryInfoController {
    private static final Logger logger = LoggerFactory.getLogger(IndustryInfoController.class);

    @Resource(name = "industryInfoService2")
    private IndustryInfoService industryInfoService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @RequiresPermissions("/internal/industry/searchIndustries")
    @GetMapping("/toIndustryList")
    public ModelAndView toIndustryList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("industryList/Industry");
        return mv;
    }

    @RequiresPermissions("/internal/industry/uploadIndustryInfos")
    @GetMapping("/toUploadIndustryInfo")
    public ModelAndView toUploadIndustryInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("industryList/UploadExcel");
        return mv;
    }

    @RequiresPermissions("/internal/industry/saveIndustry")
    @GetMapping("/toSaveIndustryInfo")
    public ModelAndView toSaveIndustryInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("industryList/AddIndustry");
        return mv;
    }

    @RequiresPermissions("/internal/industry/updateIndustryUserID")
    @GetMapping("/updateOwner")
    public ModelAndView updateOwner() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("industryList/UpdateOwner");
        return mv;
    }

    @RequiresPermissions("/internal/industry/uploadIndustryInfos")
    @PostMapping("/uploadIndustryInfos")
    public ResultBean uploadIndustryInfos(@RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(name = "terminalType") String terminalType, @RequestParam(name = "excelType") String excelType, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        String userName = (String) request.getSession().getAttribute("username");
        try {
            boolean uploaded = industryInfoService.handleExcel(file.getInputStream(), excelType, terminalType, userName);
            if (uploaded) {
                resultBean.setCode(200);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industry/saveIndustry")
    @PostMapping("/downloadIndustryInfo")
    public ResultBean downloadIndustryInfo(@RequestParam("industryUuids") String industryUuids,  HttpServletResponse response) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> uuids = Arrays.asList(industryUuids.split(","));
            List<Map> industryDetailVos = industryInfoService.getIndustryInfos(uuids);
            if (CollectionUtils.isNotEmpty(industryDetailVos)) {
                IndustryDetailInfoCsvExportWriter.exportCsv(industryDetailVos);
                IndustryDetailInfoCsvExportWriter.downloadZip(response);
            }
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industry/saveIndustry")
    @PostMapping("/updateIndustryStatus")
    public ResultBean updateIndustryStatus(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            industryInfoService.updateIndustryStatus(uuids);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industry/updateIndustryUserID")
    @PostMapping("/updateIndustryUserID")
    public ResultBean updateIndustryUserID(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            String userID = (String) requestMap.get("userID");
            industryInfoService.updateIndustryUserID(uuids, userID);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industry/saveIndustry")
    @PostMapping("/saveIndustryInfo")
    public ResultBean saveIndustryInfo(@RequestBody IndustryInfo industryInfo, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            String loginName = (String) request.getSession().getAttribute("username");
            industryInfoService.saveIndustryInfo(industryInfo, loginName);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industry/delIndustry")
    @GetMapping("/delIndustryInfo/{uuid}")
    public ResultBean delIndustryInfo(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            industryInfoService.delIndustryInfo(uuid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industry/deleteIndustries")
    @PostMapping("/deleteIndustries")
    public ResultBean deleteIndustries(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            industryInfoService.deleteIndustries(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industry/saveIndustry")
    @GetMapping("/getIndustry/{uuid}")
    public ResultBean getIndustry(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            IndustryInfo industryInfo = industryInfoService.getIndustry(uuid);
            resultBean.setCode(200);
            resultBean.setData(industryInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/industry/searchIndustries")
    @PostMapping("/searchIndustries")
    public ResultBean searchIndustriesPost(HttpServletRequest request, @RequestBody IndustryCriteria industryCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
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
            Page<IndustryInfo> page = new Page<>(industryCriteria.getPage(), industryCriteria.getLimit());
            page = industryInfoService.searchIndustries(page, industryCriteria);
            List<IndustryInfo> industryInfoList = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(industryInfoList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping("/returnSelectData")
    public ResultBean returnSelectData(HttpServletRequest request, @RequestBody IndustryCriteria industryCriteria){
        ResultBean resultBean = new ResultBean();
        try {
            HttpSession session = request.getSession();
            String loginName = (String) session.getAttribute("username");
            UserInfo user = userInfoService.getUserInfo(loginName);
            List<UserInfo> activeUsers = userInfoService.findActiveUsers();
            if (null == industryCriteria.getTerminalType()) {
                industryCriteria.setTerminalType(TerminalTypeMapping.getTerminalType(request));
            }
            Map<String, String> searchEngineMap = configService.getSearchEngineMap(industryCriteria.getTerminalType());
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("activeUsers", activeUsers);
            data.put("searchEngineMap", searchEngineMap);
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }
}
