package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.QZChargeRuleService;
import com.keymanager.monitoring.service.QZOperationTypeService;
import com.keymanager.monitoring.service.QZSettingService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.PortTerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/qzsetting")
public class QZSettingRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(QZSettingRestController.class);

	@Autowired
	private QZSettingService qzSettingService;

	@Autowired
	private QZChargeRuleService qzChargeRuleService;

	@Autowired
	private QZOperationTypeService qzOperationTypeService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/updateImmediately", method = RequestMethod.POST)
	public ResponseEntity<?> updateImmediately(@RequestBody Map<String, Object> requestMap) throws Exception{
		String uuids = (String) requestMap.get("uuids");
		String returnValue = null;
		try {
			qzSettingService.updateImmediately(uuids);
			returnValue = "{\"status\":true}";
		}catch(Exception ex){
			logger.error(ex.getMessage());
			ex.printStackTrace();
			returnValue = "{\"status\":false}";
		}
		return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> saveQZSetting(@RequestBody QZSetting qzSetting){
		qzSettingService.saveQZSetting(qzSetting);
		return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
	}

	@RequestMapping(value ="/delete/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteQZSetting(@PathVariable("uuid") Long uuid){
		return new ResponseEntity<Object>(qzSettingService.deleteOne(uuid), HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteQZSettings", method = RequestMethod.POST)
	public ResponseEntity<?> deleteQZSettings(@RequestBody Map<String, Object> requestMap){
		List<String> uuids = (List<String>) requestMap.get("uuids");
		return new ResponseEntity<Object>(qzSettingService.deleteAll(uuids) , HttpStatus.OK);
	}

	@RequestMapping(value = "/searchQZSettings", method = RequestMethod.POST)
	public ResponseEntity<?> findQZSettings(@RequestBody Map<String, Object> requestMap){
		Long uuid = (Long) requestMap.get("uuid");
		Long customerUuid = (Long) requestMap.get("customerUuid");
		String domain = (String) requestMap.get("domain");
		String group = (String) requestMap.get("group");
		String updateStatus = (String) requestMap.get("updateStatus");
		Integer pageNumber = (Integer) requestMap.get("pageNumber");
		Integer pageSize = (Integer) requestMap.get("pageSize");
		Page<QZSetting> page = new Page<QZSetting>(pageNumber, pageSize);
		return new ResponseEntity<Object>(qzSettingService.searchQZSettings(page, uuid, customerUuid, domain, group, updateStatus), HttpStatus.OK);
	}

	//通过QZSettinguuid去查询
	@RequestMapping(value = "/getQZSetting/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> findQZSettings(@PathVariable("uuid") Long uuid){
		Page<QZSetting> page = new Page<QZSetting>();
		return new ResponseEntity<Object>(qzSettingService.searchQZSettings(page, uuid, null, null, null, null), HttpStatus.OK);
	}
}
