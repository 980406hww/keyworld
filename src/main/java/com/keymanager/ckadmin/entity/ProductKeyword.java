package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 新关键字表
 * </p>
 *
 * @author lhc
 * @since 2019-09-05
 */
@TableName("t_product_keyword")
public class ProductKeyword {

    private static final long serialVersionUID = 1L;

    /**
     * 单词表ID
     */
    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    /**
     * 客户ID
     */
    @TableField("fCustomerUuid")
    private Long customerUuid;

    /**
     * 客户名称
     */
    @TableField(exist = false)
    private String contactPerson;

    /**
     * 关键字
     */
    @TableField("fKeyword")
    private String keyword;

    /**
     * 关键字状态 0-暂不操作，1-激活，2-新增  3-下架
     */
    @TableField("fStatus")
    private Integer status;

    /**
     * 链接
     */
    @TableField("fUrl")
    private String url;

    /**
     * 原始链接
     */
    @TableField("fOriginalUrl")
    private String originalUrl;

    /**
     * 终端类型
     */
    @TableField("fTerminalType")
    private String terminalType;

    /**
     * 搜索引擎
     */
    @TableField("fSearchEngine")
    private String searchEngine;

    /**
     * 熊掌号
     */
    @TableField("fBearPawNumber")
    private String bearPawNumber;

    /**
     * 初始排名
     */
    @TableField("fInitialPosition")
    private Integer initialPosition;

    /**
     * 当前排名
     */
    @TableField("fCurrentPosition")
    private Integer currentPosition;

    /**
     * 指数
     */
    @TableField("fIndexCount")
    private Integer indexCount;

    /**
     * 第一收费
     */
    @TableField("fPositionFirstFee")
    private BigDecimal positionFirstFee;

    /**
     * 第二收费
     */
    @TableField("fPositionSecondFee")
    private BigDecimal positionSecondFee;

    /**
     * 第三收费
     */
    @TableField("fPositionThirdFee")
    private BigDecimal positionThirdFee;

    /**
     * 第四收费
     */
    @TableField("fPositionForthFee")
    private BigDecimal positionForthFee;

    /**
     * 第五收费
     */
    @TableField("fPositionFifthFee")
    private BigDecimal positionFifthFee;

    /**
     * 首页收费
     */
    @TableField("fPositionFirstPageFee")
    private BigDecimal positionFirstPageFee;

    /**
     * 收费方式: PerDay/PerMonth
     */
    @TableField("fCollectMethod")
    private String collectMethod;

    /**
     * 创建时间
     */
    @TableField("fCreateTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("fUpdateTime")
    private Date updateTime;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
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

    public Integer getIndexCount() {
        return indexCount;
    }

    public void setIndexCount(Integer indexCount) {
        this.indexCount = indexCount;
    }

    public BigDecimal getPositionFirstFee() {
        return positionFirstFee;
    }

    public void setPositionFirstFee(BigDecimal positionFirstFee) {
        this.positionFirstFee = positionFirstFee;
    }

    public BigDecimal getPositionSecondFee() {
        return positionSecondFee;
    }

    public void setPositionSecondFee(BigDecimal positionSecondFee) {
        this.positionSecondFee = positionSecondFee;
    }

    public BigDecimal getPositionThirdFee() {
        return positionThirdFee;
    }

    public void setPositionThirdFee(BigDecimal positionThirdFee) {
        this.positionThirdFee = positionThirdFee;
    }

    public BigDecimal getPositionForthFee() {
        return positionForthFee;
    }

    public void setPositionForthFee(BigDecimal positionForthFee) {
        this.positionForthFee = positionForthFee;
    }

    public BigDecimal getPositionFifthFee() {
        return positionFifthFee;
    }

    public void setPositionFifthFee(BigDecimal positionFifthFee) {
        this.positionFifthFee = positionFifthFee;
    }

    public BigDecimal getPositionFirstPageFee() {
        return positionFirstPageFee;
    }

    public void setPositionFirstPageFee(BigDecimal positionFirstPageFee) {
        this.positionFirstPageFee = positionFirstPageFee;
    }

    public String getCollectMethod() {
        return collectMethod;
    }

    public void setCollectMethod(String collectMethod) {
        this.collectMethod = collectMethod;
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
}
