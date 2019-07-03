package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;

import java.util.Date;

/**
 * @Author zhoukai
 * @Date 2018/12/6 10:56
 **/
@TableName(value = "t_qz_keyword_rank_info")
public class QZKeywordRankInfo extends BaseEntity {

    /* 全站ID */
    @TableField(value = "fQZSettingUuid")
    private Long qzSettingUuid;

    /* 终端类型 */
    @TableField(value = "fTerminalType")
    private String terminalType;

    /* 网站类型 */
    @TableField(value = "fWebsiteType")
    private String websiteType;

    /* 数据处理状态 0 不处理 1 处理 */
    @TableField(value = "fDataProcessingStatus")
    private boolean dataProcessingStatus;

    /* 前10 对应爬取的t1 */
    @TableField(value = "fTopTen")
    private String topTen;

    /* 前20 对应爬取的t2 */
    @TableField(value = "fTopTwenty")
    private String topTwenty;

    /* 前30 对应爬取的t3 */
    @TableField(value = "fTopThirty")
    private  String topThirty;

    /* 前40 对应爬取的t4 */
    @TableField(value = "fTopForty")
    private String topForty;

    /* 前50 对应爬取的t5 */
    @TableField(value = "fTopFifty")
    private String topFifty;

    /* 前100 */
    @TableField(value = "fTopHundred")
    private String topHundred;

    @TableField(value = "fCreateTopTenNum")
    private Integer createTopTenNum;

    @TableField(value = "fCreateTopFiftyNum")
    private Integer createTopFiftyNum;

    /* 月日 （横坐标）对应爬取的data */
    @TableField(value = "fDate")
    private String date;

    /* 年月日 对应爬取的full_data */
    @TableField(value = "fFullDate", strategy = FieldStrategy.IGNORED)
    private String fullDate;

    /* 涨幅 */
    @TableField(value = "fIncrease")
    private Double increase;

    /* ip来路 */
    @TableField(value = "fIpRoute", strategy = FieldStrategy.IGNORED)
    private String ipRoute;

    /* 百度收录 */
    @TableField(value = "fRecord", strategy = FieldStrategy.IGNORED)
    private String baiduRecord;

    /* 百度收录日期 */
    @TableField(value = "fBaiduRecordFullDate", strategy = FieldStrategy.IGNORED)
    private String baiduRecordFullDate;

    /* 曲线达标时间 */
    @TableField(value = "fAchieveTime", strategy = FieldStrategy.IGNORED)
    private Date achieveTime;

    /* 达到等级 */
    @TableField(value = "fAchieveLevel")
    private Integer achieveLevel;

    /* 总级数 */
    @TableField(value = "fSumSeries")
    private Integer sumSeries;

    /* 下级差值 */
    @TableField(value = "fDifferenceValue")
    private Double differenceValue;

    /* 当前级别价格 */
    @TableField(value = "fCurrentPrice")
    private Integer currentPrice;

    /* 今日top10差值 */
    @TableField(value = "fTodayDifference")
    private Integer todayDifference;

    /* 前10数 */
    @TableField(exist = false)
    private int topTenNum;

    /* 前50数 */
    @TableField(exist = false)
    private int topFiftyNum;

    @TableField(exist = false)
    private String createMonthDay;

    public Integer getAchieveLevel() {
        return achieveLevel;
    }

    public void setAchieveLevel(Integer achieveLevel) {
        this.achieveLevel = achieveLevel;
    }

    public Integer getSumSeries() {
        return sumSeries;
    }

    public void setSumSeries(Integer sumSeries) {
        this.sumSeries = sumSeries;
    }

    public Double getDifferenceValue() {
        return differenceValue;
    }

    public void setDifferenceValue(Double differenceValue) {
        this.differenceValue = differenceValue;
    }

    public Integer getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Integer currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getIpRoute() {
        return ipRoute;
    }

    public void setIpRoute(String ipRoute) {
        this.ipRoute = ipRoute;
    }

    public String getBaiduRecord() {
        return baiduRecord;
    }

    public void setBaiduRecord(String baiduRecord) {
        this.baiduRecord = baiduRecord;
    }

    public Long getQzSettingUuid () {
        return qzSettingUuid;
    }

    public void setQzSettingUuid (Long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public String getWebsiteType () {
        return websiteType;
    }

    public void setWebsiteType (String websiteType) {
        this.websiteType = websiteType;
    }

    public String getTopTen () {
        return topTen;
    }

    public void setTopTen (String topTen) {
        this.topTen = topTen;
    }

    public String getTopTwenty () {
        return topTwenty;
    }

    public void setTopTwenty (String topTwenty) {
        this.topTwenty = topTwenty;
    }

    public String getTopThirty () {
        return topThirty;
    }

    public void setTopThirty (String topThirty) {
        this.topThirty = topThirty;
    }

    public String getTopForty () {
        return topForty;
    }

    public void setTopForty (String topForty) {
        this.topForty = topForty;
    }

    public String getTopFifty () {
        return topFifty;
    }

    public void setTopFifty (String topFifty) {
        this.topFifty = topFifty;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getFullDate () {
        return fullDate;
    }

    public void setFullDate (String fullDate) {
        this.fullDate = fullDate;
    }

    public Double getIncrease () {
        return increase;
    }

    public void setIncrease (Double increase) {
        this.increase = increase;
    }

    public int getTopTenNum () {
        return topTenNum;
    }

    public void setTopTenNum (int topTenNum) {
        this.topTenNum = topTenNum;
    }

    public int getTopFiftyNum () {
        return topFiftyNum;
    }

    public void setTopFiftyNum (int topFiftyNum) {
        this.topFiftyNum = topFiftyNum;
    }

    public String getBaiduRecordFullDate() {
        return baiduRecordFullDate;
    }

    public void setBaiduRecordFullDate(String baiduRecordFullDate) {
        this.baiduRecordFullDate = baiduRecordFullDate;
    }

    public Integer getTodayDifference() {
        return todayDifference;
    }

    public void setTodayDifference(Integer todayDifference) {
        this.todayDifference = todayDifference;
    }

    public String getTopHundred () {
        return topHundred;
    }

    public void setTopHundred (String topHundred) {
        this.topHundred = topHundred;
    }

    public Integer getCreateTopTenNum () {
        return createTopTenNum;
    }

    public void setCreateTopTenNum (Integer createTopTenNum) {
        this.createTopTenNum = createTopTenNum;
    }

    public Integer getCreateTopFiftyNum () {
        return createTopFiftyNum;
    }

    public void setCreateTopFiftyNum (Integer createTopFiftyNum) {
        this.createTopFiftyNum = createTopFiftyNum;
    }

    public Date getAchieveTime() {
        return achieveTime;
    }

    public void setAchieveTime(Date achieveTime) {
        this.achieveTime = achieveTime;
    }

    public boolean getDataProcessingStatus () {
        return dataProcessingStatus;
    }

    public void setDataProcessingStatus (boolean dataProcessingStatus) {
        this.dataProcessingStatus = dataProcessingStatus;
    }

    public String getCreateMonthDay () {
        return createMonthDay;
    }

    public void setCreateMonthDay (String createMonthDay) {
        this.createMonthDay = createMonthDay;
    }
}
