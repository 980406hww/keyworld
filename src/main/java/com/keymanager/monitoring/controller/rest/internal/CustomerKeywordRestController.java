package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.EntryTypeEnum;
import com.keymanager.monitoring.excel.operator.CustomerKeywordInfoExcelWriter;
import com.keymanager.monitoring.service.*;
import com.keymanager.monitoring.vo.CodeNameVo;
import com.keymanager.monitoring.vo.KeywordStatusBatchUpdateVO;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping(value = "/internal/customerKeyword")
public class CustomerKeywordRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(CustomerKeywordRestController.class);

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ServiceProviderService serviceProviderService;

	@Autowired
	private PerformanceService performanceService;

	@Autowired
    private CustomerExcludeKeywordService customerExcludeKeywordService;

	@RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
	@RequestMapping(value="/searchCustomerKeywords/{customerUuid}" , method=RequestMethod.GET)
	public ModelAndView searchCustomerKeywords(@PathVariable("customerUuid") Long customerUuid,@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request){
		CustomerKeywordCriteria customerKeywordCriteria = new CustomerKeywordCriteria();
		customerKeywordCriteria.setStatus("1");
		customerKeywordCriteria.setCustomerUuid(customerUuid);
		return constructCustomerKeywordModelAndView(request, customerKeywordCriteria, currentPageNumber, pageSize);
	}

	@RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
	@RequestMapping(value = "/searchCustomerKeywords", method = RequestMethod.POST)
	public ModelAndView searchCustomerKeywords(CustomerKeywordCriteria customerKeywordCriteria, HttpServletRequest request) {
		try {
			String currentPageNumber = request.getParameter("currentPageNumber");
			String pageSize = request.getParameter("pageSize");
			if (null == currentPageNumber && null == pageSize) {
				currentPageNumber = "1";
				pageSize = "30";
			}
			return constructCustomerKeywordModelAndView(request, customerKeywordCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private ModelAndView constructCustomerKeywordModelAndView(HttpServletRequest request, CustomerKeywordCriteria customerKeywordCriteria, int currentPage, int pageSize) {
		long startMilleSeconds = System.currentTimeMillis();
		HttpSession session = request.getSession();
		ModelAndView modelAndView = new ModelAndView("/customerkeyword/customerKeywordList");
		String loginName = (String) session.getAttribute("username");
		UserInfo user = userInfoService.getUserInfo(loginName);
		String entryType = (String) session.getAttribute("entryType");
		String terminalType = TerminalTypeMapping.getTerminalType(request);
		Customer customer = customerService.getCustomerWithKeywordCount(terminalType, entryType, customerKeywordCriteria.getCustomerUuid(), loginName);
		String orderElement = request.getParameter("orderingElement");
		initOrderElemnet(orderElement, customerKeywordCriteria);
		customerKeywordCriteria.setEntryType(entryType);
		customerKeywordCriteria.setTerminalType(terminalType);
		List<ServiceProvider> serviceProviders = serviceProviderService.searchServiceProviders();
		Page<CustomerKeyword> page = customerKeywordService.searchCustomerKeywords(new Page<CustomerKeyword>(currentPage, pageSize), customerKeywordCriteria);
		String  accountName=null;
		Set<String> roles = getCurrentUser().getRoles();
		if(!roles.contains("DepartmentManager")) {
			accountName=loginName;
		}
		List<Customer> customerList=customerService.searchTargetCustomers(entryType,accountName);
		modelAndView.addObject("customerList", customerList);
		modelAndView.addObject("customerKeywordCriteria", customerKeywordCriteria);
		modelAndView.addObject("page", page);
		modelAndView.addObject("user", user);
		modelAndView.addObject("customer", customer);
		modelAndView.addObject("serviceProviders",serviceProviders);
		modelAndView.addObject("orderElement",orderElement);
		performanceService.addPerformanceLog(terminalType + ":searchCustomerKeywords", System.currentTimeMillis() - startMilleSeconds, null);
		return modelAndView;
	}

	private void initOrderElemnet(String orderElement, CustomerKeywordCriteria customerKeywordCriteria){
		if(StringUtils.isNotEmpty(orderElement)){
			switch (orderElement.charAt(0)){
				case '0':
					customerKeywordCriteria.setOrderingElement("");break;
				case '1':
					customerKeywordCriteria.setOrderingElement("fCreateTime");break;
				case '2':
					customerKeywordCriteria.setOrderingElement("fCurrentPosition");break;
				case '3':
					customerKeywordCriteria.setOrderingElement("fSequence");break;
			}
		}else{
			orderElement = "0";
		}
	}

	@RequiresPermissions("/internal/customerKeyword/saveCustomerKeywords")
	@RequestMapping(value = "/saveCustomerKeywords", method = RequestMethod.POST)
	public ResponseEntity<?> saveCustomerKeywords(@RequestBody List<CustomerKeyword> customerKeywords, HttpServletRequest request) {
		try{
			String userName = (String) request.getSession().getAttribute("username");
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String entryType = (String)request.getSession().getAttribute("entryType");
			customerKeywordService.addCustomerKeywordsFromSimpleUI(customerKeywords, terminalType, entryType, userName);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	//重构部分
	//通过排名修改分组
	@RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordGroupNameByRank")
	@RequestMapping(value = "/updateCustomerKeywordGroupNameByRank", method = RequestMethod.POST)
	public ResponseEntity<?> updateCustomerKeywordGroupNameByRank(@RequestBody Map<String,Object> resultMap, HttpServletRequest request) {
		try {
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String entryType = (String)request.getSession().getAttribute("entryType");
			resultMap.put("terminalType",terminalType);
			resultMap.put("entryType",entryType);
			customerKeywordService.updateCustomerKeywordGroupNameByRank(resultMap);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}
	//修改该用户关键字组名
	@RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordGroupName")
	@RequestMapping(value = "/updateCustomerKeywordGroupName", method = RequestMethod.POST)
	public ResponseEntity<?> updateCustomerKeywordGroupName(@RequestBody CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria, HttpServletRequest request) {
		try {
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String entryType = (String)request.getSession().getAttribute("entryType");
			customerKeywordUpdateCriteria.setTerminalType(terminalType);
			customerKeywordUpdateCriteria.setEntryType(entryType);
			customerKeywordService.updateCustomerKeywordGroupName(customerKeywordUpdateCriteria);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordSearchEngine")
	@RequestMapping(value = "/updateCustomerKeywordSearchEngine", method = RequestMethod.POST)
	public ResponseEntity<?> updateCustomerKeywordSearchEngine(@RequestBody CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria, HttpServletRequest request) {
		try {
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String entryType = (String)request.getSession().getAttribute("entryType");
			customerKeywordUpdateCriteria.setTerminalType(terminalType);
			customerKeywordUpdateCriteria.setEntryType(entryType);
			customerKeywordService.updateCustomerKeywordSearchEngine(customerKeywordUpdateCriteria);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	//关键字Excel上传(简化版)
	@RequiresPermissions("/internal/customerKeyword/uploadCustomerKeywords")
	@RequestMapping(value = "/uploadCustomerKeywords" , method = RequestMethod.POST)
	public boolean uploadCustomerKeywords(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
		String customerUuid = request.getParameter("customerUuid");
		String excelType = request.getParameter("excelType");
		String entry = (String)request.getSession().getAttribute("entryType");
		String terminalType = TerminalTypeMapping.getTerminalType(request);
		String userName = (String) request.getSession().getAttribute("username");
		try{
			boolean uploaded = customerKeywordService.handleExcel(file.getInputStream(), excelType, Integer.parseInt(customerUuid),  entry, terminalType, userName);
			if (uploaded){
				return true;
			};
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return false;
	}

	@RequiresPermissions("/internal/customerKeyword/deleteCustomerKeyword")
	@RequestMapping(value = "/deleteCustomerKeyword/{customerKeywordUuid}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteCustomerKeyword(@PathVariable("customerKeywordUuid") Long customerKeywordUuid , HttpServletRequest request) {
		try {
			customerKeywordService.deleteById(customerKeywordUuid);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/deleteCustomerKeywords")
	@RequestMapping(value = "/deleteCustomerKeywords" , method = RequestMethod.POST)
	public ResponseEntity<?> deleteCustomerKeywords(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
		try {
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String entryType = (String) request.getSession().getAttribute("entryType");
			String deleteType = (String) requestMap.get("deleteType");
			String customerUuid = (String) requestMap.get("customerUuid");
			List<String> customerKeywordUuids = (List<String>) requestMap.get("uuids");
			customerKeywordService.deleteCustomerKeywords(deleteType, terminalType, entryType, customerUuid, customerKeywordUuids);
			return new ResponseEntity<Object>(true , HttpStatus.OK);
		}catch (Exception e){
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
		}
	}

	//重采标题
	@RequiresPermissions("/internal/customerKeyword/cleanTitle")
	@RequestMapping(value = "/cleanTitle", method = RequestMethod.POST)
	public ResponseEntity<?> cleanTitle(@RequestBody CustomerKeywordCleanCriteria customerKeywordCleanCriteria, HttpServletRequest request) {
		try{
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String entryType = (String)request.getSession().getAttribute("entryType");

			customerKeywordCleanCriteria.setTerminalType(terminalType);
			customerKeywordCleanCriteria.setEntryType(entryType);

			customerKeywordService.cleanTitle(customerKeywordCleanCriteria);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
	@RequestMapping(value = "/saveCustomerKeyword", method = RequestMethod.POST)
	public ResponseEntity<?> saveCustomerKeyword(@RequestBody CustomerKeyword customerKeyword, HttpServletRequest request) {
		try{
			String userName = (String) request.getSession().getAttribute("username");
			if (customerKeyword.getUuid() == null) {
				String terminalType = TerminalTypeMapping.getTerminalType(request);
				String entryType = (String) request.getSession().getAttribute("entryType");
				customerKeyword.setTerminalType(terminalType);
				customerKeyword.setType(entryType);
				String customerExcludeKeywords = customerExcludeKeywordService.getCustomerExcludeKeyword(customerKeyword.getCustomerUuid(), customerKeyword.getQzSettingUuid(), customerKeyword.getTerminalType(), customerKeyword.getUrl());
				if (null != customerExcludeKeywords) {
					Set excludeKeyword = new HashSet();
					excludeKeyword.addAll(Arrays.asList(customerExcludeKeywords.split(",")));
					if (!excludeKeyword.isEmpty()){
						if (excludeKeyword.contains(customerKeyword.getKeyword())){
							customerKeyword.setOptimizeGroupName("zanting");
						}
					}
				}
				customerKeywordService.addCustomerKeyword(customerKeyword, userName);
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			} else {
				customerKeywordService.updateCustomerKeywordFromUI(customerKeyword, userName);
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getCustomerKeywordByCustomerKeywordUuid/{customerKeywordUuid}" , method = RequestMethod.POST)
	public ResponseEntity<?> getCustomerKeywordByUuid(@PathVariable("customerKeywordUuid")Long customerKeywordUuid){
		CustomerKeyword customerKeyword = customerKeywordService.getCustomerKeyword(customerKeywordUuid);
		return new ResponseEntity<Object>(customerKeyword, HttpStatus.OK);
	}

	//导出成Excel文件
	@RequiresPermissions("/internal/customerKeyword/downloadCustomerKeywordInfo")
	@RequestMapping(value = "/downloadCustomerKeywordInfo", method = RequestMethod.POST)
	public ResponseEntity<?> downloadCustomerKeywordInfo( HttpServletRequest request,
														 HttpServletResponse response,CustomerKeywordCriteria customerKeywordCriteria) {
		try {
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String entryType = (String) request.getSession().getAttribute("entryType");
			customerKeywordCriteria.setTerminalType(terminalType);
			customerKeywordCriteria.setEntryType(entryType);
			initOrderElemnet(customerKeywordCriteria.getOrderingElement(), customerKeywordCriteria);
			List<CustomerKeyword> customerKeywords = customerKeywordService.searchCustomerKeywordInfo(customerKeywordCriteria);
			if (!Utils.isEmpty(customerKeywords)) {
				CustomerKeywordInfoExcelWriter excelWriter = new CustomerKeywordInfoExcelWriter();
				excelWriter.writeDataToExcel(customerKeywords);
				Customer customer = customerService.selectById(customerKeywordCriteria.getCustomerUuid());
				String fileName = customer.getContactPerson() + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd") + ".xls";
				fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
				byte[] buffer = excelWriter.getExcelContentBytes();
				downExcelFile(response, fileName, buffer);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false,HttpStatus.OK);
		}
		return new ResponseEntity<Object>(true,HttpStatus.OK);
	}

	@RequestMapping(value = "/haveCustomerKeywordForOptimization", method = RequestMethod.POST)
	public ResponseEntity<?> haveCustomerKeywordForOptimization(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception{
		List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
		String terminalType = TerminalTypeMapping.getTerminalType(request);
		try {
			Map<String,Boolean> resultMap = new HashMap<String, Boolean>();
			for(String clientID : clientIDs){
				boolean haveCustomerKeywordForOptimization = customerKeywordService.haveCustomerKeywordForOptimization(terminalType, clientID);
				resultMap.put(clientID,haveCustomerKeywordForOptimization);
			}
			return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
		}catch(Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/resetInvalidRefreshCount")
	@RequestMapping(value = "/resetInvalidRefreshCount", method = RequestMethod.POST)
	public ResponseEntity<?> resetInvalidRefreshCount(@RequestBody CustomerKeywordRefreshStatInfoCriteria criteria, HttpServletRequest request) {
		try {
			String entryType = (String)request.getSession().getAttribute("entryType");
			criteria.setEntryType(entryType);
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			criteria.setTerminalType(terminalType);
			customerKeywordService.resetInvalidRefreshCount(criteria);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/searchCustomerKeywordLists")
	@RequestMapping(value="/searchCustomerKeywordLists" , method= RequestMethod.GET)
	public ModelAndView searchCustomerKeywordLists(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request){
		String entryType = (String)request.getSession().getAttribute("entryType");
		if(EntryTypeEnum.bc.name().equalsIgnoreCase(entryType) && !SecurityUtils.getSubject().hasRole("BCSpecial")){
			SecurityUtils.getSubject().logout();
		}
		CustomerKeywordCriteria customerKeywordCriteria = new CustomerKeywordCriteria();
		customerKeywordCriteria.setStatus("1");
		return constructCustomerKeywordListsModelAndView(request, customerKeywordCriteria, currentPageNumber, pageSize);
	}

	@RequiresPermissions("/internal/customerKeyword/searchCustomerKeywordLists")
	@RequestMapping(value = "/searchCustomerKeywordLists", method = RequestMethod.POST)
	public ModelAndView searchCustomerKeywordLists(CustomerKeywordCriteria customerKeywordCriteria, HttpServletRequest request) {
		try {
			String currentPageNumber = request.getParameter("currentPageNumber");
			String pageSize = request.getParameter("pageSize");
			if(StringUtils.isEmpty(currentPageNumber)){
				currentPageNumber = "1";
			}
			if(StringUtils.isEmpty(pageSize)){
				pageSize = "50";
			}
			return constructCustomerKeywordListsModelAndView(request, customerKeywordCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ModelAndView("/customerkeyword/keywordfinderList");
		}
	}

	private ModelAndView constructCustomerKeywordListsModelAndView(HttpServletRequest request, CustomerKeywordCriteria customerKeywordCriteria, int currentPage, int pageSize) {
		long startMilleSeconds = System.currentTimeMillis();
		HttpSession session = request.getSession();
		ModelAndView modelAndView = new ModelAndView("/customerkeyword/keywordfinderList");
		String userName = (String) session.getAttribute("username");
		UserInfo user = userInfoService.getUserInfo(userName);
		List<UserInfo> activeUsers = userInfoService.findActiveUsers();
		String entryType = (String) session.getAttribute("entryType");
		String terminalType = TerminalTypeMapping.getTerminalType(request);
		String orderElement = request.getParameter("orderingElement");
		initOrderElemnet(orderElement, customerKeywordCriteria);
		customerKeywordCriteria.setEntryType(entryType);
		customerKeywordCriteria.setTerminalType(terminalType);
		boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
		if(!isDepartmentManager) {
			customerKeywordCriteria.setUserName(userName);
		}
		if(request.getMethod().equals("POST")) {
			Page<CustomerKeyword> page = customerKeywordService.searchCustomerKeywordLists(new Page<CustomerKeyword>(currentPage, pageSize), customerKeywordCriteria);
			modelAndView.addObject("page", page);
		}
		modelAndView.addObject("customerKeywordCriteria", customerKeywordCriteria);
		modelAndView.addObject("user", user);
		modelAndView.addObject("activeUsers", activeUsers);
		modelAndView.addObject("orderElement",orderElement);
		modelAndView.addObject("isDepartmentManager",isDepartmentManager);
		performanceService.addPerformanceLog(terminalType + ":searchCustomerKeywordLists", (System.currentTimeMillis() - startMilleSeconds), null);
		return modelAndView;
	}

	@RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
	@RequestMapping(value = "/updateCustomerKeywordStatus", method = RequestMethod.POST)
	public ResponseEntity<?> updateCustomerKeywordStatus(@RequestBody Map<String, Object> requestMap) {
		try {
			List<Long> customerKeywordUuids = (List<Long>) requestMap.get("uuids");
			Integer status = (Integer) requestMap.get("status");
			customerKeywordService.updateCustomerKeywordStatus(customerKeywordUuids, status);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
	@RequestMapping(value = "/changeCustomerKeywordStatus", method = RequestMethod.POST)
	public ResponseEntity<?> changeCustomerKeywordStatus(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
		try {
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String entryType = (String) request.getSession().getAttribute("entryType");
			String customerUuid = (String) requestMap.get("customerUuid");
			Integer status = (Integer) requestMap.get("status");
 			customerKeywordService.changeCustomerKeywordStatus(terminalType, entryType, Long.parseLong(customerUuid), status);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/searchGroups", method = RequestMethod.POST)
	public List<CodeNameVo> searchGroups() {
		return customerKeywordService.searchGroups();
	}

	@RequiresPermissions("/internal/customerKeyword/editOptimizePlanCount")
	@RequestMapping(value = "/editOptimizePlanCount" , method = RequestMethod.POST)
	public ResponseEntity<?> editOptimizePlanCount(@RequestBody Map<String, Object> requestMap, HttpServletRequest request){
		try {
			List<String> uuids = (List<String>) requestMap.get("uuids");
			String customerUuid = (String) requestMap.get("customerUuid");
			String settingType = (String) requestMap.get("settingType");
			String optimizePlanCount = (String) requestMap.get("optimizePlanCount");
			if(StringUtils.isNotBlank(customerUuid)) {
				String terminalType = TerminalTypeMapping.getTerminalType(request);
				String entryType = (String) request.getSession().getAttribute("entryType");
				customerKeywordService.editOptimizePlanCountByCustomerUuid(terminalType, entryType, Long.parseLong(customerUuid), Integer.parseInt(optimizePlanCount), settingType);
			} else {
				customerKeywordService.editCustomerOptimizePlanCount(Integer.parseInt(optimizePlanCount), settingType, uuids);
			}
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<Object>(false, HttpStatus.OK);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordGroupName")
	@RequestMapping(value = "/updateOptimizeGroupName", method = RequestMethod.POST)
	public ResponseEntity<?> updateOptimizeGroupName(@RequestBody CustomerKeywordCriteria customerKeywordCriteria, HttpServletRequest request) {
		try {
			String entryType = (String) request.getSession().getAttribute("entryType");
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String userName = (String) request.getSession().getAttribute("username");
			boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
			if(!isDepartmentManager) {
				customerKeywordCriteria.setUserName(userName);
			}
			customerKeywordCriteria.setEntryType(entryType);
			customerKeywordCriteria.setTerminalType(terminalType);
			customerKeywordService.updateOptimizeGroupName(customerKeywordCriteria);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
	@RequestMapping(value = "/updateBearPawNumber", method = RequestMethod.POST)
	public ResponseEntity<?> updateBearPawNumber(@RequestBody CustomerKeywordCriteria customerKeywordCriteria, HttpServletRequest request) {
		try {
			String entryType = (String) request.getSession().getAttribute("entryType");
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			String userName = (String) request.getSession().getAttribute("username");
			boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
			if(!isDepartmentManager) {
				customerKeywordCriteria.setUserName(userName);
			}
			customerKeywordCriteria.setEntryType(entryType);
			customerKeywordCriteria.setTerminalType(terminalType);
			customerKeywordService.updateBearPawNumber(customerKeywordCriteria);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/searchCustomerKeywordLists")
	@RequestMapping(value = "/searchCustomerKeywordForNoReachStandard", method = RequestMethod.POST)
	public ResponseEntity<?> searchCustomerKeywordForNoReachStandard(@RequestBody CustomerKeywordCriteria customerKeywordCriteria) {
		try {
			customerKeywordCriteria.setStatus("1");
			customerKeywordService.searchCustomerKeywordForNoReachStandard(customerKeywordCriteria);
			return new ResponseEntity<Object>(customerKeywordCriteria, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(customerKeywordCriteria, HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
	@RequestMapping(value = "/updateKeywordCustomerUuid" , method = RequestMethod.POST)
	public ResponseEntity<?> updateKeywordCustomerUuid(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
		try {
			List<String> keywordUuids = (List<String>)requestMap.get("keywordUuids");
			String customerUuid = (String)requestMap.get("customerUuid");
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			customerKeywordService.updateKeywordCustomerUuid(keywordUuids,customerUuid,terminalType);
			return new ResponseEntity<Object>(true , HttpStatus.OK);
		}catch (Exception e){
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
		}
	}

	@RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
    @RequestMapping(value="batchUpdateKeywordStatus",method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateKeywordStatus(@RequestBody KeywordStatusBatchUpdateVO keywordStatusBatchUpdateVO){
        try {
          customerKeywordService.batchUpdateKeywordStatus(keywordStatusBatchUpdateVO);
          return new ResponseEntity<Object>(true,HttpStatus.OK);
        }catch (Exception e){
        	logger.error(e.getMessage());
        	return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
		}
    }
	@RequiresPermissions("/internal/customerKeyword/deleteCustomerKeywords")
    @RequestMapping(value = "/deleteDuplicateCustomerKeyword/{customerUuid}" , method = RequestMethod.POST)
    public ResponseEntity<?>  deleteDuplicateCustomerKeyword(@PathVariable("customerUuid") Long customerUuid,HttpServletRequest request,HttpSession session) {
	    try{
	        String entryType = EntryTypeEnum.qz.name();
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            customerKeywordService.deleteDuplicateKeywords(customerUuid,terminalType,entryType);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
	}

	@RequiresPermissions("/internal/customerKeyword/deleteCustomerKeywords")
	@RequestMapping(value = "/deleteDuplicateQZKeyword" , method = RequestMethod.POST)
	public ResponseEntity<?>  deleteDuplicateQZKeyword(HttpServletRequest request) {
		try {
			String entryType = EntryTypeEnum.qz.name();
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			customerKeywordService.deleteDuplicateKeywords(null, terminalType, entryType);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}
}
