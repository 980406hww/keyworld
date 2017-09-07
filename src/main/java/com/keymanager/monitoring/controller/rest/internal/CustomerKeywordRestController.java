package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.excel.operator.CustomerKeywordInfoExcelWriter;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CustomerKeywordCleanCriteria;
import com.keymanager.monitoring.criteria.CustomerKeywordCrilteria;
import com.keymanager.monitoring.criteria.CustomerKeywordUpdateGroupCriteria;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.service.*;
import com.keymanager.util.PortTerminalTypeMapping;
import com.keymanager.util.Utils;
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
import java.io.*;
import java.util.*;

@RestController
@RequestMapping(value = "/internal/customerKeyword")
public class CustomerKeywordRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(CustomerKeywordRestController.class);

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ServiceProviderService serviceProviderService;

	@Autowired
	private CustomerKeywordPositionIndexLogService customerKeywordPositionIndexLogService;

	@RequestMapping(value="/searchCustomerKeywords/{customerUuid}" , method=RequestMethod.GET)
	public ModelAndView searchCustomerKeywords(@PathVariable("customerUuid") Long customerUuid,@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request){
		CustomerKeywordCrilteria customerKeywordCrilteria = new CustomerKeywordCrilteria();
		customerKeywordCrilteria.setStatus("1");
		customerKeywordCrilteria.setCustomerUuid(customerUuid);
		return constructCustomerKeywordModelAndView(request, customerKeywordCrilteria, currentPageNumber, pageSize);
	}

	@RequestMapping(value = "/searchCustomerKeywords", method = RequestMethod.POST)
	public ModelAndView searchCustomerKeywords(CustomerKeywordCrilteria customerKeywordCrilteria, HttpServletRequest request) {
		try {
			String currentPageNumber = request.getParameter("currentPageNumber");
			String pageSize = request.getParameter("pageSize");
			if (null == currentPageNumber && null == pageSize) {
				currentPageNumber = "1";
				pageSize = "30";
			}
			return constructCustomerKeywordModelAndView(request, customerKeywordCrilteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private ModelAndView constructCustomerKeywordModelAndView(HttpServletRequest request, CustomerKeywordCrilteria customerKeywordCrilteria, int currentPage, int pageSize) {
		HttpSession session = request.getSession();
		ModelAndView modelAndView = new ModelAndView("/customerkeyword/customerKeywordList");
		String userID = (String) session.getAttribute("username");
		User user = userService.getUser(userID);
		Customer customer = customerService.getCustomerWithKeywordCount(customerKeywordCrilteria.getCustomerUuid());
		String entryType = (String) session.getAttribute("entry");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		customerKeywordCrilteria.setEntryType(entryType);
		customerKeywordCrilteria.setTerminalType(terminalType);
		List<ServiceProvider> serviceProviders = serviceProviderService.searchServiceProviders();
		Page<CustomerKeyword> page = customerKeywordService.searchCustomerKeywords(new Page<CustomerKeyword>(currentPage, pageSize), customerKeywordCrilteria);
		modelAndView.addObject("customerKeywordCrilteria", customerKeywordCrilteria);
		modelAndView.addObject("page", page);
		modelAndView.addObject("user", user);
		modelAndView.addObject("customer", customer);
		modelAndView.addObject("serviceProviders",serviceProviders);
		return modelAndView;
	}

	@RequestMapping(value = "/saveCustomerKeywords", method = RequestMethod.POST)
	public ResponseEntity<?> saveCustomerKeywords(@RequestBody List<CustomerKeyword> customerKeywords, HttpServletRequest request) {
		try{
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			String entryType = (String)request.getSession().getAttribute("entry");
			customerKeywordService.addCustomerKeywordsFromSimpleUI(customerKeywords, terminalType, entryType);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	//重构部分
	//修改该用户关键字组名
	@RequestMapping(value = "/updateCustomerKeywordGroupName", method = RequestMethod.POST)
	public ResponseEntity<?> updateCustomerKeywordGroupName(@RequestBody CustomerKeywordUpdateGroupCriteria customerKeywordUpdateGroupCriteria, HttpServletRequest request) {
		try {
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			String entryType = (String)request.getSession().getAttribute("entry");
			customerKeywordUpdateGroupCriteria.setTerminalType(terminalType);
			customerKeywordUpdateGroupCriteria.setEntryType(entryType);
			customerKeywordService.updateCustomerKeywordGroupName(customerKeywordUpdateGroupCriteria);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	//关键字Excel上传(简化版)
	@RequestMapping(value = "/uploadCustomerKeywords" , method = RequestMethod.POST)
	public boolean uploadCustomerKeywords(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
		String customerUuid = request.getParameter("customerUuid");
		String excelType = request.getParameter("excelType");
		String entry = (String)request.getSession().getAttribute("entry");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		try{
			boolean uploaded = customerKeywordService.handleExcel(file.getInputStream(), excelType, Integer.parseInt(customerUuid),  entry, terminalType);
			if (uploaded){
				return true;
			};
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return false;
	}

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

	@RequestMapping(value = "/deleteCustomerKeywords" , method = RequestMethod.POST)
	public ResponseEntity<?> deleteCustomerKeywords(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
		try {
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			String entryType = (String) request.getSession().getAttribute("entry");
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
	@RequestMapping(value = "/cleanTitle", method = RequestMethod.POST)
	public ResponseEntity<?> cleanTitle(@RequestBody CustomerKeywordCleanCriteria customerKeywordCleanCriteria, HttpServletRequest request) {
		try{
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			String entryType = (String)request.getSession().getAttribute("entry");

			customerKeywordCleanCriteria.setTerminalType(terminalType);
			customerKeywordCleanCriteria.setEntryType(entryType);

			customerKeywordService.cleanTitle(customerKeywordCleanCriteria);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/saveCustomerKeyword", method = RequestMethod.POST)
	public ResponseEntity<?> saveCustomerKeyword(@RequestBody CustomerKeyword customerKeyword, HttpServletRequest request) {
		try{
			if (customerKeyword.getUuid() == null) {
				String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
				String entryType = (String) request.getSession().getAttribute("entry");
				customerKeyword.setTerminalType(terminalType);
				customerKeyword.setType(entryType);
				customerKeyword.setStatus(1);
				customerKeywordService.addCustomerKeyword(customerKeyword);
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			} else {
				customerKeyword.setUpdateTime(new Date());
				customerKeyword.setStatus(1);
				customerKeyword.setPositionForthFee(null);
				customerKeywordService.updateById(customerKeyword);
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
	@RequestMapping(value = "/downloadCustomerKeywordInfo", method = RequestMethod.GET)
	public ResponseEntity<?> downloadCustomerKeywordInfo( HttpServletRequest request,
														 HttpServletResponse response,CustomerKeywordCrilteria customerKeywordCrilteria) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			String customerUuid = request.getParameter("customerUuid").trim();
			List<CustomerKeyword> customerKeywords = appendCondition(request,response);
			if (!Utils.isEmpty(customerKeywords)) {
				CustomerKeywordInfoExcelWriter excelWriter = new CustomerKeywordInfoExcelWriter();
				excelWriter.writeDataToExcel(customerKeywords);
				Customer customer = customerService.selectById(customerUuid);
				String fileName = customer.getContactPerson() + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd") + ".xls";
				fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
				// 以流的形式下载文件。
				byte[] buffer = excelWriter.getExcelContentBytes();
				// 清空response
				response.reset();
				// 设置response的Header
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);//new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
				response.addHeader("Content-Length", "" + buffer.length);
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream");
				toClient.write(buffer);
				toClient.flush();
				toClient.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<Object>(false,HttpStatus.OK);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					return new ResponseEntity<Object>(false,HttpStatus.OK);
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					return new ResponseEntity<Object>(false,HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<Object>(true,HttpStatus.OK);
	}

	public List<CustomerKeyword> appendCondition(HttpServletRequest request, HttpServletResponse response){
		String customerUuid = request.getParameter("customerUuid").trim();
		String invalidRefreshCount = request.getParameter("invalidRefreshCount").trim();
		String keyword = request.getParameter("keyword").trim();
		String url = request.getParameter("url").trim();
		String creationFromTime = request.getParameter("creationFromTime").trim();
		String creationToTime = request.getParameter("creationToTime").trim();
		String status = request.getParameter("status").trim();
		String optimizeGroupName = request.getParameter("optimizeGroupName").trim();
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		String position = request.getParameter("position");
		CustomerKeywordCrilteria customerKeywordCrilteria = new CustomerKeywordCrilteria();
		customerKeywordCrilteria.setTerminalType(terminalType);
		customerKeywordCrilteria.setCustomerUuid(Long.parseLong(customerUuid));
		if (!Utils.isNullOrEmpty(keyword)) {
			customerKeywordCrilteria.setKeyword(keyword);
		}

		if (!Utils.isNullOrEmpty(url)) {
			customerKeywordCrilteria.setUrl(url);
		}

		if (!Utils.isNullOrEmpty(position)) {
			customerKeywordCrilteria.setPosition(position);;
		}

		if (!Utils.isNullOrEmpty(optimizeGroupName)) {
			customerKeywordCrilteria.setOptimizeGroupName(optimizeGroupName);
		}

		if (!Utils.isNullOrEmpty(creationFromTime)) {
			customerKeywordCrilteria.setCreationToTime(creationFromTime);
		}
		if (!Utils.isNullOrEmpty(creationToTime)) {
			customerKeywordCrilteria.setCreationToTime(creationToTime);
		}

		if (!Utils.isNullOrEmpty(status)) {
			customerKeywordCrilteria.setStatus(status);
		}

		if (!Utils.isNullOrEmpty(invalidRefreshCount)) {
			customerKeywordCrilteria.setInvalidRefreshCount(invalidRefreshCount);
		}
		Page<CustomerKeyword>  page = customerKeywordService.searchCustomerKeywords(new Page<CustomerKeyword>(1, 100000), customerKeywordCrilteria);
		List<CustomerKeyword> customerKeywords = page.getRecords();
		return customerKeywords;
	}
	//查询历史缴费记录
	@RequestMapping(value = "/historyPositionAndIndex/{customerKeywordUuid}/{queryTime}" , method = RequestMethod.GET)
	public ModelAndView historyPositionAndIndex(@PathVariable("customerKeywordUuid")Long customerKeywordUuid, @PathVariable("queryTime")String queryTime,HttpServletRequest request){
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		CustomerKeyword customerKeyword = customerKeywordService.selectById(customerKeywordUuid);
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("terminalType",terminalType);
		condition.put("customerKeywordUuid",customerKeywordUuid);
		condition.put("queryTime",queryTime);
		Page<CustomerKeywordPositionIndexLog> page = customerKeywordPositionIndexLogService.searchCustomerKeywordPositionIndexLogs(new Page<CustomerKeywordPositionIndexLog>(1,10000),condition);
		ModelAndView modelAndView = new ModelAndView("/customerkeyword/historyPositionAndIndex");
		modelAndView.addObject("customerKeyword", customerKeyword);
		modelAndView.addObject("page", page);
		return modelAndView;
	}
}
