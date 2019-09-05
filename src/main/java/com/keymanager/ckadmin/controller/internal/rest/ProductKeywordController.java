package com.keymanager.ckadmin.controller.internal.rest;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.ProductkeywordCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.ProductKeyword;
import com.keymanager.ckadmin.service.ProductKeywordService;
import com.keymanager.ckadmin.util.ReflectUtils;
import com.keymanager.ckadmin.util.SQLFilterUtils;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 新关键字表 前端控制器
 * </p>
 *
 * @author lhc
 * @since 2019-09-05
 */
@RestController
@RequestMapping("/internal/productKeyword")
public class ProductKeywordController {
    private static Logger logger = LoggerFactory.getLogger(ProductKeywordController.class);

    @Resource(name = "productKeywordService2")
    private ProductKeywordService productKeywordService;

    //跳转添加或修改用户页面
    @RequiresPermissions("/internal/productKeyword/searchProductKeywords")
    @GetMapping(value = "/searchProductKeywords")
    public ModelAndView toCustomersAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productKeywords/productKeyword");
        return mv;
    }

    @RequiresPermissions("/internal/productKeyword/searchProductKeywords")
    @PostMapping(value = "/searchProductKeywords")
    public ResultBean getAlgorithmTestPlans(HttpServletRequest request,
        @RequestBody ProductkeywordCriteria productkeywordCriteria) {
        ResultBean resultBean = new ResultBean();
        if (SQLFilterUtils.sqlInject(productkeywordCriteria.toString())) {
            resultBean.setCode(400);
            resultBean.setMsg("查询参数错误或包含非法字符，请检查后重试！");
            return resultBean;
        }
        try {

            Page<ProductKeyword> page = new Page(productkeywordCriteria.getPage(), productkeywordCriteria.getLimit());
            String orderByField = ReflectUtils
                .getTableFieldValue(Customer.class, productkeywordCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
            }
            if (productkeywordCriteria.getOrderMode() != null && productkeywordCriteria.getOrderMode() == 0) {
                page.setAsc(false);
            }
            page = productKeywordService.searchProductKeywords(page, productkeywordCriteria);
            List<ProductKeyword> customers = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("");
            resultBean.setData(customers);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

}
