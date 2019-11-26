package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.MachineInfoService;
import com.keymanager.ckadmin.util.StringUtil;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class ExternalCustomerKeywordController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalCustomerKeywordController.class);

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "machineInfoService2")
    private MachineInfoService machineInfoService;

    @RequestMapping(value = "/getCustomerKeywordForCapturePositionTemp2", method = RequestMethod.POST)
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
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("getCustomerKeywordForCapturePositionTemp:" + ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateCustomerKeywordPosition2", method = RequestMethod.POST)
    public ResponseEntity<?> updateCustomerKeywordPosition(@RequestBody Map<String, Object> requestMap) throws Exception {
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
}
