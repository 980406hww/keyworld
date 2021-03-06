package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.AdvertisingAllTypeAndCustomerListCriteria;
import com.keymanager.ckadmin.criteria.AdvertisingCriteria;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.entity.Advertising;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.service.AdvertisingService;
import com.keymanager.ckadmin.service.CustomerService;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/advertisingList")
public class AdvertisingController extends SpringMVCBaseController {
    
    private static Logger logger = LoggerFactory.getLogger(AdvertisingController.class);

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "advertisingService2")
    private AdvertisingService advertisingService;

    @GetMapping("/toSaveAdvertising")
    public ModelAndView toSaveAdvertising() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("advertisingList/SaveAdvertising");
        return mv;
    }

    @PostMapping("/saveAdvertising")
    public ResultBean saveAdvertising(@RequestBody Advertising advertising){
        ResultBean resultBean = new ResultBean();
        try{
            advertisingService.saveAdvertising(advertising);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping("/updateAdvertising")
    public ResultBean updateAdvertising(@RequestBody Advertising advertising){
        ResultBean resultBean = new ResultBean();
        try{
            advertisingService.updateAdvertising(advertising);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @GetMapping(value = "/searchAdvertisingAllTypeAndCustomerList/{websiteUuid}")
    public ResultBean searchAdvertisingAllTypeAndCustomerList(@PathVariable Long websiteUuid, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        try {
            CustomerCriteria customerCriteria = new CustomerCriteria();
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")) {
                String loginName = (String) session.getAttribute("username");
                customerCriteria.setLoginName(loginName);
            }
            List<Customer> customerList = customerService.getActiveCustomerSimpleInfo(customerCriteria);
            AdvertisingAllTypeAndCustomerListCriteria advertisingAllTypeAndCustomerListCriteria = advertisingService.searchAdvertisingAllTypeList(websiteUuid);
            advertisingAllTypeAndCustomerListCriteria.setCustomerList(customerList);
            resultBean.setCode(200);
            resultBean.setData(advertisingAllTypeAndCustomerListCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping(value = "/pushAdvertising")
    public ResultBean pushAdvertising(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            advertisingService.pushAdvertising(requestMap);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping("/searchAdvertisingLists")
    public ResultBean searchAdvertisingLists(@RequestBody AdvertisingCriteria advertisingCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<Advertising> page = new Page<>(advertisingCriteria.getPage(), advertisingCriteria.getLimit());
            page = advertisingService.searchAdvertisingList(page, advertisingCriteria);
            List<Advertising> industryDetailList = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(industryDetailList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @GetMapping("/delAdvertising/{uuid}")
    public ResultBean delAdvertising(@PathVariable Long uuid){
        ResultBean resultBean = new ResultBean();
        try{
            advertisingService.delAdvertising(uuid);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping("/delAdvertisings")
    public ResultBean delAdvertisings(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try{
            advertisingService.delAdvertisings(requestMap);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/getAdvertising/{uuid}", method = RequestMethod.GET)
    public ResultBean getAdvertising(@PathVariable Long uuid){
        ResultBean resultBean = new ResultBean();
        try{
            Advertising advertising = advertisingService.getAdvertising(uuid);
            resultBean.setData(advertising);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

}
