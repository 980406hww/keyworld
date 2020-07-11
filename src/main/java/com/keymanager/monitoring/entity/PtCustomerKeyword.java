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

    @TableId(value = "ID", type= IdType.AUTO)
    private Long id;

    @TableField(value = "USER_ID")
    private Long userId;

    @TableField(value = "CUSTOMER_KEYWORD_ID")
    private Long customerKeywordId;

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

    @TableField(value = "KEYWORD_GROUP")
    private String keywordGroup;

    @TableField(value = "STATUS")
    private Integer status;

    @TableField(value = "OPERA_STATUS")
    private Integer operaStatus;

    @TableField(value = "CURRENT_POSITION")
    private Integer currentPosition;

    @TableField(value = "PRICE_PER_DAY")
    private Double pricePreDay;

    @TableField(value = "CAPTURE_POSITION_TIME")
    private Date capturePositionTime;

    @TableField(value = "DELETE_FLAG")
    private Integer deleteFlag;

    @TableField(value = "CREATE_TIME")
    private Date createTime;

    @TableField(value = "UPDATE_TIME")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCustomerKeywordId() {
        return customerKeywordId;
    }

    public void setCustomerKeywordId(Long customerKeywordId) {
        this.customerKeywordId = customerKeywordId;
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

    public String getKeywordGroup() {
        return keywordGroup;
    }

    public void setKeywordGroup(String keywordGroup) {
        this.keywordGroup = keywordGroup;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOperaStatus() {
        return operaStatus;
    }

    public void setOperaStatus(Integer operaStatus) {
        this.operaStatus = operaStatus;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Double getPricePreDay() {
        return pricePreDay;
    }

    public void setPricePreDay(Double pricePreDay) {
        this.pricePreDay = pricePreDay;
    }

    public Date getCapturePositionTime() {
        return capturePositionTime;
    }

    public void setCapturePositionTime(Date capturePositionTime) {
        this.capturePositionTime = capturePositionTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
