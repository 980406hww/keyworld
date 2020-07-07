package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.ProductCriteria;
import com.keymanager.ckadmin.entity.ProductInfo;
import com.keymanager.ckadmin.vo.ProductStatisticsVO;

import java.text.ParseException;
import java.util.List;

public interface ProductInfoService {
    ProductInfo getProductInfo(long id);

    Long getProductIdByName(String name);

    Long getProductId(ProductInfo productInfo);

    Page<ProductInfo> getProductsByName(Page<ProductInfo> page, String name);

    Page<ProductInfo> getProducts(Page<ProductInfo> page, ProductCriteria criteria);

    void deleteProduct(long uuid);

    void updateProduct(ProductInfo productInfo);

    void addProduct(ProductInfo productInfo);

    List<ProductInfo> getAllProduct();

    void updateProductPriceForUuids(List<Long> uuids, String productPrice);

    List<ProductStatisticsVO> getAllProductStatistics(Long productId) throws ParseException;

    List<ProductInfo> getSupperProduct();

    List<ProductStatisticsVO> getProductStatisticsForTerminalType(ProductCriteria productCriteria) throws ParseException;
}
