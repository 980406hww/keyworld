package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerExcludeKeyword;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.service.*;
import com.keymanager.monitoring.vo.QZSettingSearchGroupInfoVO;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
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
	private CustomerService customerService;

	@Autowired
	private QZKeywordRankInfoService qzKeywordRankInfoService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private OperationCombineService operationCombineService;

	@Autowired
	private OperationTypeService operationTypeService;

	@Autowired QZCategoryTagService qzCategoryTagService;

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
	public ResponseEntity<?> saveQZSetting(@RequestBody QZSetting qzSetting, HttpServletRequest request){
		try {
			if(qzSetting.getUuid() == null) {
				Set<String> roles = getCurrentUser().getRoles();
				if(roles.contains("DepartmentManager")) {
					qzSetting.setStatus(1);
				} else {
					qzSetting.setStatus(2);
				}
			}
			String userName = (String) request.getSession().getAttribute("username");
			qzSettingService.saveQZSetting(qzSetting, userName);
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

	@RequestMapping(value = "/getChargeRule", method = RequestMethod.POST)
    public ResponseEntity<?> getChargeRules(@RequestBody QZSettingSearchChargeRuleCriteria qzSettingSearchChargeRuleCriteria) {
	    try{
            return new ResponseEntity<Object>(qzChargeRuleService.searchChargeRules(qzSettingSearchChargeRuleCriteria), HttpStatus.OK);
        } catch (Exception e) {
	        logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
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
		if ((null == currentPageNumber && null == pageSize) || qzSettingSearchCriteria.getResetPagingParam()) {
			currentPageNumber = "1";
			pageSize = "50";
		}
		return constructQZSettingModelAndView(request, qzSettingSearchCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
	}

	private ModelAndView constructQZSettingModelAndView(HttpServletRequest request, QZSettingSearchCriteria qzSettingSearchCriteria, int currentPageNumber, int pageSize) {
		ModelAndView modelAndView = new ModelAndView("/qzsetting/list");
		// Map<String, Integer> chargeRemindDataMap = qzSettingService.getChargeRemindData();
		if (null == qzSettingSearchCriteria.getTerminalType()) {
            qzSettingSearchCriteria.setTerminalType(TerminalTypeMapping.getTerminalType(request));
        }
		if (null == qzSettingSearchCriteria.getSearchEngine()) {
			qzSettingSearchCriteria.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
		}
		CustomerCriteria customerCriteria = new CustomerCriteria();
		String entryType = (String) request.getSession().getAttribute("entryType");
		customerCriteria.setEntryType(entryType);
		boolean isSEO = false;
		boolean hasFilterUserName = false;
		Set<String> roles = getCurrentUser().getRoles();
		if(!roles.contains("DepartmentManager")) {
			if (roles.contains("Operation") || roles.contains("Technical")) {
                hasFilterUserName = true;
			} else {
				String loginName = (String) request.getSession().getAttribute("username");
				customerCriteria.setLoginName(loginName);
				qzSettingSearchCriteria.setLoginName(loginName);
			}
		} else {
            hasFilterUserName = true;
		}
		if (hasFilterUserName) {
			if (qzSettingSearchCriteria.getUserInfoID() != null) {
                UserInfo userInfo = userInfoService.selectById(qzSettingSearchCriteria.getUserInfoID());
                customerCriteria.setLoginName(userInfo.getLoginName());
				qzSettingSearchCriteria.setLoginName(userInfo.getLoginName());
			}
		}
		if (roles.contains("SEO")) {
			isSEO = true;
		}
		qzKeywordRankInfoService.getCountNumOfRankInfo(qzSettingSearchCriteria);
		Page<QZSetting> page = qzSettingService.searchQZSetting(new Page<QZSetting>(currentPageNumber, pageSize), qzSettingSearchCriteria);
		Map<String, String> existSearchEngineMap = qzSettingService.searchQZSettingSearchEngineMap(qzSettingSearchCriteria, page.getRecords().size());
		List<Customer> customerList = customerService.getActiveCustomerSimpleInfo(customerCriteria);
		List<String> tagNameList = qzCategoryTagService.findTagNames(null);
		Integer availableQZSettingCount = qzSettingService.getAvailableQZSettings().size();
		List operationTypeValues =  operationTypeService.getOperationTypeValuesByRole(qzSettingSearchCriteria.getTerminalType());
		List<String> operationCombines = operationCombineService.getOperationCombineNames(qzSettingSearchCriteria.getTerminalType());
		// modelAndView.addObject("chargeRemindDataMap", chargeRemindDataMap);
		modelAndView.addObject("existSearchEngineMap", existSearchEngineMap);
		modelAndView.addObject("searchEngineMap", Constants.SEARCH_ENGINE_MAP);
		modelAndView.addObject("customerList", customerList);
		modelAndView.addObject("tagNameList", tagNameList);
		modelAndView.addObject("qzSettingSearchCriteria", qzSettingSearchCriteria);
		modelAndView.addObject("statusList", Constants.QZSETTING_STATUS_LIST);
		modelAndView.addObject("page", page);
		modelAndView.addObject("isSEO", isSEO);
		modelAndView.addObject("availableQZSettingCount", availableQZSettingCount);
		modelAndView.addObject("operationTypeValues", operationTypeValues);
		modelAndView.addObject("searchEngineMap", configService.getSearchEngineMap(qzSettingSearchCriteria.getTerminalType()));
		modelAndView.addObject("standardSpeciesMap", Constants.QZ_RANK_STANDARD_SPECIES_MAP);
		modelAndView.addObject("optimizationTypeMap", Constants.QZ_OPERATION_OPTIMIZATION_TYPE_MAP);
		modelAndView.addObject("operationCombines", operationCombines);
		return modelAndView;
	}

	@RequiresPermissions("/internal/qzsetting/searchQZSettings")
	@RequestMapping(value = "/getAvailableQZSettings", method = RequestMethod.POST)
	public ResponseEntity<?> getAvailableQZSettings(HttpServletRequest request){
		try {
			List<QZSetting>	qzSettings = qzSettingService.getAvailableQZSettings();
			return new ResponseEntity<Object>(qzSettings, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/qzsetting/searchQZSettings")
	@RequestMapping(value = "/getQZSettingGroupInfo", method = RequestMethod.POST)
	public ResponseEntity<?> getQZSettingGroupInfo(@RequestBody QZSettingSearchGroupInfoCriteria qzSettingSearchGroupInfoCriteria) {
		try {
			QZSettingSearchGroupInfoVO qzSettingSearchGroupInfoVo = qzSettingService.getQZSettingGroupInfo(qzSettingSearchGroupInfoCriteria);
			return new ResponseEntity<Object>(qzSettingSearchGroupInfoVo, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "saveQZSettingCustomerKeywords", method = RequestMethod.POST)
	public ResponseEntity<?> saveQZSettingCustomerKeywords(HttpServletRequest request, @RequestBody QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria) {
		try {
			String userName = (String) request.getSession().getAttribute("username");
			qzSettingService.saveQZSettingCustomerKeywords(qzSettingSaveCustomerKeywordsCriteria, userName);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "excludeQZSettingCustomerKeywords", method = RequestMethod.POST)
	public ResponseEntity<?> excludeQZSettingCustomerKeywords(HttpServletRequest request, @RequestBody QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
		try {
			String entryType = (String) request.getSession().getAttribute("entryType");
            qzSettingExcludeCustomerKeywordsCriteria.setType(entryType);
			qzSettingService.excludeQZSettingCustomerKeywords(qzSettingExcludeCustomerKeywordsCriteria);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

    @RequestMapping(value = "echoExcludeKeyword", method = RequestMethod.POST)
    public ResponseEntity<?> echoExcludeKeyword(HttpServletRequest request, @RequestBody QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        try {
            CustomerExcludeKeyword customerExcludeKeyword = qzSettingService.echoExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria);
            return new ResponseEntity<Object>(customerExcludeKeyword, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/qzsetting/startMonitorImmediately")
    @RequestMapping(value = "/startMonitorImmediately", method = RequestMethod.POST)
    public ResponseEntity<?> startMonitorImmediately(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        String uuids = (String) requestMap.get("uuids");
		String userName = (String) request.getSession().getAttribute("username");
        boolean returnValue = false;
        try {
            qzSettingService.startMonitorImmediately(uuids, userName);
            returnValue = true;
        } catch(Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
    }

    @RequiresPermissions("/internal/qzsetting/updateQZKeywordEffectImmediately")
    @RequestMapping(value = "/updateQZKeywordEffectImmediately", method = RequestMethod.POST)
    public ResponseEntity<?> updateQZKeywordEffectImmediately (@RequestBody Map<String, Object> requestMap) {
		String uuids = (String) requestMap.get("uuids");
		try {
			qzSettingService.updateQZKeywordEffectImmediately(uuids);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}
}
