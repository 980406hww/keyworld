package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

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

    /* 爬取的时间 */
    @TableField(value = "fCrawlTime")
    private Date crawlTime;

    /* 修改的类型 （new，finish） */
    @TableField(value = "fUpdateStatus")
    private String updateStatus;

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

    public Date getCrawlTime () {
        return crawlTime;
    }

    public void setCrawlTime (Date crawlTime) {
        this.crawlTime = crawlTime;
    }

    public String getUpdateStatus () {
        return updateStatus;
    }

    public void setUpdateStatus (String updateStatus) {
        this.updateStatus = updateStatus;
    }
}
