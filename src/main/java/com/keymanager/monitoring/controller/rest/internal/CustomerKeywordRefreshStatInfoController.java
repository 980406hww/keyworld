package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.service.CustomerKeywordRefreshStatInfoService;
import com.keymanager.monitoring.vo.CustomerKeywordRefreshStatInfoVO;
import com.keymanager.util.FileUtil;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import org.apache.commons.collections.CollectionUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj08 on 2017/9/12.
 */
@RestController
@RequestMapping(value = "/internal/refreshstatinfo")
public class CustomerKeywordRefreshStatInfoController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordRefreshStatInfoController.class);

    @Autowired
    private CustomerKeywordRefreshStatInfoService customerKeywordRefreshStatInfoService;

    @RequiresPermissions("/internal/refreshstatinfo/searchRefreshStatInfos")
    @RequestMapping(value = "/searchRefreshStatInfos", method = RequestMethod.GET)
    public ModelAndView searchRefreshStatInfos(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/refresh/list");
        return modelAndView;
    }

    @RequiresPermissions("/internal/refreshstatinfo/searchRefreshStatInfos")
    @RequestMapping(value = "/searchRefreshStatInfos", method = RequestMethod.POST)
    public ModelAndView searchRefreshStatInfosPost(HttpServletRequest request, CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        try {
            return constructNegativeListModelAndView(request, customerKeywordRefreshStatInfoCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/refresh/list");
        }
    }

    private ModelAndView constructNegativeListModelAndView(HttpServletRequest request, CustomerKeywordRefreshStatInfoCriteria refreshStatInfoCriteria) {
        ModelAndView modelAndView = new ModelAndView("/refresh/list");
        String entryType = (String) request.getSession().getAttribute("entryType");
        refreshStatInfoCriteria.setEntryType(entryType);
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        refreshStatInfoCriteria.setTerminalType(terminalType);

        List<CustomerKeywordRefreshStatInfoVO> refreshStatInfoVOs = customerKeywordRefreshStatInfoService.generateCustomerKeywordStatInfo(refreshStatInfoCriteria);
        modelAndView.addObject("refreshStatInfoCriteria", refreshStatInfoCriteria);
        modelAndView.addObject("refreshStatInfoVOs", refreshStatInfoVOs);
        return modelAndView;
    }

    @RequiresPermissions("/internal/customerKeyword/downloadCustomerKeywordInfo")
    @RequestMapping(value = "/downloadKeywordUrlByGroup", method = RequestMethod.POST)
    public ResponseEntity<?> downloadKeywordUrlByGroup(@RequestBody Map<String, Object> requestMap, HttpServletRequest request, HttpServletResponse response) {
        List<String> groups = (List<String>) requestMap.get("groups");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        String entryType = (String) request.getSession().getAttribute("entryType");
        try {
            customerKeywordRefreshStatInfoService.searchKeywordUrlByGroup(terminalType, entryType, groups);
            downFile("keywordUrl.zip");
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch(Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/customerKeyword/uploadCustomerKeywords")
    @RequestMapping(value = "/uploadCSVFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVPSFile(@RequestParam(value = "file", required = false) MultipartFile file,
                                           @RequestParam(defaultValue = "百度", name = "searchEngine") String searchEngine,
                                           @RequestParam(defaultValue = "0", name = "reachStandardPosition") int reachStandardPosition, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String entryType = (String) request.getSession().getAttribute("entryType");
            String path = Utils.getWebRootPath() + "csvfile";
            File targetFile = new File(path, "positionInfo.csv");
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            customerKeywordRefreshStatInfoService.uploadCSVFile(terminalType, entryType, searchEngine, reachStandardPosition, targetFile);
            FileUtil.delFolder(path);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
