package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *     关键词表
 * </p>
 * @author shunshikj40
 */
@TableName("sys_customer_keyword")
public class SysCustomerKeyword implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站点ID
     */
    @TableField(value = "QS_ID")
    private Long qsId;

    /**
     * URL
     */
    @TableField(value = "URL")
    private String url;
    /**
     * 终端类型
     */
    @TableField(value = "TERMINAL_TYPE")
    private String terminalType;
    /**
     * 客户ID
     */
    @TableField(value = "CUSTOMER_ID")
    private Long customerId;
    /**
     * 关键词
     */
    @TableField(value = "KEYWORD")
    private String keyword;
    /**
     * 原排名
     */
    @TableField(value = "INITIAL_POSITION")
    private Integer initialPosition;
    /**
     * 现排名
     */
    @TableField(value = "CURRENT_POSITION")
    private Integer currentPosition;
    /**
     * 预计刷量
     */
    @TableField(value = "OPTIMIZE_PLAN_COUNT")
    private Integer optimizePlanCount;
    /**
     * 已刷
     */
    @TableField(value = "OPTIMIZED_COUNT")
    private Integer optimizedCount;
    /**
     * 创建日期
     */
    @TableField(value = "CREATE_TIME")
    private Date createTime;

    public Long getQsId() {
        return qsId;
    }

    public void setQsId(Long qsId) {
        this.qsId = qsId;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
}
