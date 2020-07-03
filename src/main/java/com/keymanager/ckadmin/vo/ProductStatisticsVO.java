package com.keymanager.ckadmin.vo;

import java.io.Serializable;

/**
 * 产品统计页的数据包装类
 */
public class ProductStatisticsVO implements Serializable {


    private long productId;

    private String productName;

    private String productSuppliers;

    private int productAvgTimes;

    private String vncUrl;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSuppliers() {
        return productSuppliers;
    }

    public void setProductSuppliers(String productSuppliers) {
        this.productSuppliers = productSuppliers;
    }

    public int getProductAvgTimes() {
        return productAvgTimes;
    }

    public void setProductAvgTimes(int productAvgTimes) {
        this.productAvgTimes = productAvgTimes;
    }

    public String getVncUrl() {
        return vncUrl;
    }

    public void setVncUrl(String vncUrl) {
        this.vncUrl = vncUrl;
    }
}
