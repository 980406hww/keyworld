package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.*;
import com.keymanager.monitoring.vo.QZSettingVO;
import com.keymanager.util.PortTerminalTypeMapping;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
	private CustomerService customerService;

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
		Integer pageList = (Integer) requestMap.get("pageList");
		Page<QZSetting> page = new Page<QZSetting>(pageNumber, pageList);
		return new ResponseEntity<Object>(qzSettingService.searchQZSettings(page, uuid, customerUuid, domain, group, updateStatus), HttpStatus.OK);
	}

	//通过QZSettinguuid去查询
	@RequestMapping(value = "/getQZSetting/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> findQZSettings(@PathVariable("uuid") Long uuid){
		Page<QZSetting> page = new Page<QZSetting>();
		return new ResponseEntity<Object>(qzSettingService.searchQZSettings(page, uuid, null, null, null, null), HttpStatus.OK);
	}

	@RequestMapping(value = "/searchQZSettingPage", method = RequestMethod.GET)
	public ModelAndView findQZSettingPage(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "50") int displaysRecords, @RequestParam(defaultValue = "1") int chargeDays, HttpServletRequest request) {
		return constructQZSettingModelAndView(new QZSettingVO(), currentPage + "", displaysRecords + "", chargeDays + "");
	}

	@RequestMapping(value = "/searchQZSettingPage", method = RequestMethod.POST)
	public ModelAndView findQZSettingPagePost(HttpServletRequest request, QZSettingVO qzSettingVO) {
		String chargeDays = request.getParameter("chargeDays");
		String currentPage = request.getParameter("currentPageHidden");
		String displaysRecords = request.getParameter("displayRerondsHidden");
		if (null == currentPage && null == currentPage) {
			currentPage = "1";
			displaysRecords = "50";
		}
		return constructQZSettingModelAndView(qzSettingVO, currentPage, displaysRecords, chargeDays);
	}

	private ModelAndView constructQZSettingModelAndView(QZSettingVO qzSettingVO, String currentPage, String displaysRecords, String chargeDays) {
		ModelAndView modelAndView = new ModelAndView("/qzsetting/list");
		Page<QZSetting> page = qzSettingService.searchQZSettingPage(new Page<QZSetting>(Integer.parseInt(currentPage), Integer.parseInt(displaysRecords)), qzSettingVO);
		List<QZSetting> expiredChargeList = qzOperationTypeService.expiredCharge();
		List<QZSetting> nowChargeList = qzOperationTypeService.nowCharge();
		List<QZSetting> threeChargeList = qzOperationTypeService.threeCharge();
		List<QZSetting> sevenChargeList = qzOperationTypeService.sevenCharge();
		List<Customer> customerList = customerService.findCustomers();

		int days = Integer.parseInt(chargeDays);
		if(days == -1) {
			page.setRecords(expiredChargeList);
			page.setTotal(expiredChargeList.size());
		} else if (days == 0) {
			page.setRecords(nowChargeList);
			page.setTotal(nowChargeList.size());
		} else if (days == 3) {
			page.setRecords(threeChargeList);
			page.setTotal(threeChargeList.size());
		} else if (days == 7) {
			page.setRecords(sevenChargeList);
			page.setTotal(sevenChargeList.size());
		}

		if(days != 1) {
			page.setSize(Integer.parseInt(displaysRecords));
			page.setCurrent(Integer.parseInt(currentPage));
		}

		List<String> statusList = new ArrayList<String>();
		statusList.add("");
		statusList.add("Processing");
		statusList.add("Completed");
		statusList.add("DownloadTimesUsed");

		modelAndView.addObject("expiredChargeList", expiredChargeList);
		modelAndView.addObject("nowChargeList", nowChargeList);
		modelAndView.addObject("threeChargeList", threeChargeList);
		modelAndView.addObject("sevenChargeList", sevenChargeList);
		modelAndView.addObject("customerList", customerList);
		modelAndView.addObject("qzSettingVO", qzSettingVO);
		modelAndView.addObject("statusList", statusList);
		modelAndView.addObject("chargeDays", chargeDays);
		modelAndView.addObject("page", page);
		return modelAndView;
	}
}
