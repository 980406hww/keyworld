package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    private static Logger logger = LoggerFactory.getLogger(QZSettingController.class);

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    //跳转添加或修改用户页面
//    @RequiresPermissions("/internal/productKeyword/searchProductKeywords")
    @GetMapping(value = "/toQZSetttings")
    public ModelAndView toQZSetttings() {
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
            List<QZSearchEngineVO> qzSearchEngine = qzSettingService
                .searchQZSettingSearchEngineMap(qzSettingCriteria, 0);
            resultBean.setCode(0);
            resultBean.setMsg("获取搜索引擎映射列表成功");
            resultBean.setData(qzSearchEngine);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/toQzsettingSf", method = RequestMethod.GET)
    public ModelAndView toQzsettingSf() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsettingSf");
        return mv;
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/toQzsettingAdd", method = RequestMethod.GET)
    public ModelAndView toQzsettingAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsettingAdd");
        return mv;
    }

}
