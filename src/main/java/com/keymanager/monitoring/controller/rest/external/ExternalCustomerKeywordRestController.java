package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaiduIndexCriteria;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.criteria.KeywordIndexCriteria;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.MachineInfo;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.MachineInfoService;
import com.keymanager.monitoring.service.PerformanceService;
import com.keymanager.monitoring.vo.*;
import com.keymanager.util.AESUtils;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import com.keymanager.value.CustomerKeywordForCaptureTitle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.CollectionUtils;
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

@RestController
@RequestMapping(value = "/external/customerkeyword")
public class ExternalCustomerKeywordRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory
        .getLogger(ExternalCustomerKeywordRestController.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private MachineInfoService machineInfoService;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private ConfigService configService;

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
    public ResponseEntity<?> getKeywordForCaptureTitle(@RequestBody Map<String, Object> requestMap,
        HttpServletRequest request) throws Exception {
        String groupName = (String) requestMap.get("group");
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String searchEngine = (String) requestMap.get("searchEngine");
        try {
            if (validUser(userName, password)) {
                List<CustomerKeywordForCaptureTitle> customerKeywordForCaptureTitles = null;
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                if (StringUtils.isEmpty(groupName)) {
                    //just baidu have whole site
                    if (Constants.SEARCH_ENGINE_BAIDU.equals(searchEngine)) {
                        customerKeywordForCaptureTitles = customerKeywordService
                            .searchCustomerKeywordsForCaptureTitle(terminalType, searchEngine, 1);
                    }
                } else {
                    customerKeywordForCaptureTitles = (customerKeywordService
                        .searchCustomerKeywordsForCaptureTitle(groupName, terminalType,
                            searchEngine, 1));
                }
                CustomerKeywordForCaptureTitle customerKeywordForCaptureTitle = null;
                if (CollectionUtils.isNotEmpty(customerKeywordForCaptureTitles)) {
                    customerKeywordForCaptureTitle = customerKeywordForCaptureTitles.get(0);
                } else {
                    customerKeywordForCaptureTitle = new CustomerKeywordForCaptureTitle();
                    customerKeywordForCaptureTitle.setKeyword("end");
                }
                return new ResponseEntity<Object>(customerKeywordForCaptureTitle, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getKeywordForCaptureTitle:  " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCustomerKeywordForCaptureTitle", method = RequestMethod.POST)
    public ResponseEntity<?> updateCustomerKeywordForCaptureTitle(
        @RequestBody SearchEngineResultItemVO searchEngineResultItemVO) throws Exception {
        try {
            if (validUser(searchEngineResultItemVO.getUserName(),
                searchEngineResultItemVO.getPassword())) {
                customerKeywordService.updateCustomerKeywordTitle(searchEngineResultItemVO);
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("updateCustomerKeywordForCaptureTitle:    " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    // new
    @RequestMapping(value = "/updateCustomerKeywordsForCaptureTitle", method = RequestMethod.POST)
    public ResponseEntity<?> updateCustomerKeywordsForCaptureTitle(
        @RequestBody SearchEngineResultItemsVO searchEngineResultItemsVO) throws Exception {
        try {
            if (validUser(searchEngineResultItemsVO.getUserName(),
                searchEngineResultItemsVO.getPassword())) {
                customerKeywordService.updateCustomerKeywordsTitle(
                    searchEngineResultItemsVO.getSearchEngineResultItemVOs());
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("updateCustomerKeywordsForCaptureTitle:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    // new
    @RequestMapping(value = "/getKeywordsForCaptureTitle", method = RequestMethod.POST)
    public ResponseEntity<?> getKeywordsForCaptureTitle(@RequestBody Map<String, Object> requestMap,
        HttpServletRequest request) throws Exception {
        String groupName = (String) requestMap.get("group");
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String searchEngine = (String) requestMap.get("searchEngine");
        try {
            if (validUser(userName, password)) {
                List<CustomerKeywordForCaptureTitle> customerKeywordForCaptureTitles = null;
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                Config countConfig = configService
                    .getConfig(Constants.CONFIG_TYPE_CAPTURE_TITLE, "BatchCount");
                Integer batchCount = Integer.parseInt(countConfig.getValue());
                if (StringUtils.isEmpty(groupName)) {
                    //just baidu have whole site
                    if (Constants.SEARCH_ENGINE_BAIDU.equals(searchEngine)) {
                        customerKeywordForCaptureTitles = customerKeywordService
                            .searchCustomerKeywordsForCaptureTitle(terminalType, searchEngine,
                                batchCount);
                    }
                } else {
                    customerKeywordForCaptureTitles = (customerKeywordService
                        .searchCustomerKeywordsForCaptureTitle(groupName, terminalType,
                            searchEngine, batchCount));
                }
                if (customerKeywordForCaptureTitles == null) {
                    customerKeywordForCaptureTitles = new ArrayList<CustomerKeywordForCaptureTitle>();
                }
                if (CollectionUtils.isEmpty(customerKeywordForCaptureTitles)) {
                    CustomerKeywordForCaptureTitle customerKeywordForCaptureTitle = new CustomerKeywordForCaptureTitle();
                    customerKeywordForCaptureTitle.setKeyword("end");
                    customerKeywordForCaptureTitles.add(customerKeywordForCaptureTitle);
                }
                return new ResponseEntity<Object>(customerKeywordForCaptureTitles, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getKeywordsForCaptureTitle:   " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordsForCaptureIndex", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordsForCaptureIndex(
        @RequestBody BaseCriteria baseCriteria) throws Exception {
        try {
            if (validUser(baseCriteria.getUserName(), baseCriteria.getPassword())) {
                CustomerKeyword customerKeyword = customerKeywordService
                    .getCustomerKeywordsForCaptureIndex();
                return new ResponseEntity<Object>(customerKeyword, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerKeywordsForCaptureIndex:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateKeywordIndex", method = RequestMethod.POST)
    public ResponseEntity<?> updateKeywordIndex(@RequestBody BaiduIndexCriteria baiduIndexCriteria)
        throws Exception {
        try {
            if (validUser(baiduIndexCriteria.getUserName(), baiduIndexCriteria.getPassword())) {
                customerKeywordService.updateCustomerKeywordIndex(baiduIndexCriteria);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("updateKeywordIndex:    " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getGroups", method = RequestMethod.POST)
    public ResponseEntity<?> getGroups(@RequestBody BaseCriteria baseCriteria) throws Exception {
        try {
            if (validUser(baseCriteria.getUserName(), baseCriteria.getPassword())) {
                List<String> groups = customerKeywordService.getGroups(null);
                return new ResponseEntity<Object>(groups, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getGroups:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordSummaryInfos", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordSummaryInfos(
        @RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String keyword = (String) requestMap.get("keyword");
        String terminalType = (String) requestMap.get("terminalType");
        try {
            if (validUser(userName, password)) {
                long startMilleSeconds = System.currentTimeMillis();
                List<NegativeList> customerKeywordList = customerKeywordService
                    .getCustomerKeywordSummaryInfos(terminalType, keyword);
                performanceService
                    .addPerformanceLog(terminalType + ":getCustomerKeywordSummaryInfos",
                        System.currentTimeMillis() - startMilleSeconds,
                        "Record Count: " + (customerKeywordList != null ? customerKeywordList.size()
                            : 0));
                return new ResponseEntity<Object>(customerKeywordList, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerKeywordSummaryInfos:      " + ex.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordZip", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerKeywordForOptimizationZip(HttpServletRequest request)
        throws Exception {
        long startMilleSeconds = System.currentTimeMillis();
        String clientID = request.getParameter("clientID");
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String version = request.getParameter("version");
        try {
            if (validUser(userName, password)) {
                MachineInfo machineInfo = machineInfoService.selectById(clientID);
                String s = "";
                if (machineInfo != null) {
                    String terminalType = machineInfo.getTerminalType();
                    CustomerKeywordForOptimizationSimple customerKeywordForOptimization = customerKeywordService
                        .searchCustomerKeywordsForOptimizationZip(terminalType, clientID, version,
                            true);
                    if (customerKeywordForOptimization != null) {
                        machineInfoService.updateMachineInfoVersion(clientID, version,
                            customerKeywordForOptimization != null);
                        byte[] compress = AESUtils
                            .compress(AESUtils.encrypt(customerKeywordForOptimization).getBytes());
                        s = AESUtils.parseByte2HexStr(compress);
                        performanceService.addPerformanceLog(terminalType + ":getCustomerKeyword",
                            System.currentTimeMillis() - startMilleSeconds, null);
                    }
                } else {
                    logger.error("getCustomerKeywordZip,     Not found clientID:" + clientID);
                }
                return ResponseEntity.status(HttpStatus.OK).body(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerKeywordZip:     " + clientID + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/fetchCustomerKeywordZip", method = RequestMethod.GET)
    public ResponseEntity<?> fetchCustomerKeywordForOptimization(HttpServletRequest request)
        throws Exception {
        long startMilleSeconds = System.currentTimeMillis();
        String clientID = request.getParameter("clientID");
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String version = request.getParameter("version");
        StringBuilder errorFlag = new StringBuilder("");
        try {
            if (validUser(userName, password)) {
                MachineInfo machineInfo = machineInfoService.selectById(clientID);
                String s = "";
                if (machineInfo != null) {
                    String terminalType = machineInfo.getTerminalType();
                    errorFlag.append("1");
                    OptimizationVO optimizationVO = customerKeywordService
                        .fetchCustomerKeywordForOptimization(machineInfo);
                    errorFlag.append("2");
                    if (optimizationVO != null) {
                        machineInfoService
                            .updateMachineInfoVersion(clientID, version, optimizationVO != null);
                        errorFlag.append("3");
                        byte[] compress = AESUtils
                            .compress(AESUtils.encrypt(optimizationVO).getBytes());
                        errorFlag.append("4");
                        s = AESUtils.parseByte2HexStr(compress);
                        errorFlag.append("5");
                        performanceService
                            .addPerformanceLog(terminalType + ":fetchCustomerKeywordZip",
                                System.currentTimeMillis() - startMilleSeconds, null);
                        errorFlag.append("6");
                    }
                } else {
                    logger.error("fetchCustomerKeywordZip,     Not found clientID:" + clientID);
                }
                return ResponseEntity.status(HttpStatus.OK).body(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(
                "fetchCustomerKeywordZip: " + errorFlag.toString() + "clientID:" + clientID + ex
                    .getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/fetchCustomerKeywordZipList", method = RequestMethod.GET)
    public ResponseEntity<?> fetchCustomerKeywordListForOptimization(HttpServletRequest request) throws Exception {
        long startMilleSeconds = System.currentTimeMillis();
        String clientID = request.getParameter("clientID");
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String version = request.getParameter("version");
        StringBuilder errorFlag = new StringBuilder("");
        List<OptimizationMachineVO> machineVOList=null;
        try {
            if (validUser(userName, password)) {
                MachineInfo machineInfo = machineInfoService.selectById(clientID);
                if (machineInfo != null) {
                    String terminalType = machineInfo.getTerminalType();
                    errorFlag.append("1");
                    machineVOList= customerKeywordService.fetchCustomerKeywordForOptimizationList(machineInfo);
                    errorFlag.append("2");
                    if (!CollectionUtils.isEmpty(machineVOList)) {
                        machineInfoService.updateMachineInfoVersion(clientID, version, !CollectionUtils.isEmpty(machineVOList));
                        errorFlag.append("3");
                        performanceService.addPerformanceLog(terminalType + ":fetchCustomerKeywordZip", System.currentTimeMillis() - startMilleSeconds, null);
                        errorFlag.append("4");
                    }
                } else {
                    logger.error("fetchCustomerKeywordZip,     Not found clientID:" + clientID);
                }
                String result = CollectionUtils.isEmpty(machineVOList) ? "" : AESUtils.encrypt(machineVOList);
                errorFlag.append("6");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("fetchCustomerKeywordZip: " + errorFlag.toString() + "clientID:" + clientID + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateOptimizedCount", method = RequestMethod.GET)
    public ResponseEntity<?> updateOptimizedCount(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");

        UpdateOptimizedCountVO updateOptimizedCountVO = new UpdateOptimizedCountVO();
        updateOptimizedCountVO.setCustomerKeywordUuid(Long.parseLong(request.getParameter("uuid").trim()));
        updateOptimizedCountVO.setCount(Integer.parseInt(request.getParameter("count")));
        updateOptimizedCountVO.setClientID(request.getParameter("clientID"));
        updateOptimizedCountVO.setFreeSpace(request.getParameter("freespace"));
        updateOptimizedCountVO.setVersion(request.getParameter("version"));
        updateOptimizedCountVO.setCity(request.getParameter("city"));
        updateOptimizedCountVO.setStatus(request.getParameter("status"));
        updateOptimizedCountVO.setRunningProgramType(request.getParameter("runningProgramType"));
        updateOptimizedCountVO.setCpuCount(request.getParameter("cpuCount") == null ? 0
            : Integer.parseInt(request.getParameter("cpuCount")));
        updateOptimizedCountVO.setMemory(request.getParameter("memory") == null ? 0
            : Integer.parseInt(request.getParameter("memory")));
        updateOptimizedCountVO.setIp(getIP(request));

        String position = request.getParameter("position");

        try {
            if (validUser(userName, password)) {

//                if (StringUtils.isNotBlank(position)) {
//                    customerKeywordService.updateCustomerKeywordPosition(customerKeywordUuid,
//                        Integer.parseInt(position), null, null, null);
//                }
//                customerKeywordService.updateOptimizationResult(customerKeywordUuid,
//                    Integer.parseInt(count.trim()), ip, city, clientID,
//                    status, freeSpace, version, runningProgramType, cpuCount, memory);
                customerKeywordService.cacheUpdateOptimizedCountResult(updateOptimizedCountVO);
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("updateOptimizedCount:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/batchUpdateOptimizedCount", method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateOptimizedCount(@RequestBody Map<String, Object> requestMap,
        HttpServletRequest request) {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        List<Long> customerKeywordUuids = (List<Long>) requestMap.get("customerKeywordUuids");
        try {
            if (validUser(userName, password)) {
                customerKeywordService.batchUpdateOptimizedCount(customerKeywordUuids);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("batchUpdateOptimizedCount:      " + ex.getMessage());
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
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("adjustOptimizationCount:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCustomerKeywordPosition", method = RequestMethod.POST)
    public ResponseEntity<?> updateCustomerKeywordPosition(
        @RequestBody Map<String, Object> requestMap) throws Exception {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");

        Long customerKeywordUuid = Long.parseLong(requestMap.get("customerKeywordUuid").toString());
        int position = (Integer) requestMap.get("position");
        String ip = (String) requestMap.get("capturePositionIP");
        String clientID = (String) requestMap.get("clientID");
        String city = (String) requestMap.get("capturePositionCity");
        Date startTime = new Date((Long) requestMap.get("startTime"));
        try {
            if (validUser(userName, password)) {
                if (position > -1) {
                    customerKeywordService
                        .updateCustomerKeywordPosition(customerKeywordUuid, position,
                            Utils.getCurrentTimestamp(), ip, city);
                } else {
                    customerKeywordService
                        .updateCustomerKeywordQueryTime(customerKeywordUuid, startTime);
                }
                if (StringUtil.isNotNullNorEmpty(clientID)) {
                    machineInfoService.updateMachineInfoForCapturePosition(clientID);
                }
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("updateCustomerKeywordPosition:        " + ex.getMessage());
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordForCapturePosition", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordForCapturePosition(
        @RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String terminalType = (String) requestMap.get("terminalType");

        List<String> groupNames = (List<String>) requestMap.get("groupNames");

        Date startTime = new Date((Long) requestMap.get("startTime"));
        Integer customerUuid = (requestMap.get("customerUuid") == null) ? null
            : (Integer) requestMap.get("customerUuid");
        Integer captureRankJobUuid = (Integer) requestMap.get("captureRankJobUuid");
        try {
            if (validUser(userName, password)) {
                CustomerKeywordForCapturePosition capturePosition = customerKeywordService
                    .getCustomerKeywordForCapturePosition(terminalType,
                        groupNames, customerUuid != null ? customerUuid.longValue() : null,
                        startTime, captureRankJobUuid.longValue());
                if (capturePosition == null) {
                    capturePosition = new CustomerKeywordForCapturePosition();
                    capturePosition.setKeyword("end");
                }
                return new ResponseEntity<Object>(capturePosition, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerKeywordForCapturePosition:        " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordForCapturePositionTemp", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordForCapturePositionTemp(
        @RequestBody Map<String, Object> requestMap) {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String terminalType = (String) requestMap.get("terminalType");
        String groupName = (String) requestMap.get("groupName");
        Date startTime = new Date((Long) requestMap.get("startTime"));
        Integer customerUuid = (requestMap.get("customerUuid") == null) ? null
            : (Integer) requestMap.get("customerUuid");
        Integer captureRankJobUuid = (Integer) requestMap.get("captureRankJobUuid");
        Integer qzSettingUuid = (Integer) requestMap.get("qzSettingUuid");
        Boolean saveTopThree = requestMap.get("saveTopThree") == null ? true
            : (Boolean) requestMap.get("saveTopThree");
        try {
            if (validUser(userName, password)) {
                List<CustomerKeywordForCapturePosition> customerKeywordForCapturePositions = customerKeywordService
                    .getCustomerKeywordForCapturePositionTemp(
                        qzSettingUuid != null ? qzSettingUuid.longValue() : null, terminalType,
                        groupName, customerUuid != null ? customerUuid.longValue() : null,
                        startTime, captureRankJobUuid.longValue(), saveTopThree);
                if (customerKeywordForCapturePositions.size() == 0) {
                    CustomerKeywordForCapturePosition customerKeywordForCapturePosition = new CustomerKeywordForCapturePosition();
                    customerKeywordForCapturePosition.setKeyword("end");
                    customerKeywordForCapturePositions.add(customerKeywordForCapturePosition);
                }
                return new ResponseEntity<Object>(customerKeywordForCapturePositions,
                    HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerKeywordForCapturePositionTemp:" + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/saveCustomerKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> saveCustomerKeywords(
        @RequestBody SearchEngineResultVO searchEngineResultVO, HttpServletRequest request)
        throws Exception {
        try {
            if (validUser(searchEngineResultVO.getUserName(), searchEngineResultVO.getPassword())) {
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                customerKeywordService.addCustomerKeywords(searchEngineResultVO, terminalType,
                    searchEngineResultVO.getUserName());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("saveCustomerKeywords:    " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/autoUpdateCustomerKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> autoUpdateCustomerKeywords(
        @RequestBody SearchEngineResultVO searchEngineResultVO, HttpServletRequest request)
        throws Exception {
        try {
            if (validUser(searchEngineResultVO.getUserName(), searchEngineResultVO.getPassword())) {
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                customerKeywordService
                    .autoUpdateNegativeCustomerKeywords(searchEngineResultVO, terminalType,
                        searchEngineResultVO.getUserName());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("autoUpdateCustomerKeywords:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerKeywordForAutoUpdateNegative", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordForAutoUpdateNegative(
        @RequestBody SearchEngineResultVO searchEngineResultVO, HttpServletRequest request)
        throws Exception {
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        try {
            if (validUser(searchEngineResultVO.getUserName(), searchEngineResultVO.getPassword())) {
                SearchEngineResultVO result = customerKeywordService
                    .getCustomerKeywordForAutoUpdateNegative(terminalType,
                        searchEngineResultVO.getGroup());
                return new ResponseEntity<Object>(result, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerKeywordForAutoUpdateNegative:      " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateAutoUpdateNegativeTimeAs4MinutesAgo", method = RequestMethod.POST)
    public ResponseEntity<?> updateAutoUpdateNegativeTimeAs4MinutesAgo(
        @RequestBody SearchEngineResultVO searchEngineResultVO, HttpServletRequest request)
        throws Exception {
        try {
            if (validUser(searchEngineResultVO.getUserName(), searchEngineResultVO.getPassword())) {
                String terminalType = TerminalTypeMapping.getTerminalType(request);
                customerKeywordService.updateAutoUpdateNegativeTimeAs4MinutesAgo(terminalType,
                    searchEngineResultVO.getGroup());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("updateAutoUpdateNegativeTimeAs4MinutesAgo:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/searchCustomerNegativeKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> searchCustomerNegativeKeywords(
        @RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            Integer customerUuid = (Integer) requestMap.get("customerUuid");
            if (validUser(userName, password)) {
                if (customerUuid != null) {
                    String[] keywords = customerKeywordService
                        .searchCustomerNegativeKeywords(customerUuid.longValue());
                    return new ResponseEntity<Object>(keywords, HttpStatus.OK);
                } else {
                    return new ResponseEntity<Object>(null, HttpStatus.OK);
                }
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("searchCustomerNegativeKeywords:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerSource", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerSource(@RequestBody Map<String, Object> requestMap,
        HttpServletRequest request) throws Exception {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerSource:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCustomerSources", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerSources(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                List<customerSourceVO> customerSources = customerKeywordService.getCustomerSource();
                return new ResponseEntity<Object>(customerSources, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerSources:    " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/findAllNegativeCustomerKeyword", method = RequestMethod.POST)
    public ResponseEntity<?> findAllNegativeCustomerKeyword(
        @RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            String searchEngine = (String) requestMap.get("searchEngine");
            if (validUser(userName, password)) {
                String customerKeywordStr = customerKeywordService
                    .findAllNegativeCustomerKeyword(searchEngine);
                String[] customerKeywords = customerKeywordStr.split(",");
                return new ResponseEntity<Object>(customerKeywords, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("findAllNegativeCustomerKeyword:      " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getCheckingEnteredKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> getCheckingEnteredKeywords(
        @RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                return new ResponseEntity<Object>(
                    customerKeywordService.getCheckingEnteredKeywords(), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getNoEnteredKeywords:  " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCheckingEnteredKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> updateCheckingEnteredKeywords(
        @RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                JSONArray jsonArray = JSONArray
                    .fromObject(requestMap.get("customerKeywordEnteredVos"));
                customerKeywordService.updateCheckingEnteredKeywords(
                    JSONArray.toList(jsonArray, new CustomerKeywordEnteredVO(), new JsonConfig()));
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("updateNoEnteredKeywords:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getTenCustomerKeywordsForCaptureIndex", method = RequestMethod.POST)
    public ResponseEntity<?> getTenCustomerKeywordsForCaptureIndex(
        @RequestBody BaseCriteria baseCriteria) throws Exception {
        try {
            if (validUser(baseCriteria.getUserName(), baseCriteria.getPassword())) {
                List<ExternalCustomerKeywordVO> customerKeywords = customerKeywordService
                    .getTenCustomerKeywordsForCaptureIndex();
                return new ResponseEntity<Object>(customerKeywords, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getTenCustomerKeywordsForCaptureIndex:     " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateKeywordsIndex", method = RequestMethod.POST)
    public ResponseEntity<?> updateKeywordsIndex(
        @RequestBody KeywordIndexCriteria keywordIndexCriteria) throws Exception {
        try {
            if (validUser(keywordIndexCriteria.getUserName(), keywordIndexCriteria.getPassword())) {
                customerKeywordService.updateCustomerKeywordIndexByKeywords(keywordIndexCriteria);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("updateKeywordsIndex:    " + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

}
