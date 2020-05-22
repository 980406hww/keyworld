package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
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
    public ProductInfo getProductInfo(long id) {
        return productInfoDao.selectById(id);
    }

    @Override
    public Long getProductIdByName(String name) {
        return productInfoDao.getProductIdByName(name);
    }

    @Override
    public Page<ProductInfo> getProductsByName(Page<ProductInfo> page,String name) {
        List<ProductInfo> productInfos= productInfoDao.getProductsByName(page,name);
        return page.setRecords(productInfos);
    }

    @Override
    public void deleteProduct(long uuid) {
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

    @Override
    public List<ProductInfo> getAllProduct() {
        return productInfoDao.getProductsByName("");
    }


}
