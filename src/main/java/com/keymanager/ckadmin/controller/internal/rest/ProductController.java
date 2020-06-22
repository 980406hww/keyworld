package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.ProductCriteria;
import com.keymanager.ckadmin.entity.ProductInfo;
import com.keymanager.ckadmin.service.ProductInfoService;

import java.util.Date;
import java.util.List;

import java.util.Map;

import com.keymanager.ckadmin.vo.ProductStatisticsVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/internal/productManage")
public class ProductController {
    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Resource
    private ProductInfoService productInfoService;

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
    @RequestMapping(value = "/toProductInfo", method = RequestMethod.GET)
    public ModelAndView toProductManage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productManage/productManager");
        return mv;
    }

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
    @GetMapping(value = "/toAddProduct")
    public ModelAndView toAddProduct() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productManage/productAdd");
        return mv;
    }

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
    @GetMapping(value = "/toUpdateProduct")
    public ModelAndView toUpdateMachineInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productManage/productUpdate");
        return mv;
    }

    @RequiresPermissions(value = "/internal/productManage/toProductStatistics" )
    @RequestMapping(value = "/toProductStatistics", method = RequestMethod.GET)
    public ModelAndView toProductStatistics() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productManage/productStatistics");
        return mv;
    }

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
    @GetMapping(value = "/getAllProduct")
    public ResultBean getAllProduct(){
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

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
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

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
    @PostMapping("/updateProductInfo")
    public ResultBean updateProduct(@RequestBody ProductInfo productInfo) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            productInfo.setUpdateTime(new Date());
            productInfoService.updateProduct(productInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
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

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
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

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
    @PostMapping("/addProduct")
    public ResultBean addProduct(@RequestBody ProductInfo productInfo) {
        ResultBean resultBean = new ResultBean();
        try {
            productInfo.setCreateTime(new Date());
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

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
    @PostMapping("/updateProductPriceForUuids")
    public ResultBean updateProductPriceForUuids(@RequestBody Map<String, Object> requestMap){
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<Long> uuids = (List<Long>) requestMap.get("uuids");
            String productPrice = (String) requestMap.get("productPrice");
            productInfoService.updateProductPriceForUuids(uuids, productPrice);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @RequiresPermissions(value = "/internal/productManage/toProductInfo" )
    @GetMapping(value = "/getSupperProduct")
    public ResultBean getSupperProduct(){
        ResultBean resultBean = new ResultBean();
        try {
            List<ProductInfo> products = productInfoService.getSupperProduct();
            resultBean.setData(products);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions(value = "/internal/productManage/toProductStatistics")
    @PostMapping(value = "/getProductStatistics")
    public ResultBean getProductStatistics(@RequestBody ProductCriteria criteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<ProductStatisticsVO> list = productInfoService.getAllProductStatistics(criteria.getProductId());
            resultBean.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
