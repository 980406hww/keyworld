package com.keymanager.monitoring.vo;

import java.util.Date;

public class UpdateCustomerKeywordPositionVO {

    private Long customerKeywordUuid;
    private String type;
    private Integer position;
    private Date capturePositionQueryTime;
    private Double todayFee;
    private String ip;
    private String city;

    public Long getCustomerKeywordUuid() {
        return customerKeywordUuid;
    }

    public void setCustomerKeywordUuid(Long customerKeywordUuid) {
        this.customerKeywordUuid = customerKeywordUuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Date getCapturePositionQueryTime() {
        return capturePositionQueryTime;
    }

    public void setCapturePositionQueryTime(Date capturePositionQueryTime) {
        this.capturePositionQueryTime = capturePositionQueryTime;
    }

    public Double getTodayFee() {
        return todayFee;
    }

    public void setTodayFee(Double todayFee) {
        this.todayFee = todayFee;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
