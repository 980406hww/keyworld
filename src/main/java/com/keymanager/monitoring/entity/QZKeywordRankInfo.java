package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

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

    /* 月日 （横坐标）对应爬取的data */
    @TableField(value = "fDate")
    private String date;

    /* 年月日 对应爬取的full_data */
    @TableField(value = "fFullDate")
    private String fullDate;

    /* 涨幅 */
    @TableField(value = "fIncrease")
    private Double increase;

    /* ip来路 */
    @TableField(value = "fIpRoute")
    private String ipRoute;

    /* 百度权重 */
    @TableField(value = "fBaiduWeight")
    private int baiduWeight;

//    /* 百度收录 */
//    @TableField(value = "fBaiduRecord")
//    private String baiduRecord;

    /* 百度收录 */
    @TableField(value = "fRecord")
    private String baiduRecord;

    /* 百度收录日期 */
    @TableField(value = "fBaiduRecordFullDate")
    private String baiduRecordFullDate;

    /* 达到等级 */
    @TableField(value = "fAchieveLevel")
    private int achieveLevel;

    /* 总级数 */
    @TableField(value = "fSumSeries")
    private int sumSeries;

    /* 下级差值 */
    @TableField(value = "fDifferenceValue")
    private Double differenceValue;

    /* 当前级别价格 */
    @TableField(value = "fCurrentPrice")
    private int currentPrice;

    /* 前10的增长数（有正负） */
    @TableField(exist = false)
    private int topTenIncrement;

    /* 前20的增长数 */
    @TableField(exist = false)
    private int topTwentyIncrement;

    /* 前30的增长数 */
    @TableField(exist = false)
    private int topThirtyIncrement;

    /* 前40的增长数 */
    @TableField(exist = false)
    private int topFortyIncrement;

    /* 前50的增长数 */
    @TableField(exist = false)
    private int topFiftyIncrement;

    /* 前10数 */
    @TableField(exist = false)
    private int topTenNum;

    /* 前50数 */
    @TableField(exist = false)
    private int topFiftyNum;

    public int getAchieveLevel() {
        return achieveLevel;
    }

    public void setAchieveLevel(int achieveLevel) {
        this.achieveLevel = achieveLevel;
    }

    public int getSumSeries() {
        return sumSeries;
    }

    public void setSumSeries(int sumSeries) {
        this.sumSeries = sumSeries;
    }

    public Double getDifferenceValue() {
        return differenceValue;
    }

    public void setDifferenceValue(Double differenceValue) {
        this.differenceValue = differenceValue;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getIpRoute() {
        return ipRoute;
    }

    public void setIpRoute(String ipRoute) {
        this.ipRoute = ipRoute;
    }

    public int getBaiduWeight() {
        return baiduWeight;
    }

    public void setBaiduWeight(int baiduWeight) {
        this.baiduWeight = baiduWeight;
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

    public int getTopTenIncrement () {
        return topTenIncrement;
    }

    public void setTopTenIncrement (int topTenIncrement) {
        this.topTenIncrement = topTenIncrement;
    }

    public int getTopTwentyIncrement () {
        return topTwentyIncrement;
    }

    public void setTopTwentyIncrement (int topTwentyIncrement) {
        this.topTwentyIncrement = topTwentyIncrement;
    }

    public int getTopThirtyIncrement () {
        return topThirtyIncrement;
    }

    public void setTopThirtyIncrement (int topThirtyIncrement) {
        this.topThirtyIncrement = topThirtyIncrement;
    }

    public int getTopFortyIncrement () {
        return topFortyIncrement;
    }

    public void setTopFortyIncrement (int topFortyIncrement) {
        this.topFortyIncrement = topFortyIncrement;
    }

    public int getTopFiftyIncrement () {
        return topFiftyIncrement;
    }

    public void setTopFiftyIncrement (int topFiftyIncrement) {
        this.topFiftyIncrement = topFiftyIncrement;
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
}
