package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.ProductInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductInfoDao extends BaseMapper<ProductInfo> {

    ProductInfo selectProductByName(@Param(value = "name") String name);

    List<ProductInfo> getProductsByName(@Param(value = "name") String name );

}
