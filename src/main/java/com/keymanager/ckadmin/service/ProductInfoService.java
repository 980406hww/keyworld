package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.ProductInfo;
import java.util.List;

public interface ProductInfoService {
    ProductInfo getProductInfo(long id);

    ProductInfo getProductByName(String name);

    List<ProductInfo> getProductsByName(String name);

    void deleteProduct(long uuid);

    void updateProduct(ProductInfo productInfo);

    void addProduct(ProductInfo productInfo);

    List<ProductInfo> getAllProduct();
}
