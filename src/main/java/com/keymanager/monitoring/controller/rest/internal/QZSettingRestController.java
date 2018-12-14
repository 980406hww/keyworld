package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.dao.QZKeywordRankInfoDao;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.service.*;
import com.keymanager.util.Constants;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
	private CustomerService customerService;

	@Autowired
	private QZKeywordRankInfoDao qzKeywordRankInfoDao;

	@RequiresPermissions("/internal/qzsetting/updateStatus")
	@RequestMapping(value = "/updateQZSettingStatus", method = RequestMethod.POST)
	public ResponseEntity<?> updateQZSettingStatus(@RequestBody Map<String, Object> requestMap) throws Exception{
		List<Long> uuids = (List<Long>) requestMap.get("uuids");
		Integer status = (Integer) requestMap.get("status");
		try {
			qzSettingService.updateQZSettingStatus(uuids, status);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch(Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(false, HttpStatus.OK);
	}

	@RequiresPermissions("/internal/qzsetting/updateImmediately")
	@RequestMapping(value = "/updateImmediately", method = RequestMethod.POST)
	public ResponseEntity<?> updateImmediately(@RequestBody Map<String, Object> requestMap) throws Exception{
		String uuids = (String) requestMap.get("uuids");
		boolean returnValue = false;
		try {
			qzSettingService.updateImmediately(uuids);
			returnValue = true;
		}catch(Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
	}

	@RequiresPermissions("/internal/qzsetting/save")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> saveQZSetting(@RequestBody QZSetting qzSetting){
		try {
			if(qzSetting.getUuid() == null) {
				Set<String> roles = getCurrentUser().getRoles();
				if(roles.contains("DepartmentManager")) {
					qzSetting.setStatus(1);
				} else {
					qzSetting.setStatus(2);
				}
			}
			qzSettingService.saveQZSetting(qzSetting);
			return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/qzsetting/delete")
	@RequestMapping(value ="/delete/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteQZSetting(@PathVariable("uuid") Long uuid){
		try {
			qzSettingService.deleteOne(uuid);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/qzsetting/deleteQZSettings")
	@RequestMapping(value = "/deleteQZSettings", method = RequestMethod.POST)
	public ResponseEntity<?> deleteQZSettings(@RequestBody Map<String, Object> requestMap){
		try {
			List<String> uuids = (List<String>) requestMap.get("uuids");
			qzSettingService.deleteAll(uuids);
			return new ResponseEntity<Object>(true , HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getQZSetting/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> findQZSettings(@PathVariable("uuid") Long uuid){
		return new ResponseEntity<Object>(qzSettingService.getQZSetting(uuid), HttpStatus.OK);
	}

	@RequiresPermissions("/internal/qzsetting/searchQZSettings")
	@RequestMapping(value = "/searchQZSettings", method = RequestMethod.GET)
	public ModelAndView searchQZSettingsGet(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
		return constructQZSettingModelAndView(request, new QZSettingSearchCriteria(), currentPageNumber, pageSize);
	}

	@RequiresPermissions("/internal/qzsetting/searchQZSettings")
	@RequestMapping(value = "/searchQZSettings", method = RequestMethod.POST)
	public ModelAndView searchQZSettingsPost(HttpServletRequest request, QZSettingSearchCriteria qzSettingSearchCriteria) {
		String currentPageNumber = request.getParameter("currentPageNumber");
		String pageSize = request.getParameter("pageSize");
		if (null == currentPageNumber && null == pageSize) {
			currentPageNumber = "1";
			pageSize = "50";
		}
		return constructQZSettingModelAndView(request, qzSettingSearchCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
	}

	private ModelAndView constructQZSettingModelAndView(HttpServletRequest request, QZSettingSearchCriteria qzSettingSearchCriteria, int currentPageNumber, int pageSize) {
		ModelAndView modelAndView = new ModelAndView("/qzsetting/list");
		Map<String, Integer> chargeRemindDataMap = qzSettingService.getChargeRemindData();
        QZSettingSearchCriteria qzSettingCriteria = qzKeywordRankInfoDao.getCountDownAndUp();
        qzSettingSearchCriteria.setDownNum(qzSettingCriteria.getDownNum());
        qzSettingSearchCriteria.setUpNum(qzSettingCriteria.getUpNum());
		CustomerCriteria customerCriteria = new CustomerCriteria();
		String entryType = (String) request.getSession().getAttribute("entryType");
		customerCriteria.setEntryType(entryType);
		boolean isDepartmentManager = true;
		Set<String> roles = getCurrentUser().getRoles();
		if(!roles.contains("DepartmentManager")) {
			isDepartmentManager = false;
			String loginName = (String) request.getSession().getAttribute("username");
			customerCriteria.setLoginName(loginName);
			qzSettingSearchCriteria.setLoginName(loginName);
		}
		Page<QZSetting> page;
		if (null != qzSettingSearchCriteria.getIncreaseType()) {
			page = qzSettingService.searchRiseOrFallQZSetting(new Page<QZSetting>(currentPageNumber, pageSize), qzSettingSearchCriteria);
		} else {
			page = qzSettingService.searchQZSetting(new Page<QZSetting>(currentPageNumber, pageSize), qzSettingSearchCriteria);
		}
		List<Customer> customerList = customerService.getActiveCustomerSimpleInfo(customerCriteria);
		Integer availableQZSettingCount = qzSettingService.getAvailableQZSettings().size();
		modelAndView.addObject("chargeRemindDataMap", chargeRemindDataMap);
		modelAndView.addObject("customerList", customerList);
		modelAndView.addObject("qzSettingSearchCriteria", qzSettingSearchCriteria);
		modelAndView.addObject("statusList", Constants.QZSETTING_STATUS_LIST);
		modelAndView.addObject("page", page);
		modelAndView.addObject("isDepartmentManager", isDepartmentManager);
		modelAndView.addObject("availableQZSettingCount", availableQZSettingCount);
		return modelAndView;
	}

	@RequiresPermissions("/internal/qzsetting/searchQZSettings")
	@RequestMapping(value = "/getAvailableQZSettings", method = RequestMethod.POST)
	public ResponseEntity<?> getAvailableQZSettings(HttpServletRequest request){
		try {
			List<QZSetting>	qzSettings = qzSettingService.getAvailableQZSettings();
			return new ResponseEntity<Object>(qzSettings,HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
	}
}
