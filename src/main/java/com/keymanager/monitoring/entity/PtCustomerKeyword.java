package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "cms_keyword")
public class PtCustomerKeyword implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "fUuid", type= IdType.AUTO)
    private Long keywordId;

    @TableField(exist = false)
    private Long customerUuid;

    @TableField(value = "COMPANY_CODE")
    private String companyCode;

    @TableField(value = "KEYWORD")
    private String keyword;

    @TableField(value = "URL")
    private String url;

    @TableField(value = "TITLE")
    private String title;

    @TableField(value = "SEARCH_ENGINE")
    private String searchEngine;

    @TableField(value = "TERMINAL_TYPE")
    private String terminalType;

    @TableField(value = "BEAR_PAW_NUM")
    private String bearPawNumber;

    @TableField(value = "STATUS")
    private Integer status;

    @TableField(value = "CITY")
    private String city;

    @TableField(value = "CURRENT_POSITION")
    private Integer currentPosition;

    @TableField(value = "CAPTURE_POSITION_CITY")
    private String capturePositionCity;

    @TableField(value = "PRICE_PER_DAY")
    private Double pricePreDay;

    @TableField(value = "CAPTURE_POSITION_TIME")
    private Date capturePositionQueryTime;

    @TableField(value = "SUB_DATE")
    private Date subDate;

    @TableField(value = "COMP_STATUS")
    private Boolean compStatus;

    public Long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getCapturePositionCity() {
        return capturePositionCity;
    }

    public void setCapturePositionCity(String capturePositionCity) {
        this.capturePositionCity = capturePositionCity;
    }

    public Double getPricePreDay() {
        return pricePreDay;
    }

    public void setPricePreDay(Double pricePreDay) {
        this.pricePreDay = pricePreDay;
    }

    public Date getCapturePositionQueryTime() {
        return capturePositionQueryTime;
    }

    public void setCapturePositionQueryTime(Date capturePositionQueryTime) {
        this.capturePositionQueryTime = capturePositionQueryTime;
    }

    public Date getSubDate() {
        return subDate;
    }

    public void setSubDate(Date subDate) {
        this.subDate = subDate;
    }

    public Boolean getCompStatus() {
        return compStatus;
    }

    public void setCompStatus(Boolean compStatus) {
        this.compStatus = compStatus;
    }
}
