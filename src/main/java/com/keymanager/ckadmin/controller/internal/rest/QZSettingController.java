package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.service.QZSettingService;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @Resource(name = "QZSettingService2")
    private QZSettingService qzSettingService;
    //跳转添加或修改用户页面
//    @RequiresPermissions("/internal/productKeyword/searchProductKeywords")
    @GetMapping(value = "/toQZSetttings")
    public ModelAndView toCustomersAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsetting");
        return mv;
    }

    @PostMapping("/getQZSettings")
    public ResultBean getQZSettings(@RequestBody QZSettingCriteria qzSettingCriteria){
        ResultBean resultBean = new ResultBean();
        Page<QZSetting> page= new Page<>(qzSettingCriteria.getPage(), qzSettingCriteria.getLimit());
        page = qzSettingService.searchQZSetting(page, qzSettingCriteria);
        resultBean.setCode(0);
        resultBean.setMsg("success");
        resultBean.setData(page.getRecords());
        resultBean.setCount(page.getTotal());
        return resultBean;
    }


}
