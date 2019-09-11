package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName QZSettingController
 * @Description 新后台全站入口
 * @Author lhc
 * @Date 2019/9/6 17:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/internal/qzsetting")
public class QZSettingController {

    private static Logger logger = LoggerFactory.getLogger(QZSettingController.class);

    @Resource(name = "QZSettingService2")
    private QZSettingService qzSettingService;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    //跳转添加或修改用户页面
//    @RequiresPermissions("/internal/productKeyword/searchProductKeywords")

    @GetMapping(value = "/getActiveCustomer")
    public List<Customer> getActiveCustomer(){
        CustomerCriteria customerCriteria = new CustomerCriteria();
        List<Customer> customerList = customerService.getActiveCustomerSimpleInfo(customerCriteria);
        return customerList;
    }

    @GetMapping(value = "/getUserInfo")
    public List<UserInfo> getUserInfo(){
        List<UserInfo> activeUsers = userInfoService.findActiveUsers();
        return activeUsers;
    }

    @GetMapping(value = "/toQZSetttings")
    public ModelAndView toCustomersAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsetting");
        return mv;
    }

    @PostMapping("/getQZSettings")
    public ResultBean getQZSettings(@RequestBody QZSettingCriteria qzSettingCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<QZSetting> page = new Page<>(qzSettingCriteria.getPage(),
                qzSettingCriteria.getLimit());
            page = qzSettingService.searchQZSetting(page, qzSettingCriteria);
            resultBean.setCode(0);
            resultBean.setMsg("success");
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;

    }

    @GetMapping("/getQZSettingSearchEngineMap")
    public ResultBean getQZSettingSearchEngineMap(QZSettingCriteria qzSettingCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            List<QZSearchEngineVO> qzSearchEngine = qzSettingService.searchQZSettingSearchEngineMap(qzSettingCriteria, 0);
            resultBean.setCode(0);
            resultBean.setMsg("获取搜索引擎映射列表成功");
            resultBean.setData(qzSearchEngine);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }


}
