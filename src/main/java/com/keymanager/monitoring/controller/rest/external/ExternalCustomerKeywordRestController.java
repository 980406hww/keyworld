package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaiduIndexCriteria;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.Performance;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.PerformanceService;
import com.keymanager.monitoring.vo.SearchEngineResultItemVO;
import com.keymanager.monitoring.vo.SearchEngineResultVO;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import com.keymanager.value.CustomerKeywordForCaptureTitle;
import com.keymanager.value.CustomerKeywordForOptimization;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/external/customerkeyword")
public class ExternalCustomerKeywordRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalCustomerKeywordRestController.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private ClientStatusService clientStatusService;

    @Autowired
    private PerformanceService performanceService;

    private String getIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @RequestMapping(value = "/getKeywordForCaptureTitle", method = RequestMethod.POST)
    public ResponseEntity<?> getKeywordForCaptureTitle(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        String groupName = (String) requestMap.get("group");
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String searchEngine = (String) requestMap.get("searchEngine");
        try {
            if (validUser(userName, password)) {
                //String returnValue = "";
                CustomerKeywordForCaptureTitle returnValue = null;
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                if (StringUtils.isEmpty(groupName)) {
                    returnValue = customerKeywordService.searchCustomerKeywordForCaptureTitle(terminalType);
                } else {
                    returnValue = (customerKeywordService.searchCustomerKeywordForCaptureTitle(groupName, terminalType));
                }
                    return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCustomerKeywordForCaptureTitle", method = RequestMethod.POST)
    public ResponseEntity<?> updateCustomerKeywordForCaptureTitle(@RequestBody SearchEngineResultItemVO searchEngineResultItemVO) throws Exception {
        try {
            if (validUser(searchEngineResultItemVO.getUserName(), searchEngineResultItemVO.getPassword())) {
                customerKeywordService.updateCustomerKeywordTitle(searchEngineResultItemVO);
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordsForCaptureIndex", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordsForCaptureIndex(@RequestBody BaseCriteria baseCriteria) throws Exception {
        try {
            if (validUser(baseCriteria.getUserName(), baseCriteria.getPassword())) {
                CustomerKeyword customerKeyword = customerKeywordService.getCustomerKeywordsForCaptureIndex();
                return new ResponseEntity<Object>(customerKeyword, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateKeywordIndex", method = RequestMethod.POST)
    public ResponseEntity<?> updateKeywordIndex(@RequestBody BaiduIndexCriteria baiduIndexCriteria) throws Exception {
        try {
            if (validUser(baiduIndexCriteria.getUserName(), baiduIndexCriteria.getPassword())) {
                customerKeywordService.updateCustomerKeywordIndex(baiduIndexCriteria);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getGroups", method = RequestMethod.POST)
    public ResponseEntity<?> getGroups(@RequestBody BaseCriteria baseCriteria) throws Exception {
        try {
            if (validUser(baseCriteria.getUserName(), baseCriteria.getPassword())) {
                List<String> groups = customerKeywordService.getGroups();
                return new ResponseEntity<Object>(groups, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeyword", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerKeywordForOptimization(HttpServletRequest request) throws Exception {
        long startMilleSeconds = System.currentTimeMillis();
        String clientID = request.getParameter("clientID");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String version = request.getParameter("version");
        try {
            if (validUser(userName, password)) {
                ClientStatus clientStatus = clientStatusService.selectById(clientID);
                String terminalType = clientStatus.getTerminalType();

                CustomerKeywordForOptimization customerKeywordForOptimization = customerKeywordService.searchCustomerKeywordsForOptimization(terminalType, clientID, version);
                if (customerKeywordForOptimization != null) {
                    customerKeywordService.updateOptimizationQueryTime(customerKeywordForOptimization.getUuid());
                }
                clientStatusService.updateClientVersion(clientID, version);

                performanceService.addPerformanceLog(terminalType + ":getCustomerKeyword", System.currentTimeMillis() - startMilleSeconds, null);
                return new ResponseEntity<Object>(customerKeywordForOptimization, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateOptimizedCount", method = RequestMethod.GET)
    public ResponseEntity<?> updateOptimizedCount(HttpServletRequest request) throws Exception {
        long startMilleSeconds = System.currentTimeMillis();
        String userName = request.getParameter("username");
        String password = request.getParameter("password");

        Long customerKeywordUuid = Long.parseLong(request.getParameter("uuid").trim());
        String count = request.getParameter("count");
        String clientID = request.getParameter("clientID");
        String freeSpace = request.getParameter("freespace");
        String version = request.getParameter("version");
        String city = request.getParameter("city");
        String status = request.getParameter("status");

        String ip = getIP(request);

        try {
            if (validUser(userName, password)) {
                ClientStatus clientStatus = clientStatusService.selectById(clientID);
                String terminalType = clientStatus.getTerminalType();
                customerKeywordService.updateOptimizationResult(terminalType, customerKeywordUuid, Integer.parseInt(count.trim()), ip, city, clientID,
                        status, freeSpace, version);
                performanceService.addPerformanceLog(terminalType + ":updateOptimizedCount", System.currentTimeMillis() - startMilleSeconds, null);

                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/adjustOptimizationCount", method = RequestMethod.POST)
    public ResponseEntity<?> adjustOptimizationCount(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        try {
            if (validUser(userName, password)) {
                customerKeywordService.adjustOptimizationCount();
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCustomerKeywordPosition", method = RequestMethod.POST)
    public ResponseEntity<?> updateCustomerKeywordPosition(@RequestBody Map<String, Object> requestMap) throws Exception {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");

        Long customerKeywordUuid = Long.parseLong(requestMap.get("customerKeywordUuid").toString());
        int position = (Integer) requestMap.get("position");
        try {
            if (validUser(userName, password)) {
                customerKeywordService.updateCustomerKeywordPosition(customerKeywordUuid, position);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordForCapturePosition", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordForCapturePosition(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String terminalType = (String) requestMap.get("terminalType");

        List<String> groupNames = (List<String>) requestMap.get("groupNames");

        Date startTime =  new Date((Long) requestMap.get("startTime"));
        Integer customerUuid = (requestMap.get("customerUuid") == null) ? null : (Integer) requestMap.get("customerUuid");
        Integer minutes = (Integer) requestMap.get("minutes");
        try {
            if (validUser(userName, password)) {
                CustomerKeywordForCapturePosition capturePosition = customerKeywordService.getCustomerKeywordForCapturePosition(terminalType,
                        groupNames, customerUuid != null ? customerUuid.longValue() : null, startTime, minutes);
                return new ResponseEntity<Object>(capturePosition, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/saveCustomerKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> saveCustomerKeywords(@RequestBody SearchEngineResultVO searchEngineResultVO, HttpServletRequest request) throws Exception {
        try {
            if (validUser(searchEngineResultVO.getUserName(), searchEngineResultVO.getPassword())) {
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                customerKeywordService.addCustomerKeywords(searchEngineResultVO, terminalType, searchEngineResultVO.getUserName());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/autoUpdateCustomerKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> autoUpdateCustomerKeywords(@RequestBody SearchEngineResultVO searchEngineResultVO, HttpServletRequest request) throws Exception {
        try {
            if (validUser(searchEngineResultVO.getUserName(), searchEngineResultVO.getPassword())) {
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                customerKeywordService.autoUpdateNegativeCustomerKeywords(searchEngineResultVO, terminalType, searchEngineResultVO.getUserName());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordForAutoUpdateNegative", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordForAutoUpdateNegative(@RequestBody SearchEngineResultVO searchEngineResultVO, HttpServletRequest request) throws Exception {
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        try {
            if (validUser(searchEngineResultVO.getUserName(), searchEngineResultVO.getPassword())) {
                SearchEngineResultVO result = customerKeywordService.getCustomerKeywordForAutoUpdateNegative(terminalType, searchEngineResultVO.getGroup());
                return new ResponseEntity<Object>(result, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateAutoUpdateNegativeTimeAs4MinutesAgo", method = RequestMethod.POST)
    public ResponseEntity<?> updateAutoUpdateNegativeTimeAs4MinutesAgo(@RequestBody SearchEngineResultVO searchEngineResultVO, HttpServletRequest request) throws Exception {
        try {
            if (validUser(searchEngineResultVO.getUserName(), searchEngineResultVO.getPassword())) {
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                customerKeywordService.updateAutoUpdateNegativeTimeAs4MinutesAgo(terminalType, searchEngineResultVO.getGroup());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
