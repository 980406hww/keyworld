package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaiduIndexCriteria;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.PerformanceService;
import com.keymanager.monitoring.vo.SearchEngineResultItemVO;
import com.keymanager.monitoring.vo.SearchEngineResultVO;
import com.keymanager.monitoring.vo.ZTreeVO;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import com.keymanager.value.CustomerKeywordForCaptureTitle;
import com.keymanager.monitoring.vo.CustomerKeywordForOptimization;
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
                CustomerKeywordForCaptureTitle returnValue = null;
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                if (StringUtils.isEmpty(groupName)) {
                    //just baidu have whole site
                    if(Constants.SEARCH_ENGINE_BAIDU.equals(searchEngine)) {
                        returnValue = customerKeywordService.searchCustomerKeywordForCaptureTitle(terminalType, searchEngine);
                    }
                } else {
                    returnValue = (customerKeywordService.searchCustomerKeywordForCaptureTitle(groupName, terminalType,searchEngine));
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

    @RequestMapping(value = "/getCustomerKeywordSummaryInfos", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordSummaryInfos(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String keyword = (String) requestMap.get("keyword");
        try {
            if (validUser(userName, password)) {
                List<NegativeList> customerKeywordList = customerKeywordService.getCustomerKeywordSummaryInfos(keyword);
                return new ResponseEntity<Object>(customerKeywordList, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
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

                CustomerKeywordForOptimization customerKeywordForOptimization = customerKeywordService.searchCustomerKeywordsForOptimization(terminalType, clientID, version, true);
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
        String position = request.getParameter("position");

        String ip = getIP(request);

        try {
            if (validUser(userName, password)) {
                ClientStatus clientStatus = clientStatusService.selectById(clientID);
                String terminalType = clientStatus.getTerminalType();
                if(StringUtils.isNotBlank(position)) {
                    customerKeywordService.updateCustomerKeywordPosition(customerKeywordUuid, Integer.parseInt(position), null);
                }
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

    @RequestMapping(value = "/batchUpdateOptimizedCount", method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateOptimizedCount(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        List<Long> customerKeywordUuids = (List<Long>) requestMap.get("customerKeywordUuids");
        try {
            if (validUser(userName, password)) {
                customerKeywordService.batchUpdateOptimizedCount(customerKeywordUuids);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
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
                customerKeywordService.updateCustomerKeywordPosition(customerKeywordUuid, position, Utils.getCurrentTimestamp());
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
        Integer captureRankJobUuid = (Integer) requestMap.get("captureRankJobUuid");
        try {
            if (validUser(userName, password)) {
                CustomerKeywordForCapturePosition capturePosition = customerKeywordService.getCustomerKeywordForCapturePosition(terminalType,
                        groupNames, customerUuid != null ? customerUuid.longValue() : null, startTime, captureRankJobUuid.longValue());
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

    @RequestMapping(value = "/searchCustomerNegativeKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> searchCustomerNegativeKeywords(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            Integer customerUuid = (Integer) requestMap.get("customerUuid");
            if (validUser(userName, password)) {
                if(customerUuid != null){
                    String [] keywords = customerKeywordService.searchCustomerNegativeKeywords(customerUuid.longValue());
                    return new ResponseEntity<Object>(keywords,HttpStatus.OK);
                }else {
                    return new ResponseEntity<Object>(null,HttpStatus.OK);
                }
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerSource", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerSource(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                List<ZTreeVO> zTreeList = customerKeywordService.getCustomerSource();
                return new ResponseEntity<Object>(zTreeList, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/findAllNegativeCustomerKeyword", method = RequestMethod.POST)
    public ResponseEntity<?> findAllNegativeCustomerKeyword(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            String searchEngine = (String) requestMap.get("searchEngine");
            if (validUser(userName, password)) {
                String customerKeywordStr = customerKeywordService.findAllNegativeCustomerKeyword(searchEngine);
                String[] customerKeywords = customerKeywordStr.split(",");
                return new ResponseEntity<Object>(customerKeywords, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
