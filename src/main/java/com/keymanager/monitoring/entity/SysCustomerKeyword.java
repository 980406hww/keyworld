package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "sys_customer_keyword")
public class SysCustomerKeyword implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type= IdType.AUTO)
    private Long id;

    @TableField(value = "KEYWORD_ID")
    private Long keywordId;

    @TableField(value = "QS_ID")
    private Long qsId;

    @TableField(value = "KEYWORD")
    private String keyword;

    @TableField(value = "URL")
    private String url;

    @TableField(value = "TERMINAL_TYPE")
    private String terminalType;

    @TableField(value = "KEYWORD_GROUP")
    private String keywordGroup;

    @TableField(value = "STATUS")
    private Integer status;

    @TableField(value = "OPERA_STATUS")
    private Integer operaStatus;

    @TableField(value = "INITIAL_POSITION")
    private Integer initialPosition;

    @TableField(value = "CURRENT_POSITION")
    private Integer currentPosition;

    @TableField(value = "CAPTURE_POSITION_TIME")
    private Date capturePositionTime;

    @TableField(value = "DELETE_FLAG")
    private Integer deleteFlag;

    @TableField(value = "OPTIMIZE_PLAN_COUNT")
    private Integer optimizePlanCount;

    @TableField(value = "OPTIMIZED_COUNT")
    private Integer optimizedCount;

    @TableField(value = "CREATE_TIME")
    private Date createTime;

    @TableField(value = "UPDATE_TIME")
    private Date updateTime;

    @TableField(exist = false)
    private String searchEngine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
    }

    public Long getQsId() {
        return qsId;
    }

    public void setQsId(Long qsId) {
        this.qsId = qsId;
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

    public Integer getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(Integer initialPosition) {
        this.initialPosition = initialPosition;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
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

    public Integer getOptimizePlanCount() {
        return optimizePlanCount;
    }

    public void setOptimizePlanCount(Integer optimizePlanCount) {
        this.optimizePlanCount = optimizePlanCount;
    }

    public Integer getOptimizedCount() {
        return optimizedCount;
    }

    public void setOptimizedCount(Integer optimizedCount) {
        this.optimizedCount = optimizedCount;
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

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }
}
