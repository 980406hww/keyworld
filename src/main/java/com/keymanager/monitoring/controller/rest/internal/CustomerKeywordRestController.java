package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CustomerKeywordCrilteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.CustomerService;
import com.keymanager.monitoring.service.UserService;
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
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Map;

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

	@RequestMapping(value = "/clearTitle", method = RequestMethod.POST)
	public ResponseEntity<?> clearTitle(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception{
		String uuids = (String) requestMap.get("uuids");
		String customerUuid = (String) requestMap.get("customerUuid");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		try{
			customerKeywordService.clearTitle(uuids, customerUuid, terminalType);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value="searchCustomerKeywords/{status}/{customerUuid}" , method=RequestMethod.GET)
	public ModelAndView searchCustomerKeywords(@PathVariable("status") int status,@PathVariable("customerUuid") Long customerUuid,@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request){
		CustomerKeywordCrilteria customerKeywordCrilteria = new CustomerKeywordCrilteria();
		customerKeywordCrilteria.setStatus(status);
		customerKeywordCrilteria.setCustomerUuid(customerUuid);
		return constructCustomerKeywordModelAndView(request, customerKeywordCrilteria, currentPageNumber+"", pageSize+"");
	}

	@RequestMapping(value="searchCustomerKeywords" , method=RequestMethod.POST)
	public ModelAndView searchCustomerKeywords(HttpServletRequest request, CustomerKeywordCrilteria customerKeywordCrilteria, String currentPageNumber, String pageSize) {
		return  constructCustomerKeywordModelAndView(request, customerKeywordCrilteria, currentPageNumber, pageSize);
	}

	private ModelAndView constructCustomerKeywordModelAndView(HttpServletRequest request, CustomerKeywordCrilteria customerKeywordCrilteria, String currentPage, String pageSize) {
		ModelAndView modelAndView = new ModelAndView("/customerkeyword/customerKeywordList");
		HttpSession session = request.getSession();
		String userID = (String) session.getAttribute("username");
		User user = userService.getUser(userID);
		Customer customer = customerService.selectById(customerKeywordCrilteria.getCustomerUuid());
		Page<CustomerKeyword> page = customerKeywordService.searchCustomerKeywords(new Page<CustomerKeyword>(Integer.parseInt(currentPage), Integer.parseInt(pageSize)), customerKeywordCrilteria);
		modelAndView.addObject("customerKeywordVO", customerKeywordCrilteria);
		modelAndView.addObject("page", page);
		modelAndView.addObject("user", user);
		modelAndView.addObject("customer", customer);
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
	public ResponseEntity<?> updateCustomerKeywordGroupName(@RequestBody CustomerKeyword customerKeyword, HttpServletRequest request) {
		try {
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			customerKeyword.setTerminalType(terminalType);
			customerKeywordService.updateCustomerKeywordGroupName(customerKeyword);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.OK);
		}
	}
	//修改选中关键字组名
	@RequestMapping(value = "/changeOptimizationGroup", method = RequestMethod.POST)
	public ResponseEntity<?> changeOptimizationGroup(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
		try {
			List<String> customerKeywordUuids = (List<String>) requestMap.get("customerKeywordUuids");
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			String optimizeGroupName = (String) requestMap.get("optimizeGroupName");
			for (String customerKeywordUuid : customerKeywordUuids) {
				CustomerKeyword customerKeyword = new CustomerKeyword();
				customerKeyword.setUuid(Long.parseLong(customerKeywordUuid));
				customerKeyword.setTerminalType(terminalType);
				customerKeyword.setOptimizeGroupName(optimizeGroupName);
				customerKeywordService.changeOptimizationGroup(customerKeyword);
			}
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}
	//关键字Excel上传(简化版)
	@RequestMapping(value = "/uploadsimplecon" , method = RequestMethod.POST)
	public boolean uploadsimplecon(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
		String customerUuid = request.getParameter("customerUuid");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		String path = Utils.getWebRootPath() + "uploadsimplecon" + File.separator + terminalType + File.separator;
		String fileName = customerUuid  + ".xls";
		File targetFile = new File(path, fileName);
		if(!targetFile.exists()){
			targetFile.mkdirs();
		}
		//保存
		try {
			file.transferTo(targetFile);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
	//关键字Excel上传(简化版)
	@RequestMapping(value = "/uploadFullcon" , method = RequestMethod.POST)
	public boolean uploadFullcon(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
		String customerUuid = request.getParameter("customerUuid");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		String path = Utils.getWebRootPath() + "uploadFullcon" + File.separator + terminalType + File.separator;
		String fileName = customerUuid  + ".xls";
		File targetFile = new File(path, fileName);
		if(!targetFile.exists()){
			targetFile.mkdirs();
		}
		//保存
		try {
			file.transferTo(targetFile);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@RequestMapping(value = "/getCustomerKeyword/{uuid}" , method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerKeyword(@PathVariable("uuid")Long uuid){
		return new ResponseEntity<Object>(customerService.getCustomerWithKeywordCount(uuid), HttpStatus.OK);
	}

	@RequestMapping(value = "/delelteCustomerKeyword/{customerKeywordUuid}", method = RequestMethod.GET)
	public ResponseEntity<?> delelteCustomerKeyword(@PathVariable("customerKeywordUuid") Long customerKeywordUuid , HttpServletRequest request) {
		try {
			String entry = (String)request.getSession().getAttribute("entry");
			customerKeywordService.deleteCustomerKeyword(customerKeywordUuid,entry);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/deleteCustomerKeywords" , method = RequestMethod.POST)
	public ResponseEntity<?> deleteCustomerKeywords(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
		try {
			List<String> customerKeywordUuids = (List<String>) requestMap.get("uuids");
			String entry = (String)request.getSession().getAttribute("entry");
			customerKeywordService.deleteCustomerKeywords(customerKeywordUuids,entry);
			return new ResponseEntity<Object>(true , HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/saveCustomerKeyword", method = RequestMethod.POST)
	public ResponseEntity<?> saveCustomerKeyword(@RequestBody CustomerKeyword customerKeyword, HttpServletRequest request) {
		try{
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			String entryType = (String)request.getSession().getAttribute("entry");
//			customerKeywordService.addCustomerKeywordsFromSimpleUI(customerKeywords, terminalType, entryType);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}
}
