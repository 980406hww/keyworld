package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "t_pt_customer_keyword")
public class PtCustomerKeywordTemporary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "fUuid")
    private Long uuid;

    @TableField(value = "fTitle")
    private String title;

    @TableField(value = "fCurrentPosition")
    private Integer currentPosition;

    @TableField(value = "fPrice")
    private Double price;

    @TableField(value = "fCaptureStatus")
    private Integer captureStatus;

    @TableField(value = "fCapturePositionQueryTime")
    private Date capturePositionQueryTime;

    @TableField(value = "fCity")
    private String city;

    @TableField(value = "fCapturePositionCity")
    private String capturePositionCity;

    @TableField(value = "fMark")
    private Integer mark;

    @TableField(value = "fOperaStatus")
    private Integer operaStatus;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCaptureStatus() {
        return captureStatus;
    }

    public void setCaptureStatus(Integer captureStatus) {
        this.captureStatus = captureStatus;
    }

    public Date getCapturePositionQueryTime() {
        return capturePositionQueryTime;
    }

    public void setCapturePositionQueryTime(Date capturePositionQueryTime) {
        this.capturePositionQueryTime = capturePositionQueryTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCapturePositionCity() {
        return capturePositionCity;
    }

    public void setCapturePositionCity(String capturePositionCity) {
        this.capturePositionCity = capturePositionCity;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Integer getOperaStatus() {
        return operaStatus;
    }

    public void setOperaStatus(Integer operaStatus) {
        this.operaStatus = operaStatus;
    }
}
