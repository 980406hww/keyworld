package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.entity.ProductInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductInfoDao extends BaseMapper<ProductInfo> {

    Long getProductIdByName(@Param(value = "name") String name);

    Long getProductId(@Param(value = "productInfo") ProductInfo productInfo);

    List<ProductInfo> getProductsByName(Page<ProductInfo> page, @Param(value = "name") String name );

    List<ProductInfo> getProductsByName(@Param(value = "name") String name );

    void updateProductPriceForUuids(@Param(value = "uuids") List<Long> uuids, @Param(value = "productPrice") String productPrice);
}
