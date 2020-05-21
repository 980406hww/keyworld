package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.entity.ProductInfo;
import java.util.List;

public interface ProductInfoService {
    ProductInfo getProductInfo(long id);

    ProductInfo getProductByName(String name);

    Page<ProductInfo> getProductsByName(Page<ProductInfo> page, String name);

    void deleteProduct(long uuid);

    void updateProduct(ProductInfo productInfo);

    void addProduct(ProductInfo productInfo);

    List<ProductInfo> getAllProduct();
}
