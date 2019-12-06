package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.base.ExternalBaseCriteria;
import com.keymanager.ckadmin.entity.MachineInfo;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.MachineInfoService;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.ckadmin.util.StringUtil;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.vo.SearchEngineResultVO;
import com.keymanager.ckadmin.vo.OptimizationMachineVO;
import com.keymanager.ckadmin.vo.OptimizationVO;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.util.AESUtils;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/customerkeyword")
public class ExternalCustomerKeywordController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalCustomerKeywordController.class);

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "machineInfoService2")
    private MachineInfoService machineInfoService;

    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @Resource(name = "performanceService2")
    private PerformanceService performanceService;

    @RequestMapping(value = "/getCustomerKeywordForCapturePositionTemp", method = RequestMethod.POST)
    public ResponseEntity<?> getCustomerKeywordForCapturePositionTemp(@RequestBody Map<String, Object> requestMap) {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String terminalType = (String) requestMap.get("terminalType");
        String groupName = (String) requestMap.get("groupName");
        Date startTime = new Date((Long) requestMap.get("startTime"));
        Integer customerUuid = (requestMap.get("customerUuid") == null) ? null : (Integer) requestMap.get("customerUuid");
        Integer captureRankJobUuid = (Integer) requestMap.get("captureRankJobUuid");
        Integer qzSettingUuid = (Integer) requestMap.get("qzSettingUuid");
        Boolean saveTopThree = requestMap.get("saveTopThree") == null ? true : (Boolean) requestMap.get("saveTopThree");
        try {
            if (validUser(userName, password)) {
                synchronized (ExternalCustomerKeywordController.class) {
                    List<CustomerKeywordForCapturePosition> customerKeywordForCapturePositions = customerKeywordService.getCustomerKeywordForCapturePositionTemp(
                            qzSettingUuid != null ? qzSettingUuid.longValue() : null, terminalType, groupName, customerUuid != null ? customerUuid.longValue() : null,
                            startTime, captureRankJobUuid.longValue(), saveTopThree);
                    if (customerKeywordForCapturePositions.size() == 0) {
                        CustomerKeywordForCapturePosition customerKeywordForCapturePosition = new CustomerKeywordForCapturePosition();
                        customerKeywordForCapturePosition.setKeyword("end");
                        customerKeywordForCapturePositions.add(customerKeywordForCapturePosition);
                    }
                    return new ResponseEntity<Object>(customerKeywordForCapturePositions, HttpStatus.OK);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerKeywordForCapturePositionTemp:" + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCustomerKeywordPosition", method = RequestMethod.POST)
    public ResponseEntity<?> updateCustomerKeywordPosition(@RequestBody Map<String, Object> requestMap) {
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        try {
            if (validUser(userName, password)) {
                Integer capturePositionFailIdentify = (Integer) requestMap.get("capturePositionFailIdentify");
                Long customerKeywordUuid = Long.parseLong(requestMap.get("customerKeywordUuid").toString());
                Integer position = (Integer) requestMap.get("position");
                String ip = (String) requestMap.get("capturePositionIP");
                String clientID = (String) requestMap.get("clientID");
                String city = (String) requestMap.get("capturePositionCity");
                Date startTime = new Date((Long) requestMap.get("startTime"));
                if (null != position && position > -1) {
                    customerKeywordService.updateCustomerKeywordPosition(customerKeywordUuid, position, Utils.getCurrentTimestamp(), ip, city);
                } else {
                    customerKeywordService.updateCustomerKeywordQueryTime(customerKeywordUuid, capturePositionFailIdentify, startTime);
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

    /**
     * 外部链接 登录获取用户信息
     */
    @RequestMapping(value = "/getCustomerSource2", method = RequestMethod.POST)
    public ResultBean getCustomerSource(@RequestBody ExternalBaseCriteria criteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(criteria.getUserName(), criteria.getPassword())) {
                resultBean.setData(userInfoService.getUserInfo(criteria.getUserName()).getUserName());
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalUserLoginController.getCustomerSource()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 获得优化分组
     */
    @RequestMapping(value = "/getGroups2", method = RequestMethod.POST)
    public ResultBean getGroups(@RequestBody ExternalBaseCriteria baseCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(baseCriteria.getUserName(), baseCriteria.getPassword())) {
                boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(baseCriteria.getUserName()));
                if (isDepartmentManager) {
                    baseCriteria.setUserName(null);
                }
                resultBean.setData(customerKeywordService.getGroupsByUser(baseCriteria.getUserName(), "fm"));
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalCustomerKeywordController.getGroups()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 保存 关键字
     */
    @RequestMapping(value = "/saveCustomerKeywords2", method = RequestMethod.POST)
    public ResultBean saveCustomerKeywords(@RequestBody SearchEngineResultVO searchEngineResultVo) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(searchEngineResultVo.getUserName(), searchEngineResultVo.getPassword())) {
                customerKeywordService.addCustomerKeywords(searchEngineResultVo, searchEngineResultVo.getUserName());
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalCustomerKeywordController.saveCustomerKeywords()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequestMapping(value = "/fetchCustomerKeywordZip", method = RequestMethod.GET)
    public ResponseEntity<?> fetchCustomerKeywordForOptimization(HttpServletRequest request) throws Exception {
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
                    OptimizationVO optimizationVO = customerKeywordService.fetchCustomerKeywordForOptimization(machineInfo);
                    errorFlag.append("2");
                    if (optimizationVO != null) {
                        machineInfoService.updateMachineInfoVersion(clientID, version, true);
                        errorFlag.append("3");
                        byte[] compress = AESUtils.compress(AESUtils.encrypt(optimizationVO).getBytes());
                        errorFlag.append("4");
                        s = AESUtils.parseByte2HexStr(compress);
                        errorFlag.append("5");
                        performanceService.addPerformanceLog(terminalType + ":fetchCustomerKeywordZip", System.currentTimeMillis() - startMilleSeconds, null);
                        errorFlag.append("6");
                    }
                } else {
                    logger.error("fetchCustomerKeywordZip,     Not found clientID:" + clientID);
                }
                return ResponseEntity.status(HttpStatus.OK).body(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("fetchCustomerKeywordZip: " + errorFlag.toString() + "clientID:" + clientID + ex.getMessage());
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
        List<OptimizationMachineVO> machineVos = null;
        try {
            if (validUser(userName, password)) {
                MachineInfo machineInfo = machineInfoService.selectById(clientID);
                if (machineInfo != null) {
                    String terminalType = machineInfo.getTerminalType();
                    errorFlag.append("1");
                    machineVos = customerKeywordService.fetchCustomerKeywordForOptimizationList(machineInfo);
                    errorFlag.append("2");
                    if (CollectionUtils.isNotEmpty(machineVos)) {
                        machineInfoService.updateMachineInfoVersion(clientID, version, !CollectionUtils.isEmpty(machineVos));
                        errorFlag.append("3");
                        performanceService.addPerformanceLog(terminalType + ":fetchCustomerKeywordZip", System.currentTimeMillis() - startMilleSeconds, null);
                        errorFlag.append("4");
                    }
                } else {
                    logger.error("fetchCustomerKeywordZip, Not found clientID:" + clientID);
                }
                String result = CollectionUtils.isEmpty(machineVos) ? "" : AESUtils.encrypt(machineVos);
                errorFlag.append("6");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("fetchCustomerKeywordZip: " + errorFlag.toString() + "clientID:" + clientID + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

}
