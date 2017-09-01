package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.service.*;
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

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> saveQZSetting(@RequestBody QZSetting qzSetting){
		try {
			qzSettingService.saveQZSetting(qzSetting);
			return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
		}
	}

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

	@RequestMapping(value = "/searchQZSettings", method = RequestMethod.GET)
	public ModelAndView searchQZSettingsGet(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
		return constructQZSettingModelAndView(new QZSettingSearchCriteria(), currentPageNumber, pageSize);
	}

	@RequestMapping(value = "/searchQZSettings", method = RequestMethod.POST)
	public ModelAndView searchQZSettingsPost(HttpServletRequest request, QZSettingSearchCriteria qzSettingSearchCriteria) {
		String currentPageNumber = request.getParameter("currentPageNumber");
		String pageSize = request.getParameter("pageSize");
		String dateRangeType = request.getParameter("dateRangeType");
		qzSettingSearchCriteria.setDateRangeType(dateRangeType);
		String contactPerson = qzSettingSearchCriteria.getContactPerson();
		if(contactPerson.contains("_")) {
			qzSettingSearchCriteria.setContactPerson(contactPerson.substring(0,contactPerson.indexOf("_")));
		}
		if (null == currentPageNumber && null == pageSize) {
			currentPageNumber = "1";
			pageSize = "50";
		}
		return constructQZSettingModelAndView(qzSettingSearchCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
	}

	private ModelAndView constructQZSettingModelAndView(QZSettingSearchCriteria qzSettingSearchCriteria, int currentPageNumber, int pageSize) {
		ModelAndView modelAndView = new ModelAndView("/qzsetting/list");
		Map<String, Integer> dateRangeTypeMap = qzSettingService.getDateRangeType();
		Page<QZSetting> page = qzSettingService.searchQZSetting(new Page<QZSetting>(currentPageNumber, pageSize), qzSettingSearchCriteria);
		List<Customer> customerList = customerService.getActiveCustomerSimpleInfo();

		List<String> statusList = new ArrayList<String>();
		statusList.add("");
		statusList.add("Processing");
		statusList.add("Completed");
		statusList.add("DownloadTimesUsed");

		modelAndView.addObject("dateRangeTypeMap", dateRangeTypeMap);
		modelAndView.addObject("customerList", customerList);
		modelAndView.addObject("qzSettingSearchCriteria", qzSettingSearchCriteria);
		modelAndView.addObject("statusList", statusList);
		modelAndView.addObject("page", page);
		return modelAndView;
	}
}
