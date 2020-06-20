package com.keymanager.ckadmin.vo;

import java.io.Serializable;

public class ProductStatisticsVO implements Serializable {

    private long productId;

    private String productName;

    private String vncUrl;

    private int avgTimesForOneRMB;

    private int count;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVncUrl() {
        return vncUrl;
    }

    public void setVncUrl(String vncUrl) {
        this.vncUrl = vncUrl;
    }

    public int getAvgTimesForOneRMB() {
        return avgTimesForOneRMB;
    }

    public void setAvgTimesForOneRMB(int avgTimesForOneRMB) {
        this.avgTimesForOneRMB = avgTimesForOneRMB;
    }
}
