package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.dao.ProductInfoDao;
import com.keymanager.ckadmin.entity.ProductInfo;
import com.keymanager.ckadmin.service.ProductInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

@Service(value = "productInfoService")
public class ProdcutInfoServiceImpl implements ProductInfoService {

    @Resource
    private ProductInfoDao productInfoDao;

    @Override
    public ProductInfo getProductInfo(int id) {
        return productInfoDao.selectById(id);
    }

    @Override
    public ProductInfo getProductByName(String name) {
        return productInfoDao.selectProductByName(name);
    }

    @Override
    public List<ProductInfo> getProductsByName(String name) {
        return productInfoDao.getProductsByName(name);
    }

    @Override
    public void deleteProduct(int uuid) {
        productInfoDao.deleteById(uuid);
    }

    @Override
    public void updateProduct(ProductInfo productInfo) {
        productInfoDao.updateAllColumnById(productInfo);
    }

    @Override
    public void addProduct(ProductInfo productInfo) {
        productInfoDao.insert(productInfo);
    }


}
