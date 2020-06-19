package com.keymanager.ckadmin.vo;

import java.io.Serializable;

public class ProductStatisticsVO implements Serializable {

    private String productName;

    private String vncUrl;

    private int avgTimesForOneRMB;

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
