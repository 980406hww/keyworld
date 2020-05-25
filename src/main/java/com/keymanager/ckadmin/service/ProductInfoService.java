package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.entity.ProductInfo;
import java.util.List;

public interface ProductInfoService {
    ProductInfo getProductInfo(long id);

    Long getProductIdByName(String name);

    Long getProductId(ProductInfo productInfo);

    Page<ProductInfo> getProductsByName(Page<ProductInfo> page, String name);

    void deleteProduct(long uuid);

    void updateProduct(ProductInfo productInfo);

    void addProduct(ProductInfo productInfo);

    List<ProductInfo> getAllProduct();

    void updateProductPriceForUuids(List<Long> uuids, String productPrice);
}
