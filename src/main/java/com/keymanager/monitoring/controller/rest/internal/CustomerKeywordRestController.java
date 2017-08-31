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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/customerkeyword")
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

	@RequestMapping(value = "/saveCustomerKeyword", method = RequestMethod.POST)
	public ResponseEntity<?> saveCustomerKeywordForm(@RequestBody List<CustomerKeyword> customerKeywords, HttpServletRequest request) {
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
}
