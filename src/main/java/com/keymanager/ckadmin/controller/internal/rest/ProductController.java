package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.ProductCriteria;
import com.keymanager.ckadmin.entity.ProductInfo;
import com.keymanager.ckadmin.service.ProductInfoService;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/productManage")
public class ProductController {
    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Resource
    private ProductInfoService productInfoService;

    @RequiresPermissions(value = "/internal/productManage/modifyProductInfo" )
    @RequestMapping(value = "/toProductInfo", method = RequestMethod.GET)
    public ModelAndView toProductManage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productManage/productManager");
        return mv;
    }

    @RequiresPermissions(value = "/internal/productManage/modifyProductInfo" )
    @GetMapping(value = "/toAddProduct")
    public ModelAndView toAddProduct() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productManage/productAdd");
        return mv;
    }

    @RequiresPermissions(value = "/internal/productManage/modifyProductInfo" )
    @GetMapping(value = "/toUpdateProduct")
    public ModelAndView toUpdateMachineInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productManage/productUpdate");
        return mv;
    }

    @RequiresPermissions(value = "/internal/productManage/modifyProductInfo" )
    @GetMapping(value = "/getAllProduct")
    public ResultBean getAllProudct(){
        ResultBean resultBean = new ResultBean();
        try {
            List<ProductInfo> productInfos = productInfoService.getAllProduct();
            resultBean.setData(productInfos);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions(value = "/internal/productManage/modifyProductInfo" )
    @GetMapping("/getProductInfoByID/{uuid}")
    public ResultBean getProducts(@PathVariable("uuid") int uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            ProductInfo productInfo = productInfoService.getProductInfo(uuid);
            resultBean.setData(productInfo);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions(value = "/internal/productManage/modifyProductInfo" )
    @PostMapping("/updateProductInfo")
    public ResultBean updateProduct(@RequestBody ProductInfo productInfo) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            productInfo.setAlterDate(new Date());
            productInfoService.updateProduct(productInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions(value = "/internal/productManage/modifyProductInfo" )
    @PostMapping("/getProductInfo")
    public ResultBean getProducts(@RequestBody ProductCriteria criteria) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        if ("init".equals(criteria.getInit()))
            return resultBean;
        try {
            Page<ProductInfo> page=new Page<>(criteria.getPage(),criteria.getLimit());
            page = productInfoService.getProductsByName(page,criteria.getProductName());
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions(value = "/internal/productManage/modifyProductInfo" )
    @GetMapping("/deleteProduct/{uuid}")
    public ResultBean deleteProduct(@PathVariable("uuid") int uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            productInfoService.deleteProduct(uuid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions(value = "/internal/productManage/modifyProductInfo" )
    @PostMapping("/addProduct")
    public ResultBean addProduct(@RequestBody ProductInfo productInfo) {
        ResultBean resultBean = new ResultBean();
        try {
            productInfo.setCreateDate(new Date());
            productInfoService.addProduct(productInfo);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

}
