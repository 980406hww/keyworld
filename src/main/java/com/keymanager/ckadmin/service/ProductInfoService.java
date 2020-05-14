package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.ProductInfo;
import java.util.List;

public interface ProductInfoService {
    ProductInfo getProductInfo(int id);

    ProductInfo getProductByName(String name);

    List<ProductInfo> getProductsByName(String name);

    void deleteProduct(int uuid);

    void updateProduct(ProductInfo productInfo);

    void addProduct(ProductInfo productInfo);
}
