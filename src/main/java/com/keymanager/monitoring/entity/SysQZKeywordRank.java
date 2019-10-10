package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;

/**
 * <p>
 *     站点曲线数据表
 * </p>
 * @author shunshikj40
 */
@TableName("sys_qz_keyword_rank")
public class SysQZKeywordRank implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "QK_ID", type = IdType.ID_WORKER)
    private Long qkId;
    /**
     * 站点ID
     */
    @TableField(value = "QS_ID")
    private Long qsId;
    /**
     * 终端类型
     */
    @TableField(value = "TERMINAL_TYPE")
    private String terminalType;
    /**
     * 网站种类
     */
    @TableField(value = "WEBSITE_TYPE")
    private String websiteType;
    /**
     * 处理类型
     */
    @TableField(value = "PROCESSING_TYPE")
    private Boolean processingType;
    /**
     * 前10曲线数据
     */
    @TableField(value = "TOP_TEN")
    private String topTen;
    /**
     * 前20曲线数据
     */
    @TableField(value = "TOP_TWENTY")
    private String topTwenty;
    /**
     * 前30曲线数据
     */
    @TableField(value = "TOP_THIRTY")
    private String topThirty;
    /**
     * 前40曲线数据
     */
    @TableField(value = "TOP_FORTY")
    private String topForty;
    /**
     * 前50曲线数据
     */
    @TableField(value = "TOP_FIFTY")
    private String topFifty;
    /**
     * 前100曲线数据
     */
    @TableField(value = "TOP_HUNDRED")
    private String topHundred;
    /**
     * 关键词趋势曲线 月日（横坐标）
     */
    @TableField(value = "DATE")
    private String date;
    /**
     * 一周涨幅
     */
    @TableField(value = "INCREASE")
    private Double increase;
    /**
     * 今日top10差值
     */
    @TableField(value = "TODAY_DIFF_VAL")
    private Integer todayDiffVal;
    /**
     * 一周top10差值
     */
    @TableField(value = "ONE_WEEK_DIFF_VAL")
    private Integer oneWeekDiffVal;
    /**
     * 收录曲线 年月日（横坐标）
     */
    @TableField(value = "RECORD_DATE")
    private String recordDate;
    /**
     * 收录曲线
     */
    @TableField(value = "RECORD")
    private String record;
    /**
     * 初始前10
     */
    @TableField(value = "INIT_TOP_TEN_NUM")
    private Integer initTopTenNum;
    /**
     * 初始前50
     */
    @TableField(value = "INIT_TOP_FIFTY_NUM")
    private Integer initTopFiftyNum;

    public Long getQkId() {
        return qkId;
    }

    public void setQkId(Long qkId) {
        this.qkId = qkId;
    }

    public Long getQsId() {
        return qsId;
    }

    public void setQsId(Long qsId) {
        this.qsId = qsId;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
    }

    public Boolean getProcessingType() {
        return processingType;
    }

    public void setProcessingType(Boolean processingType) {
        this.processingType = processingType;
    }

    public String getTopTen() {
        return topTen;
    }

    public void setTopTen(String topTen) {
        this.topTen = topTen;
    }

    public String getTopTwenty() {
        return topTwenty;
    }

    public void setTopTwenty(String topTwenty) {
        this.topTwenty = topTwenty;
    }

    public String getTopThirty() {
        return topThirty;
    }

    public void setTopThirty(String topThirty) {
        this.topThirty = topThirty;
    }

    public String getTopForty() {
        return topForty;
    }

    public void setTopForty(String topForty) {
        this.topForty = topForty;
    }

    public String getTopFifty() {
        return topFifty;
    }

    public void setTopFifty(String topFifty) {
        this.topFifty = topFifty;
    }

    public String getTopHundred() {
        return topHundred;
    }

    public void setTopHundred(String topHundred) {
        this.topHundred = topHundred;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getIncrease() {
        return increase;
    }

    public void setIncrease(Double increase) {
        this.increase = increase;
    }

    public Integer getTodayDiffVal() {
        return todayDiffVal;
    }

    public void setTodayDiffVal(Integer todayDiffVal) {
        this.todayDiffVal = todayDiffVal;
    }

    public Integer getOneWeekDiffVal() {
        return oneWeekDiffVal;
    }

    public void setOneWeekDiffVal(Integer oneWeekDiffVal) {
        this.oneWeekDiffVal = oneWeekDiffVal;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public Integer getInitTopTenNum() {
        return initTopTenNum;
    }

    public void setInitTopTenNum(Integer initTopTenNum) {
        this.initTopTenNum = initTopTenNum;
    }

    public Integer getInitTopFiftyNum() {
        return initTopFiftyNum;
    }

    public void setInitTopFiftyNum(Integer initTopFiftyNum) {
        this.initTopFiftyNum = initTopFiftyNum;
    }
}
